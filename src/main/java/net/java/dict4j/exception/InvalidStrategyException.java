package net.java.dict4j.exception;

@DictError(551)
public class InvalidStrategyException extends Exception {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 2730804546734902561L;
    
    public InvalidStrategyException() {
        super("Invalid strategy");
    }

}
