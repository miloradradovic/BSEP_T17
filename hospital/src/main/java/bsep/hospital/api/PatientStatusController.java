package bsep.hospital.api;


import bsep.hospital.dto.PatientStatusDTO;
import bsep.hospital.model.PatientStatus;
import bsep.hospital.service.PatientStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PatientStatusDTO>> getAllPatientStatus() {
        List<PatientStatusDTO> patientStatusDTOS = new ArrayList<>();
        List<PatientStatus> patientStatuses = patientStatusService.findAll();
        for (PatientStatus patientStatus : patientStatuses) {
            patientStatusDTOS.add(new PatientStatusDTO(
                    patientStatus.getId(),
                    patientStatus.getPatient().getName() + " " + patientStatus.getPatient().getSurname(),
                    patientStatus.getDateTime(),
                    patientStatus.getType().toString(),
                    patientStatus.getMessage(),
                    patientStatus.isAlarm()));
        }
        return new ResponseEntity<>(patientStatusDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET)
    public ResponseEntity<List<PatientStatusDTO>> getAllPatientStatusAlarmed() {
        List<PatientStatusDTO> patientStatusDTOS = new ArrayList<>();
        List<PatientStatus> patientStatuses = patientStatusService.findAllAlarms();
        for (PatientStatus patientStatus : patientStatuses) {
            patientStatusDTOS.add(new PatientStatusDTO(
                    patientStatus.getId(),
                    patientStatus.getPatient().getName() + " " + patientStatus.getPatient().getSurname(),
                    patientStatus.getDateTime(),
                    patientStatus.getType().toString(),
                    patientStatus.getMessage(),
                    patientStatus.isAlarm()));
        }
        return new ResponseEntity<>(patientStatusDTOS, HttpStatus.OK);
    }
}