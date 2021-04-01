package bsep.admin.exceptions;

public class IssuerNotCAException extends Exception {

    public IssuerNotCAException() {
        super("Issuer is not a CA");
    }
}
