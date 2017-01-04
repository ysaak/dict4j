package net.java.dict4j.socket;

import java.io.Closeable;
import java.io.IOException;

public interface SocketFacade extends Closeable {
    
    void connect(String host, int port, int timeout) throws IOException;
    
    String readLine() throws IOException;
    
    void writeLine(String line) throws IOException;

}
