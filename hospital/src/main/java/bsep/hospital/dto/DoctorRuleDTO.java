package bsep.hospital.dto;

public class DoctorRuleDTO {

    private String ruleName;
    private int patientId; // name surname -> id
    private String type; // enum
    private double value;
    private String operation; // == != <= >= < >

    public DoctorRuleDTO() {
    }

    public DoctorRuleDTO(String ruleName, int patientId, String type, double value, String operation) {
        this.ruleName = ruleName;
        this.patientId = patientId;
        this.type = type;
        this.value = value;
        this.operation = operation;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
