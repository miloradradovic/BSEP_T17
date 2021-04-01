package bsep.admin.dto;

public class CertificateCreationDTO {

    private int subjectID;
    private KeyUsageDTO keyUsageDTO;
    private ExtendedKeyUsageDTO extendedKeyUsageDTO;

    public  CertificateCreationDTO(){

    }

    public CertificateCreationDTO(int subjectID, KeyUsageDTO keyUsageDTO, ExtendedKeyUsageDTO extendedKeyUsageDTO) {
        this.subjectID = subjectID;
        this.keyUsageDTO = keyUsageDTO;
        this.extendedKeyUsageDTO = extendedKeyUsageDTO;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public KeyUsageDTO getKeyUsageDTO() {
        return keyUsageDTO;
    }

    public void setKeyUsageDTO(KeyUsageDTO keyUsageDTO) {
        this.keyUsageDTO = keyUsageDTO;
    }

    public ExtendedKeyUsageDTO getExtendedKeyUsageDTO() {
        return extendedKeyUsageDTO;
    }

    public void setExtendedKeyUsageDTO(ExtendedKeyUsageDTO extendedKeyUsageDTO) {
        this.extendedKeyUsageDTO = extendedKeyUsageDTO;
    }
}
