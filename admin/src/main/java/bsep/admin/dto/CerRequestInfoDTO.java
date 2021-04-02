package bsep.admin.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CerRequestInfoDTO {

    @Positive
    private int id;

    @NotNull
    @NotBlank
    private String commonName;

    @NotNull
    @NotBlank
    private String surname;

    @NotNull
    @NotBlank
    private String givenName;

    @NotNull
    @NotBlank
    private String organization;

    @NotNull
    @NotBlank
    private String organizationUnit;

    @NotNull
    @NotBlank
    private String country;

    @NotNull
    @NotBlank
    private String userId;

    @NotNull
    @NotBlank
    private String email;

    public CerRequestInfoDTO() {

    }

    public CerRequestInfoDTO(int id, String commonName, String surname, String givenName, String organization, String organizationUnit, String country, String userId, String email) {
        this.id = id;
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.userId = userId;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "CerRequestInfoDTO{" +
                "id=" + id +
                ", commonName='" + commonName + '\'' +
                ", surname='" + surname + '\'' +
                ", givenName='" + givenName + '\'' +
                ", organization='" + organization + '\'' +
                ", organizationUnit='" + organizationUnit + '\'' +
                ", country='" + country + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
