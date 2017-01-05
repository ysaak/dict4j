package net.java.dict4j.exception;

@DictError(555)
public class NoStrategyPresentException extends Exception {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 3965119781400478959L;
    
    public NoStrategyPresentException() {
        super("No strategies present");
    }

}
