package net.java.dict4j.exception.technical;

import net.java.dict4j.exception.DictError;

@DictError(501)
public class SyntaxErrorParametersException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -3978590796949626092L;
    
    public SyntaxErrorParametersException() {
        super("Syntax error, illegal parameters");
    }

}
