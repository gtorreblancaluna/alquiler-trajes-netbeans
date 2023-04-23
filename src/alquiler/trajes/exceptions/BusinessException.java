package alquiler.trajes.exceptions;

public class BusinessException extends Exception{
    
    private transient Throwable cause;
    private transient String message;

    public BusinessException() {
        super();
    }

    public BusinessException(final String message) {
        this.message = message;
    }

    public BusinessException(final String message, final Throwable cause) {
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
    

