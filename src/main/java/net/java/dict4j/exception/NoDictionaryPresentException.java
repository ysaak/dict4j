package net.java.dict4j.exception;

@DictError(554)
public class NoDictionaryPresentException extends Exception {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 5883134498974920065L;
    
    public NoDictionaryPresentException() {
        super("No dictionary present");
    }

}
