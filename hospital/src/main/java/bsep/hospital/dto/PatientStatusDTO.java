package bsep.hospital.dto;

import java.time.LocalDateTime;

public class PatientStatusDTO {

    private int id;

    private String patient;

    private String dateTime;

    private String type;

    private String message;

    private boolean alarm;

    public PatientStatusDTO() {
    }

    public PatientStatusDTO(String patient, String dateTime, String type, String message, boolean alarm) {
        this.patient = patient;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
        this.alarm = alarm;
    }

    public PatientStatusDTO(int id, String patient, String dateTime, String type, String message, boolean alarm) {
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
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
