package net.java.dict4j.socket.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.java.dict4j.data.ServerCapability;

public class MockConnectCommand extends MockCommand {

    @Override
    public String getCommandCode() {
        return "CONNECT";
    }
    
    @Override
    public String getTestCommandLine() {
        return "CONNECT";
    }

    @Override
    public String getOkResponseCode() {
        return "000";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "220 pan.alephnull.com dictd 1.12.1/rf on Linux 4.4.0-1-amd64 <auth.mime> <30801377.8901.1483649623@pan.alephnull.com>"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return new ArrayList<>();
    }
    
    @Override
    public boolean isFakeCommand() {
        return true;
    }
    
    public String getBanner() {
        return "pan.alephnull.com dictd 1.12.1/rf on Linux 4.4.0-1-amd64";
    }
    
    public String getMessageId() {
        return "30801377.8901.1483649623@pan.alephnull.com";
    }
    
    public Set<ServerCapability> getCapabilities() {
        return new HashSet<>(Arrays.asList(ServerCapability.BASIC_AUTHENTICATION, ServerCapability.OPTION_MIME));
    }
}
