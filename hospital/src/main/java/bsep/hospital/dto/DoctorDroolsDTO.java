package bsep.hospital.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DoctorDroolsDTO {

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String ruleName;

    @NotBlank
    @Min(0)
    private Integer patientId;

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String messageType;

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9_ ]+")
    private String bloodType;

    @NotBlank
    @Min(0)
    private double value;

    @NotBlank
    @Pattern(regexp="[<=>!]+")
    private String operation;

    public DoctorDroolsDTO() {
    }

    public DoctorDroolsDTO(String ruleName, Integer patientId, String messageType, String bloodType, double value, String operation) {
        this.ruleName = ruleName;
        this.patientId = patientId;
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

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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
