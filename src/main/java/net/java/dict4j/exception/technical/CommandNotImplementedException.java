package net.java.dict4j.exception.technical;

import net.java.dict4j.exception.DictError;

@DictError(502)
public class CommandNotImplementedException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = 7699049748464883952L;
    
    public CommandNotImplementedException() {
        super("Command not implemented");
    }

}
