package bsep.hospital.dto;

import javax.validation.constraints.*;

public class CertificateRequestDTO {

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String commonName;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String surname;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String givenName;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String organization;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String organizationUnit;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String country;

    @Positive
    private int userId;

    @NotNull
    @NotBlank
    @Email
    @Pattern(regexp = "[^;]+")
    private String email;

    public CertificateRequestDTO() {
    }

    public CertificateRequestDTO(String commonName, String surname, String givenName, String organization, String organizationUnit, String country, int userId, String email) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.userId = userId;
        this.email = email;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
        return "CertificateRequestDTO{" +
                "commonName='" + commonName + '\'' +
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
