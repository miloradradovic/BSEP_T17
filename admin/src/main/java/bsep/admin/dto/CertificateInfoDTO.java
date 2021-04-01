package bsep.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class CertificateInfoDTO {

    private String commonName;

    private String fullName;

    private String email;

    private Boolean revoked;

    private String revocationReason;


    private boolean isCA;

    private List<CertificateInfoDTO> children;


    public CertificateInfoDTO() {
        this.revoked = false;
        this.children = new ArrayList<>();
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public String getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }

    public boolean isCA() {
        return isCA;
    }

    public void setCA(boolean CA) {
        isCA = CA;
    }

    public List<CertificateInfoDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CertificateInfoDTO> children) {
        this.children = children;
    }
}
