package net.java.dict4j.exception.technical;

import net.java.dict4j.exception.DictError;

@DictError(500)
public class SyntaxErrorCommandException extends RuntimeException {

    /**
     * Generated serial ID 
     */
    private static final long serialVersionUID = -3978590796949626092L;
    
    public SyntaxErrorCommandException() {
        super("Syntax error, command not recognized");
    }

}
