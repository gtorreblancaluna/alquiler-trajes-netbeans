package alquiler.trajes.exceptions;

public class NoDataFoundException extends BusinessException {
    
    private transient Throwable cause;
    private transient String message;

    public NoDataFoundException() {
        super();
    }

    public NoDataFoundException(final String message) {
        this.message = message;
    }

    public NoDataFoundException(final String message, final Throwable cause) {
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
    

