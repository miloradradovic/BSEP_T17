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

    @Column(name = "name", columnDefinition = "bytea", unique = false, nullable = false)
    /*@ColumnTransformer(
            read = "pgp_sym_decrypt(name::bytea, 'AES_KEY'::text)",
            write = "pgp_sym_encrypt(?, 'AES_KEY'::text)")*/
    private String name;

    @Column(name = "surname", unique = false, nullable = false)
    private String surname;

    @Column(name = "dateOfBirth", unique = false, nullable = false)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
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
