package net.java.dict4j.mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import net.java.dict4j.socket.SocketFacade;

public class MockSocketFacade implements SocketFacade {
    
    private static final Map<String, List<String>> SUCCESS_COMMAND_RESPONSES = new HashMap<>();
    private static final Map<String, List<String>> FAIL_COMMAND_RESPONSES = new HashMap<>();
    static {
        initSuccessResponses();
        initFailResponses();
    }
    
    private boolean socketFailure = false;
    private boolean dataFailure = false;
    
    private Iterator<String> commandData = null;

    public void setSocketFailure(boolean socketFailure) {
        this.socketFailure = socketFailure;
    }
    
    public void setDataFailure(boolean dataFailure) {
        this.dataFailure = dataFailure;
    }
    
    @Override
    public void close() throws IOException {
        if (socketFailure) {
            throw new IOException("mock IOException");
        }
    }

    @Override
    public void connect(String host, int port, int timeout) throws IOException {
        if (socketFailure) {
            throw new IOException("mock IOException");
        }
        
        setCommandData("CONNECT");
    }

    @Override
    public String readLine() throws IOException {
        if (socketFailure) {
            throw new IOException("mock IOException");
        }
        
        if (commandData != null && commandData.hasNext()) {
            return commandData.next();
        }

        return null;
    }

    @Override
    public void writeLine(String line) throws IOException {
        if (socketFailure) {
            throw new IOException("mock IOException");
        }
        
        setCommandData(line);
    }
    
    private void setCommandData(String commandLine) {
        Assert.assertNotNull("line sent to the server is null", commandLine);
        
        String[] parts = commandLine.split(" ");
        
        
        final List<String> data = dataFailure ? FAIL_COMMAND_RESPONSES.get(parts[0]) : SUCCESS_COMMAND_RESPONSES.get(parts[0]) ;
        
        if (data == null) {
            Assert.fail("Unknown command '" + parts[0] + "'");
        }
        
        commandData = data.iterator();
    }
    
    
    /* ----- Commands responses ----- */


    private static void initSuccessResponses() {
        
        // CONNECT command
        SUCCESS_COMMAND_RESPONSES.put("CONNECT", Arrays.asList("220 pan.alephnull.com dictd 1.12.1/rf on Linux 4.4.0-1-amd64 <auth.mime> <30801377.8901.1483649623@pan.alephnull.com>"));

        // CLIENT command
        SUCCESS_COMMAND_RESPONSES.put("CLIENT", Arrays.asList("250 ok"));
        
        // STATUS command
        SUCCESS_COMMAND_RESPONSES.put("STATUS", Arrays.asList("210 status [d/m/c = 2/12/58; 2.000r 0.000u 0.000s]"));
        
        // CLOSE command
        SUCCESS_COMMAND_RESPONSES.put("CLOSE", Arrays.asList("221 bye [d/m/c = 0/0/0; 3.000r 0.000u 0.000s]"));
        
        // TODO Auto-generated method stub
        
    }

    private static void initFailResponses() {
        // TODO Auto-generated method stub
        
    }
}
