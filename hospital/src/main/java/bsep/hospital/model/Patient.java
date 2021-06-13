package bsep.hospital.model;

import bsep.hospital.enums.BloodType;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "bytea", nullable = false)
    @ColumnTransformer(forColumn = "name",
            read = "pgp_sym_decrypt(name::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String name;


    @Column(columnDefinition = "bytea", nullable = false)
    @ColumnTransformer(forColumn = "surname",
            read = "pgp_sym_decrypt(surname::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String surname;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "date_of_birth",
            read = "pgp_sym_decrypt(date_of_birth::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    @ColumnTransformer(forColumn = "blood_type",
            read = "pgp_sym_decrypt(blood_type::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private BloodType bloodType;

    public Patient() {
    }

    public Patient(int id, String name, String surname, Date dateOfBirth, BloodType bloodType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
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

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}
