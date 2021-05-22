package bsep.admin.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class RevokeCertificateDTO {

    @NotNull
    @NotBlank
    @Email
    @Pattern(regexp = "[^;]+")
    private String subjectAlias;

    @NotNull
    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9_ ]+")
    private String revocationReason;

    public RevokeCertificateDTO() {
    }

    public RevokeCertificateDTO(String subjectAlias, String revocationReason) {
        this.subjectAlias = subjectAlias;
        this.revocationReason = revocationReason;
    }

    public String getSubjectAlias() {
        return subjectAlias;
    }

    public void setSubjectAlias(String subjectAlias) {
        this.subjectAlias = subjectAlias;
    }

    public String getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }
}
