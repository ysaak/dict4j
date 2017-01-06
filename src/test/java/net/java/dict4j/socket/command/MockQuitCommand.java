package net.java.dict4j.socket.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockQuitCommand extends MockCommand {

    @Override
    public String getCommandCode() {
        return "QUIT";
    }
    
    @Override
    public String getTestCommandLine() {
        return "QUIT";
    }

    @Override
    public String getOkResponseCode() {
        return "221";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "221 bye [d/m/c = 0/0/0; 3.000r 0.000u 0.000s]"
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

}
