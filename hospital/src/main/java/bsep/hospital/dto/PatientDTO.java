package bsep.hospital.dto;

import java.util.Date;

public class PatientDTO {

    private int id;

    private String name;

    private String surname;

    private Date dateOfBirth;

    private String bloodType;

    private Double averageHearthBeat;

    private Double averagePressure;

    private Double averageTemperature;

    public PatientDTO() {
    }

    public PatientDTO(String name, String surname, Date dateOfBirth, String bloodType) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
    }

    public PatientDTO(Integer id, String name, String surname, Date dateOfBirth, String bloodType, Double averageHearthBeat, Double averagePressure, Double averageTemperature) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.averageHearthBeat = averageHearthBeat;
        this.averagePressure = averagePressure;
        this.averageTemperature = averageTemperature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Double getAverageHearthBeat() {
        return averageHearthBeat;
    }

    public void setAverageHearthBeat(Double averageHearthBeat) {
        this.averageHearthBeat = averageHearthBeat;
    }

    public Double getAveragePressure() {
        return averagePressure;
    }

    public void setAveragePressure(Double averagePressure) {
        this.averagePressure = averagePressure;
    }

    public Double getAverageTemperature() {
        return averageTemperature;
    }

    public void setAverageTemperature(Double averageTemperature) {
        this.averageTemperature = averageTemperature;
    }
}
