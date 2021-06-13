package bsep.admin.model;


import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;

@Entity
@Table(name = "revocation_requests")
public class CerRevocationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    @ColumnTransformer(forColumn = "request",
            read = "pgp_sym_decrypt(request::bytea, 'tri-musketara-123')",
            write = "pgp_sym_encrypt(?, 'tri-musketara-123')")
    private String request;

    public CerRevocationRequest() {
    }

    public CerRevocationRequest(String request) {
        this.request = request;
    }

    public CerRevocationRequest(int id, String request) {
        this.id = id;
        this.request = request;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
