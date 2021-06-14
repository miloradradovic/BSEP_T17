package bsep.hospital.service;

import bsep.hospital.model.PatientStatus;
import bsep.hospital.repository.PatientStatusRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientStatusService {

    @Autowired
    PatientStatusRepository patientStatusRepository;

    private static Logger logger = LogManager.getLogger(PatientStatusService.class);

    public List<PatientStatus> findAll() {
        logger.info("Getting all patient statuses.");
        return (List<PatientStatus>) patientStatusRepository.findAll();
    }

    public List<PatientStatus> findAllAlarms() {
        logger.info("Getting all patient alarms.");
        return patientStatusRepository.findAllByAlarm(true);
    }

    public List<PatientStatus> findAllByPatientId(Integer patientId) {
        logger.info("Getting all statuses by patient id " + patientId.toString());
        return patientStatusRepository.findAllByPatient_Id(patientId);
    }

    public List<PatientStatus> findAllByPatientNameAndSurname(String patientName, String patientSurname) {
        logger.info("Getting all statuses by patient for patient: " + patientName + " " + patientSurname);
        return patientStatusRepository.findAllByPatient_NameOrPatient_Surname(patientName, patientSurname);
    }

    public List<PatientStatus> findAllByAlarmAndPatientNameOrSurname(String patientName, String patientSurname) {
        logger.info("Getting all alarmed statuses for patient: " + patientName + " " + patientSurname);
        return patientStatusRepository.findAllByAlarmAndPatient_NameOrAlarmAndPatient_Surname(true, patientName, true, patientSurname);
    }

    public Page<PatientStatus> findAll(Pageable pageable) {
        logger.info("Getting all patient statuses paged.");
        return patientStatusRepository.findAll(pageable);
    }

    public PatientStatus findOne(Integer id) {
        logger.info("Getting patient status by id " + id.toString());
        return patientStatusRepository.findById(id).orElse(null);
    }

    public PatientStatus saveOne(PatientStatus patientStatus) {
        logger.info("Saving patient status " + patientStatus);
        return patientStatusRepository.save(patientStatus);
    }


}
