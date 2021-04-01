package bsep.admin.exceptions;

public class AliasAlreadyExistsException extends Exception {

    public AliasAlreadyExistsException() {
        super("Alias already exists!");
    }
}
