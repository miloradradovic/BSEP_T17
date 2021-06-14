package bsep.admin.model;

import org.hibernate.annotations.ColumnTransformer;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name="authorities")
public class Authority implements GrantedAuthority {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "name",
            read = "pgp_sym_decrypt(name::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    String name;

    @Override
    public String getAuthority() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
