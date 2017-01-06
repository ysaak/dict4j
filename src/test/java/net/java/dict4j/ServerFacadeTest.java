package net.java.dict4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.java.dict4j.data.Configuration;
import net.java.dict4j.server.ServerException;
import net.java.dict4j.server.ServerFacade;
import net.java.dict4j.server.ServerFacadeImpl;
import net.java.dict4j.socket.command.MockQuitCommand;
import net.java.dict4j.socket.command.MockCommand;
import net.java.dict4j.socket.command.MockConnectCommand;
import net.java.dict4j.socket.command.MockDefineCommand;
import net.java.dict4j.socket.command.MockMatchCommand;
import net.java.dict4j.socket.command.MockShowDatabasesCommand;
import net.java.dict4j.socket.command.MockShowInfoCommand;
import net.java.dict4j.socket.command.MockShowServerCommand;
import net.java.dict4j.socket.command.MockShowStrategiesCommand;
import net.java.dict4j.socket.command.MockStatusCommand;
import net.java.dict4j.socket.facade.MockSocketFacade;

public class ServerFacadeTest {
    
    private Logger logger = LoggerFactory.getLogger(ServerFacadeTest.class);

    private MockSocketFacade socketFacade;
    
    private ServerFacade facade;
    
    private List<MockCommand> commands;
    
    @Before
    public void setUp() {
        commands = Arrays.asList(
                new MockConnectCommand(),
                new MockQuitCommand(),
                new MockDefineCommand(),
                new MockMatchCommand(),
                new MockShowDatabasesCommand(),
                new MockShowInfoCommand(),
                new MockShowServerCommand(),
                new MockShowStrategiesCommand(),
                new MockStatusCommand()
        );
        
        socketFacade = new MockSocketFacade(commands);
        facade = new ServerFacadeImpl(new Configuration("fake.test.url"));
        ((ServerFacadeImpl) facade).setSocketFacade(socketFacade);
    }
    
    @Test
    public void testCommandParsing() throws ServerException, IOException {
        
        for (MockCommand cmd : commands) {
            
            if (!cmd.isFakeCommand()) {
                System.out.println("Executing command " + cmd.getTestCommandLine());
                logger.info("Executing command " + cmd.getTestCommandLine());
                
                runCommandTest(cmd.getTestCommandLine(), cmd.getOkResponseCode(), cmd.getExpectedResponse());
            }
        }
    }
    
    private void runCommandTest(String commandLine, String okResponse, List<String> expectedResponse) throws ServerException, IOException {
        final List<String> response = facade.execute(commandLine, okResponse);
        
        Assert.assertNotNull("returned list is null", response);

        // Compare response
        Assert.assertEquals("response size invalid", expectedResponse.size(), response.size());
        
        for (int i=0;i<expectedResponse.size(); i++) {
            Assert.assertEquals("response " + i + " is invalid", expectedResponse.get(i), response.get(i));
        }
    }
    
    @Test
    public void testGetBanner() throws ServerException, IOException {
        final MockConnectCommand connectCommand = new MockConnectCommand();
        final MockStatusCommand statusCommand = new MockStatusCommand();
        
        facade.execute(statusCommand.getTestCommandLine(), statusCommand.getOkResponseCode());
        
        Assert.assertNotNull("message id is null", facade.getMessageId());
        Assert.assertEquals("message id is invalid", connectCommand.getBanner(), facade.getServerBanner());
    }
    
    @Test
    public void testGetMessageId() throws ServerException, IOException {
        final MockConnectCommand connectCommand = new MockConnectCommand();
        final MockStatusCommand statusCommand = new MockStatusCommand();
        
        facade.execute(statusCommand.getTestCommandLine(), statusCommand.getOkResponseCode());
        
        Assert.assertNotNull("message id is null", facade.getMessageId());
        Assert.assertEquals("message id is invalid", connectCommand.getMessageId(), facade.getMessageId());
    }
    
    @Test
    public void testGetCapacitiesConnection() throws ServerException, IOException {
        final MockConnectCommand connectCommand = new MockConnectCommand();
        final MockStatusCommand statusCommand = new MockStatusCommand();
        
        facade.execute(statusCommand.getTestCommandLine(), statusCommand.getOkResponseCode());
        
        Assert.assertNotNull("capabilities set is null", facade.getCapabilities());
        Assert.assertEquals("capabilities count differs", connectCommand.getCapabilities().size(), facade.getCapabilities().size());
        Assert.assertTrue("capabilities values differs", connectCommand.getCapabilities().containsAll(facade.getCapabilities()));
    }
    
    @Test
    public void testIsConnectedConnection() throws ServerException, IOException {
        Assert.assertFalse("connection is already open a startup", facade.isConnectedToServer());
        
        final MockStatusCommand statusCommand = new MockStatusCommand();
        
        facade.execute(statusCommand.getTestCommandLine(), statusCommand.getOkResponseCode());
        
        Assert.assertTrue("connection is closed", facade.isConnectedToServer());
    }
    
    @Test
    public void testCloseConnection() throws ServerException, IOException {
        
        final MockStatusCommand statusCommand = new MockStatusCommand();
        facade.execute(statusCommand.getTestCommandLine(), statusCommand.getOkResponseCode());
        facade.close();
    }
}
