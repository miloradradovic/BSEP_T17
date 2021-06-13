package bsep.admin.model;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "admins")
public class Admin implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "bytea", unique = true, nullable = false)
    @ColumnTransformer(forColumn = "email",
            read = "pgp_sym_decrypt(email::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String email;

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

    @Column(columnDefinition = "bytea", nullable = false)
    @ColumnTransformer(forColumn = "password",
            read = "pgp_sym_decrypt(password::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String password;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "verified",
            read = "pgp_sym_decrypt(verified::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private boolean verified;

    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private List<Authority> authorities;

    public Admin() {
        authorities = new ArrayList<>();
    }

    public Admin(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "Admin [id=" + id + ", email=" + email + ", name=" + name + ", surname=" + surname + ", password=" + password
                + ", verified=" + verified + ", lastPasswordResetDate=" + lastPasswordResetDate + ", authorities="
                + authorities + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Admin person = (Admin) o;
        return id == person.id &&
                Objects.equals(email, person.email) &&
                Objects.equals(password, person.password);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isVerified();
    }

}