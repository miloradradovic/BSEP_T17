package bsep.admin.dto;

import org.bouncycastle.asn1.x509.KeyUsage;

import javax.validation.constraints.NotNull;

public class KeyUsageDTO {

    @NotNull
    private boolean cRLSign; // the subject public key is used for verifying signatures on certificate revocation lists

    @NotNull
    private boolean dataEncipherment; // he subject public key is used for directly enciphering raw user data without the
    // use of an intermediate symmetric cipher

    @NotNull
    private boolean decipherOnly; //is asserted and the keyAgreement bit is also set, the subject public key may be used only
    // for deciphering data while performing key agreement

    @NotNull
    private boolean digitalSignature; // the subject public key is used for verifying digital signatures, such as those used in an
    // entity authentication service, a data origin authentication service, and/or an integrity service

    @NotNull
    private boolean encipherOnly; //is asserted and the keyAgreement bit is also set, the subject public key may be used only for
    // enciphering data while performing key agreement

    @NotNull
    private boolean keyAgreement; // the subject public key is used for key agreement

    @NotNull
    private boolean keyCertSign;// the subject public key is used for verifying signatures on public key certificates

    @NotNull
    private boolean keyEncipherment; // the subject public key is used for enciphering private or secret keys, i.e., for key transport

    @NotNull
    private boolean nonRepudiation; // the subject public key is used to verify digital signatures, used to provide a non-repudiation
    // service that protects against the signing entity falsely denying some action

    public KeyUsageDTO() {

    }

    public KeyUsageDTO(boolean cRLSign, boolean dataEncipherment, boolean decipherOnly, boolean digitalSignature, boolean encipherOnly, boolean keyAgreement, boolean keyCertSign, boolean keyEncipherment, boolean nonRepudiation) {
        this.cRLSign = cRLSign;
        this.dataEncipherment = dataEncipherment;
        this.decipherOnly = decipherOnly;
        this.digitalSignature = digitalSignature;
        this.encipherOnly = encipherOnly;
        this.keyAgreement = keyAgreement;
        this.keyCertSign = keyCertSign;
        this.keyEncipherment = keyEncipherment;
        this.nonRepudiation = nonRepudiation;
    }

    public boolean iscRLSign() {
        return cRLSign;
    }

    public void setcRLSign(boolean cRLSign) {
        this.cRLSign = cRLSign;
    }

    public int getcRLSign() {
        if (this.cRLSign)
            return KeyUsage.cRLSign;
        else
            return 0;
    }

    public boolean isDataEncipherment() {
        return dataEncipherment;
    }

    public void setDataEncipherment(boolean dataEncipherment) {
        this.dataEncipherment = dataEncipherment;
    }

    public int getDataEncipherment() {
        if (this.dataEncipherment)
            return KeyUsage.dataEncipherment;
        else
            return 0;
    }

    public boolean isDecipherOnly() {
        return decipherOnly;
    }

    public void setDecipherOnly(boolean decipherOnly) {
        this.decipherOnly = decipherOnly;
    }

    public int getDecipherOnly() {
        if (this.decipherOnly)
            return KeyUsage.decipherOnly;
        else
            return 0;
    }

    public boolean isDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(boolean digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public int getDigitalSignature() {
        if (this.digitalSignature)
            return KeyUsage.digitalSignature;
        else
            return 0;
    }

    public boolean isEncipherOnly() {
        return encipherOnly;
    }

    public void setEncipherOnly(boolean encipherOnly) {
        this.encipherOnly = encipherOnly;
    }

    public int getEncipherOnly() {
        if (this.encipherOnly)
            return KeyUsage.encipherOnly;
        else
            return 0;
    }

    public boolean isKeyAgreement() {
        return keyAgreement;
    }

    public void setKeyAgreement(boolean keyAgreement) {
        this.keyAgreement = keyAgreement;
    }

    public int getKeyAgreement() {
        if (this.keyAgreement)
            return KeyUsage.keyAgreement;
        else
            return 0;
    }

    public boolean isKeyCertSign() {
        return keyCertSign;
    }

    public void setKeyCertSign(boolean keyCertSign) {
        this.keyCertSign = keyCertSign;
    }

    public int getKeyCertSign() {
        if (this.keyCertSign)
            return KeyUsage.keyCertSign;
        else
            return 0;
    }

    public boolean isKeyEncipherment() {
        return keyEncipherment;
    }

    public void setKeyEncipherment(boolean keyEncipherment) {
        this.keyEncipherment = keyEncipherment;
    }

    public int getKeyEncipherment() {
        if (this.keyEncipherment)
            return KeyUsage.keyEncipherment;
        else
            return 0;
    }

    public boolean isNonRepudiation() {
        return nonRepudiation;
    }

    public void setNonRepudiation(boolean nonRepudiation) {
        this.nonRepudiation = nonRepudiation;
    }

    public int getNonRepudiation() {
        if (this.nonRepudiation)
            return KeyUsage.nonRepudiation;
        else
            return 0;
    }
}
