package bsep.hospital.model;

import bsep.device.enums.MessageType;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_status")
public class PatientStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @ColumnTransformer(forColumn = "patient_id",
            read = "pgp_sym_decrypt(patient_id::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private Patient patient;

    @Column( nullable = false)
    @ColumnTransformer(forColumn = "date_time",
            read = "pgp_sym_decrypt(date_time::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(forColumn = "type",
            read = "pgp_sym_decrypt(type::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private MessageType type;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "message",
            read = "pgp_sym_decrypt(message::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String message;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "alarm",
            read = "pgp_sym_decrypt(alarm::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
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
