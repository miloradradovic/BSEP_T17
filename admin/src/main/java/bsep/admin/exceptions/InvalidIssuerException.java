package bsep.admin.exceptions;

public class InvalidIssuerException extends Exception {

    public InvalidIssuerException() {
        super("Issuer is not valid");
    }
}
