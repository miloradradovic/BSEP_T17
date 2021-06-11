package bsep.hospital.api;


import bsep.device.model.PatientMessage;
import bsep.hospital.model.Patient;
import bsep.hospital.model.PatientStatus;
import bsep.hospital.service.PatientService;
import bsep.hospital.service.PatientStatusService;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> receivePatientStatus(@RequestBody byte[] encodedMessage) {
        try {
            PatientMessage msg = (PatientMessage) SerializationUtils.deserialize(encodedMessage);

            Patient patient = patientService.findOne(msg.getPatientId());

            if(patient == null)
            {
                throw new Exception();
            }
            kieSession.insert(new PatientStatus(patient, msg.getDateTime(), msg.getType(), msg.getMessage()));
            kieSession.fireAllRules();

            Collection<PatientStatus> newEvents = (Collection<PatientStatus>) kieSession.getObjects(new ClassObjectFilter(PatientStatus.class));
            PatientStatus patientStatus = (PatientStatus) newEvents.toArray()[0];


            Collection<FactHandle> handlers = kieSession.getFactHandles();
            for (FactHandle handle: handlers) {
                kieSession.delete(handle);
            }
            patientStatusService.saveOne(patientStatus);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
