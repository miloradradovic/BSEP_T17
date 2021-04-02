package bsep.admin.model;

import javax.persistence.*;

@Entity
@Table(name = "requests")
public class CerRequestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "commonName", unique = false, nullable = false)
    private String commonName;

    @Column(name = "surname", unique = false, nullable = false)
    private String surname;

    @Column(name = "givenName", unique = false, nullable = false)
    private String givenName;

    @Column(name = "organization", unique = false, nullable = false)
    private String organization;

    @Column(name = "organizationUnit", unique = false, nullable = false)
    private String organizationUnit;

    @Column(name = "country", unique = false, nullable = false)
    private String country;

    @Column(name = "userId", unique = false, nullable = false)
    private int userId;

    @Column(name = "email", unique = false, nullable = false)
    private String email;

    @Column(name = "verified", unique = false, nullable = false)
    private boolean verified;

    public CerRequestInfo() {

    }

    public CerRequestInfo(int id, String commonName, String surname, String givenName, String organization, String organizationUnit, String country, int userId) {
        this.id = id;
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.userId = userId;
    }

    public CerRequestInfo(int id, String commonName, String surname, String givenName, String organization, String organizationUnit, String country, int userId, String email, boolean verified) {
        this.id = id;
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.userId = userId;
        this.email = email;
        this.verified = verified;
    }

    public CerRequestInfo(String commonName, String surname, String givenName, String organization, String organizationUnit, String country, int userId, String email, boolean verified) {
        this.commonName = commonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.userId = userId;
        this.email = email;
        this.verified = verified;
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

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
