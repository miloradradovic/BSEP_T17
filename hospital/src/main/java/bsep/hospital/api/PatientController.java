package bsep.hospital.api;


import bsep.device.enums.MessageType;
import bsep.hospital.dto.PatientDTO;
import bsep.hospital.model.Patient;
import bsep.hospital.model.PatientStatus;
import bsep.hospital.service.PatientService;
import bsep.hospital.service.PatientStatusService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientController {

    @Autowired
    PatientService patientService;

    @Autowired
    PatientStatusService patientStatusService;

    private static Logger logger = LogManager.getLogger(PatientController.class);

    // @PreAuthorize("hasAuthority('DOCTOR')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        logger.info("User with the email " + accessToken.getEmail() + " is attempting to get all patients.");
        List<PatientDTO> patientDTOs = new ArrayList<>();
        List<Patient> patients = patientService.findAll();
        for (Patient patient : patients) {
            List<PatientStatus> patientStatuses = patientStatusService.findAllByPatientId(patient.getId());
            patientDTOs.add(new PatientDTO(patient.getId(), patient.getName(), patient.getSurname(), patient.getDateOfBirth(), patient.getBloodType().toString(), getAverageHearthBeat(patientStatuses), getAveragePressure(patientStatuses), getAverageTemperature(patientStatuses)));
        }
        logger.info("Successfully retrieved all patients.");
        return new ResponseEntity<>(patientDTOs, HttpStatus.OK);
    }

    private Double getAverageHearthBeat(List<PatientStatus> patientStatuses){
        List<PatientStatus> hearthBeats = patientStatuses.stream()
                .filter(p -> p.getType().equals(MessageType.HEARTH_BEAT)).collect(Collectors.toList());

        double avg = 0.0;
        for (PatientStatus ps: hearthBeats) {
            avg += ps.getValue();
        }
        return avg / hearthBeats.size();
    }

    private Double getAveragePressure(List<PatientStatus> patientStatuses){
        List<PatientStatus> pressures = patientStatuses.stream()
                .filter(p -> p.getType().equals(MessageType.PRESSURE)).collect(Collectors.toList());

        double avg = 0.0;
        for (PatientStatus ps: pressures) {
            avg += ps.getValue();
        }
        return avg / pressures.size();

    }

    private Double getAverageTemperature(List<PatientStatus> patientStatuses){
        List<PatientStatus> temperatures = patientStatuses.stream()
                .filter(p -> p.getType().equals(MessageType.TEMPERATURE)).collect(Collectors.toList());

        double avg = 0.0;
        for (PatientStatus ps: temperatures) {
            avg += ps.getValue();
        }
        return avg / temperatures.size();

    }


}
