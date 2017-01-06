package net.java.dict4j.socket.facade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import net.java.dict4j.socket.SocketFacade;
import net.java.dict4j.socket.command.MockCommand;

public class MockSocketFacade implements SocketFacade {
    private boolean socketFailure = false;
    private boolean dataFailure = false;
    
    private Iterator<String> commandData = null;
    
    private final Map<String, MockCommand> commands;
    
    public MockSocketFacade(List<MockCommand> commands) {
        this.commands = new HashMap<>();
        
        for (MockCommand cmd : commands) {
            this.commands.put(cmd.getCommandCode(), cmd);
        }
    }

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
        
        final String[] parts = commandLine.split(" ");
        
        final String command; 
        if ("SHOW".equals(parts[0])) {
            command = parts[0] + " " + parts[1];
        }
        else {
            command = parts[0];
        }
        
        MockCommand mockCommand = commands.get(command);
        if (mockCommand == null) {
            Assert.fail("Unknown command '" + parts[0] + "'");
        }
        
        if (dataFailure) {
            commandData = mockCommand.getErrorSocketResponse().iterator();
        }
        else {
            commandData = mockCommand.getSocketResponse().iterator();
        }
    }
}
