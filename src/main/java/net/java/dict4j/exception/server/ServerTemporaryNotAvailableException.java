package net.java.dict4j.exception.server;

import net.java.dict4j.exception.DictError;

@DictError(420)
public class ServerTemporaryNotAvailableException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -5225252416379654810L;
    
    public ServerTemporaryNotAvailableException() {
        super("Server temporarily unavailable");
    }

}
