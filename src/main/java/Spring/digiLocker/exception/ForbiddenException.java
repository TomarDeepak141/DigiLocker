package Spring.digiLocker.exception;

public class ForbiddenException
        extends RuntimeException {

    public ForbiddenException(
            String message
    ) {
        super(message);
    }
}
