package net.java.dict4j.exception;

public class UnexpectedServerException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 6011350906142710631L;
    
    public UnexpectedServerException(Throwable cause) {
        super("Unkown error", cause);
    }

}
