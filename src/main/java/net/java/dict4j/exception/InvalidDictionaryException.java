package net.java.dict4j.exception;

@DictError(550)
public class InvalidDictionaryException extends Exception {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -5497650013718590117L;
    
    public InvalidDictionaryException() {
        super("Invalid database");
    }

}
