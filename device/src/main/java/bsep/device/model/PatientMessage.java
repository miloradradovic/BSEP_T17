package bsep.device.model;

import bsep.device.enums.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PatientMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer patientId;
    private LocalDateTime dateTime;
    private MessageType type;
    private String message;

    public PatientMessage() {
    }

    public PatientMessage(Integer patientId, LocalDateTime dateTime, MessageType type,  String message) {
        this.patientId = patientId;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
