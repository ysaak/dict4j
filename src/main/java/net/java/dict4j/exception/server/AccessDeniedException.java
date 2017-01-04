package net.java.dict4j.exception.server;

import net.java.dict4j.exception.DictError;

@DictError(530)
public class AccessDeniedException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final  long serialVersionUID = 8464139294062625784L;
    
    public AccessDeniedException() {
        super("Access denied");
    }

}
