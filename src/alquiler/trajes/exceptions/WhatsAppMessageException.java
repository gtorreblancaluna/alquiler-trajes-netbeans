package alquiler.trajes.exceptions;



public class WhatsAppMessageException extends BusinessException{
    
    private transient Throwable cause;
    private transient String message;

    public WhatsAppMessageException() {
        super();
    }

    public WhatsAppMessageException(final String message) {
        this.message = message;
    }

    public WhatsAppMessageException(final String message, final Throwable cause) {
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
    

