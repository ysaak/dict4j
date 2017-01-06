package net.java.dict4j.socket.command;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock command interface
 * 
 * @author Damien ROTH
 */
public abstract class MockCommand {
    
    /**
     * Returns the command code
     * @return the command code
     */
    public abstract String getCommandCode();
    
    /**
     * Returns the command line to run
     * @return the command line to run
     */
    public abstract String getTestCommandLine();
    
    /**
     * Returns the OK response code
     * @return the OK response code
     */
    public abstract String getOkResponseCode();
    
    /**
     * Returns the raw lines sent by the DICT server 
     * @return the raw lines sent by the DICT server
     */
    public abstract List<String> getSocketResponse();
    
    /**
     * Returns the expected parsed response
     * @return the expected parsed response
     */
    public abstract List<String> getExpectedResponse();
    
    /**
     * Returns the raw error lines sent by the DICT server 
     * @return the raw error lines sent by the DICT server
     */
    public List<String> getErrorSocketResponse() {
        return new ArrayList<>();
    }
    
    /**
     * Returns the expected parsed error response 
     * @return the expected parsed error response
     */
    public List<String> getExpectedErrorSocketResponse() {
        return new ArrayList<>();
    }
    
    /**
     * Determines if this command is a fake (use for the connect event)
     * @return {@code TRUE} if this command is a fake - {@code FALSE} otherwise
     */
    public boolean isFakeCommand() {
        return false;
    }
}
