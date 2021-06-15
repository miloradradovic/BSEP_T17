package bsep.hospital.api;


import bsep.device.model.PatientMessage;
import bsep.hospital.logging.IPAddress;
import bsep.hospital.model.Patient;
import bsep.hospital.model.PatientStatus;
import bsep.hospital.service.PatientService;
import bsep.hospital.service.PatientStatusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/device", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeviceController {

    @Autowired
    PatientStatusService patientStatusService;

    @Autowired
    PatientService patientService;

    @Autowired
    KieSession kieSession;

    private static Logger logger = LogManager.getLogger(DeviceController.class);

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> receivePatientStatus(@RequestBody byte[] encodedMessage) {
        try {
            logger.info("Device is attempting to save patient status.");
            PatientMessage msg = (PatientMessage) SerializationUtils.deserialize(encodedMessage);

            Patient patient = patientService.findOne(msg.getPatientId());

            if(patient == null)
            {
                throw new Exception();
            }
            logger.info("Putting patient status through drools.");
            kieSession.insert(new PatientStatus(patient, msg.getDateTime(), msg.getType(), msg.getMessage()));
            kieSession.getAgenda().getAgendaGroup("doctor-rules").setFocus();
            kieSession.fireAllRules();

            Collection<PatientStatus> newEvents = (Collection<PatientStatus>) kieSession.getObjects(new ClassObjectFilter(PatientStatus.class));
            PatientStatus patientStatus = (PatientStatus) newEvents.toArray()[0];


            Collection<FactHandle> handlers = kieSession.getFactHandles();
            for (FactHandle handle : handlers) {
                Object obj = kieSession.getObject(handle);

                if(obj.getClass() != IPAddress.class)
                    kieSession.delete(handle);
            }
            patientStatusService.saveOne(patientStatus);
            logger.info("Successfully saved patient status.");
              return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Failed to save patient status because patient is not found.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
