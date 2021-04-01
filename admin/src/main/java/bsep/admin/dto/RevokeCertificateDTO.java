package bsep.admin.dto;

public class RevokeCertificateDTO {
    private String serialNumber;
    private String revocationReason;

    public RevokeCertificateDTO() {
    }

    public RevokeCertificateDTO(String serialNumber, String revocationReason) {
        this.serialNumber = serialNumber;
        this.revocationReason = revocationReason;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getRevocationReason() {
        return revocationReason;
    }

    public void setRevocationReason(String revocationReason) {
        this.revocationReason = revocationReason;
    }
}
