/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alquiler.trajes.exceptions;



/**
 *
 * @author gerardo torreblanca luna
 * handle exception in layer dao
 */
public class JasperPrintUtilException extends BusinessException{
    
    private transient Throwable cause;
    private transient String message;

    public JasperPrintUtilException() {
        super();
    }

    public JasperPrintUtilException(final String message) {
        this.message = message;
    }

    public JasperPrintUtilException(final String message, final Throwable cause) {
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
    

