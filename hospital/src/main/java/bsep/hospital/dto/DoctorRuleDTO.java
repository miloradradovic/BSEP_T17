package bsep.hospital.dto;

public class DoctorRuleDTO {

    private String ruleName;
    private String patient; // name surname -> id
    private String messageType; // enum
    private String bloodType; // enum
    private double value;
    private String operation; // == != <= >= < >

    public DoctorRuleDTO() {
    }

    public DoctorRuleDTO(String ruleName, String patient, String messageType, String bloodType, double value, String operation) {
        this.ruleName = ruleName;
        this.patient = patient;
        this.messageType = messageType;
        this.bloodType = bloodType;
        this.value = value;
        this.operation = operation;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
