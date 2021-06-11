package bsep.hospital.repository;

import bsep.hospital.model.Patient;
import bsep.hospital.model.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientStatusRepository extends JpaRepository<PatientStatus, Integer> {

    List<PatientStatus> findAllByAlarm(boolean alarm);
}
