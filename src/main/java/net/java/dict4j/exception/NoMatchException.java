package net.java.dict4j.exception;

@DictError(552)
public class NoMatchException extends Exception {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -2033077762689658348L;
    
    public NoMatchException() {
        super("No match");
    }

}
