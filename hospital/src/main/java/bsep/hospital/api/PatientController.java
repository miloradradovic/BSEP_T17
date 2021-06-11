package bsep.hospital.api;


import bsep.hospital.dto.PatientDTO;
import bsep.hospital.model.Patient;
import bsep.hospital.service.PatientService;
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
@RequestMapping(value = "/patient", produces = MediaType.APPLICATION_JSON_VALUE)
public class PatientController {

    @Autowired
    PatientService patientService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patientDTOs = new ArrayList<>();
        List<Patient> patients = patientService.findAll();
        for (Patient patient : patients) {
            patientDTOs.add(new PatientDTO(patient.getName(), patient.getSurname(), patient.getDateOfBirth(), patient.getBloodType().toString()));
        }
        return new ResponseEntity<>(patientDTOs, HttpStatus.OK);
    }


}
