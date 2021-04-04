package bsep.admin.exceptions;

public class CertificateSendFailException extends Exception {

    public CertificateSendFailException() {
        super("Could not send certificate!");
    }
}
