package net.java.dict4j.server;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public interface ServerFacade extends Closeable {
    
    void initialize(String host, int port);
    
    /**
     * Establish a connection to the DICT server
     * @throws ServerException
     * @return TRUE if the connection is open - throw DictException otherwise
     */
    void connect() throws ServerException, IOException;
    
    List<String> query(String query, String okResponse) throws ServerException, IOException;
    
    String getAuthMsgId();
}
