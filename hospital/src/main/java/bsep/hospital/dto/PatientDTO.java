package bsep.hospital.dto;

import java.util.Date;

public class PatientDTO {

    private String name;

    private String surname;

    private Date dateOfBirth;

    private String bloodType;

    public PatientDTO() {
    }

    public PatientDTO(String name, String surname, Date dateOfBirth, String bloodType) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
}
