package bsep.hospital.service;

import bsep.hospital.model.Patient;
import bsep.hospital.repository.PatientRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;

    private static Logger logger = LogManager.getLogger(PatientService.class);

    public List<Patient> findAll() {
        logger.info("Getting all patients.");
        return patientRepository.findAll();
    }

    public Page<Patient> findAll(Pageable pageable) {
        logger.info("Getting all patients paged.");
        return patientRepository.findAll(pageable);
    }

    public Patient findOne(Integer id) {
        logger.info("Getting patient by id " + id.toString());
        return patientRepository.findById(id).orElse(null);
    }

}
