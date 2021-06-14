package bsep.hospital.api;

import java.time.format.DateTimeFormatter;

import bsep.hospital.dto.PatientStatusDTO;
import bsep.hospital.model.PatientStatus;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/patient-status", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientStatusController {

    @Autowired
    PatientStatusService patientStatusService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private static Logger logger = LogManager.getLogger(PatientStatusController.class);

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PatientStatusDTO>> getAllPatientStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        logger.info("User with the email " + accessToken.getEmail() + " is attempting to get all patient statuses.");
        List<PatientStatusDTO> patientStatusDTOS = new ArrayList<>();
        List<PatientStatus> patientStatuses = patientStatusService.findAll();
        for (PatientStatus patientStatus : patientStatuses) {
            patientStatusDTOS.add(new PatientStatusDTO(
                    patientStatus.getId(),
                    patientStatus.getPatient().getName() + " " + patientStatus.getPatient().getSurname(),
                    patientStatus.getDateTime().format(formatter),
                    patientStatus.getType().toString(),
                    patientStatus.getMessage(),
                    patientStatus.isAlarm()));
        }
        logger.info("Successfully retrieved all patient statuses");
        return new ResponseEntity<>(patientStatusDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public ResponseEntity<List<PatientStatusDTO>> getAllPatientStatusAlarmed() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        logger.info("User with the email " + accessToken.getEmail() + " is attempting to get all patient alarms.");
        List<PatientStatusDTO> patientStatusDTOS = new ArrayList<>();
        List<PatientStatus> patientStatuses = patientStatusService.findAllAlarms();
        for (PatientStatus patientStatus : patientStatuses) {
            patientStatusDTOS.add(new PatientStatusDTO(
                    patientStatus.getId(),
                    patientStatus.getPatient().getName() + " " + patientStatus.getPatient().getSurname(),
                    patientStatus.getDateTime().format(formatter),
                    patientStatus.getType().toString(),
                    patientStatus.getMessage(),
                    patientStatus.isAlarm()));
        }
        logger.info("Successfully retrieved all patient alarms.");
        return new ResponseEntity<>(patientStatusDTOS, HttpStatus.OK);
    }
}
