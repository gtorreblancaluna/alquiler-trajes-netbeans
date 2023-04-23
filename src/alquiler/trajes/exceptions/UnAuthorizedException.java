package alquiler.trajes.exceptions;

public class UnAuthorizedException extends BusinessException {
    
    private transient Throwable cause;
    private transient String message;

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(final String message) {
        this.message = message;
    }

    public UnAuthorizedException(final String message, final Throwable cause) {
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
    

