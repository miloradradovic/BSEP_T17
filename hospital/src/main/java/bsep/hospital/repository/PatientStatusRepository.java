package bsep.hospital.repository;

import bsep.hospital.model.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientStatusRepository extends JpaRepository<PatientStatus, Integer> {

    List<PatientStatus> findAllByAlarm(boolean alarm);

    List<PatientStatus> findAllByPatient_Id(int patientId);

    List<PatientStatus> findAllByPatient_NameOrPatient_Surname(String patientName, String patientSurname);

    List<PatientStatus> findAllByAlarmAndPatient_NameOrAlarmAndPatient_Surname(boolean alarm, String patientName, boolean alarm1, String patientSurname);
}
