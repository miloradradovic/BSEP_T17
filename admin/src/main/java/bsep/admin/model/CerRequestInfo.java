package bsep.admin.model;

import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;

@Entity
@Table(name = "requests")
public class CerRequestInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "common_name",
            read = "pgp_sym_decrypt(common_name::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String commonName;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "surname",
            read = "pgp_sym_decrypt(surname::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String surname;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "given_name",
            read = "pgp_sym_decrypt(given_name::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String givenName;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "organization",
            read = "pgp_sym_decrypt(organization::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String organization;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "organization_unit",
            read = "pgp_sym_decrypt(organization_unit::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String organizationUnit;


    @Column(nullable = false)
    @ColumnTransformer(forColumn = "country",
            read = "pgp_sym_decrypt(country::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String country;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "user_id",
            read = "pgp_sym_decrypt(user_id::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private int userId;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "email",
            read = "pgp_sym_decrypt(email::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String email;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "verified",
            read = "pgp_sym_decrypt(verified::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
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
