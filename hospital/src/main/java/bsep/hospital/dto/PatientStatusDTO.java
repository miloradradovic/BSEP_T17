package bsep.hospital.dto;

import java.time.LocalDateTime;

public class PatientStatusDTO {

    private int id;

    private String patient;

    private LocalDateTime dateTime;

    private String type;

    private String message;

    private boolean alarm;

    public PatientStatusDTO() {
    }

    public PatientStatusDTO(String patient, LocalDateTime dateTime, String type, String message, boolean alarm) {
        this.patient = patient;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
        this.alarm = alarm;
    }

    public PatientStatusDTO(int id, String patient, LocalDateTime dateTime, String type, String message, boolean alarm) {
        this.id = id;
        this.patient = patient;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
        this.alarm = alarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
