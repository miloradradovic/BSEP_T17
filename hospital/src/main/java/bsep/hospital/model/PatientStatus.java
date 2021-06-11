package bsep.hospital.model;

import bsep.device.enums.MessageType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_status")
public class PatientStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Patient patient;

    @Column(name = "dateTime", unique = false, nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(name = "message", unique = false, nullable = false)
    private String message;

    @Column(name = "alarm", unique = false, nullable = false)
    private boolean alarm = false;


    public PatientStatus() {
    }

    public PatientStatus(Patient patient, LocalDateTime dateTime, MessageType type, String message) {
        this.patient = patient;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
    }

    public PatientStatus(int id, Patient patient, LocalDateTime dateTime, MessageType type, String message) {
        this.id = id;
        this.patient = patient;
        this.dateTime = dateTime;
        this.type = type;
        this.message = message;
    }

    public double getValue(){
        int startIndex = message.indexOf("is: ") + 4;
        int endIndex = message.indexOf(".", message.indexOf(".") + 1);
        String sub = message.substring(startIndex, endIndex);
        return  Double.parseDouble(message.substring(startIndex, endIndex));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
