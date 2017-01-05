package net.java.dict4j;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.java.dict4j.mock.MockSocketFacade;
import net.java.dict4j.server.ServerException;
import net.java.dict4j.server.ServerFacade;
import net.java.dict4j.server.ServerFacadeImpl;

public class ServerFacadeTest {

    private MockSocketFacade socketFacade;
    
    private ServerFacade facade;
    
    @Before
    public void setUp() {
        socketFacade = new MockSocketFacade();
        facade = new ServerFacadeImpl();
        ((ServerFacadeImpl) facade).setSocketFacade(socketFacade);
    }
    
    @Test
    public void statusCommandTest() throws ServerException, IOException {
        final List<String> response = facade.query("STATUS", "210");
        
        Assert.assertNotNull("returned list is null", response);
        Assert.assertTrue("response not empty", !response.isEmpty());
        
        Assert.assertEquals("response size invalid", 1, response.size());
        
        Assert.assertEquals("response value unexpected", "210 status [d/m/c = 2/12/58; 2.000r 0.000u 0.000s]", response.get(0));
    }
    
}
