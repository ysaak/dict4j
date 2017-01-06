package net.java.dict4j.server;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.java.dict4j.data.ServerCapability;

/**
 * A facade for a DICT server which handle open / close connection actions and commands execution 
 * 
 * @author Damien ROTH
 */
public interface ServerFacade extends Closeable {
    /**
     * Execute a command on the DICT server
     * @param command The command to execute
     * @param okResponse The success response code sent by the server
     * @return Lines sent by the server
     * @throws ServerException if an error is set by the server
     * @throws IOException if an socket error occurs
     */
    List<String> execute(String command, String okResponse) throws ServerException, IOException;
    
    /**
     * Returns the DICT server banner
     * @return the DICT server banner
     */
    String getServerBanner();
    
    /**
     * Returns the capabilities of the DICT server
     * @return the capabilities of the DICT server
     */
    Set<ServerCapability> getCapabilities();
    
    /**
     * Returns the unique message ID sent by the DICT server
     * @return the unique message ID sent by the DICT server
     */
    String getMessageId();
    
    /**
     * Determines if a connection to the DICT is active or not
     * @return {@code TRUE} if a connection to the DICT is active - {@code FALSE} otherwise
     */
    boolean isConnectedToServer();
}
