package bsep.hospital.service;

import bsep.hospital.model.PatientStatus;
import bsep.hospital.repository.PatientStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientStatusService {

    @Autowired
    PatientStatusRepository patientStatusRepository;

    public List<PatientStatus> findAll() {
        return (List<PatientStatus>) patientStatusRepository.findAll();
    }

    public List<PatientStatus> findAllAlarms() {
        return patientStatusRepository.findAllByAlarm(true);
    }

    public List<PatientStatus> findAllByPatientId(Integer patientId) {
        return patientStatusRepository.findAllByPatient_Id(patientId);
    }

    public Page<PatientStatus> findAll(Pageable pageable) {
        return patientStatusRepository.findAll(pageable);
    }

    public PatientStatus findOne(Integer id) {
        return patientStatusRepository.findById(id).orElse(null);
    }

    public PatientStatus saveOne(PatientStatus patientStatus) {
        return patientStatusRepository.save(patientStatus);
    }


}
