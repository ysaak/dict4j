package net.java.dict4j.exception.server;

import net.java.dict4j.exception.DictError;

@DictError(421)
public class ServerShuttingDownException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -5077157335374610328L;
    
    public ServerShuttingDownException() {
        super("Server shutting down at operator request");
    }

}
