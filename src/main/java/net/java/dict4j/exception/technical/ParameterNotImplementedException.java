package net.java.dict4j.exception.technical;

import net.java.dict4j.exception.DictError;

@DictError(503)
public class ParameterNotImplementedException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 461027151587333270L;
    
    public ParameterNotImplementedException() {
        super("Command parameter not implemented");
    }

}
