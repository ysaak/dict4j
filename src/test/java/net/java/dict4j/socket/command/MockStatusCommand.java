package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockStatusCommand extends MockCommand {

    @Override
    public String getCommandCode() {
        return "STATUS";
    }
    
    @Override
    public String getTestCommandLine() {
        return "STATUS";
    }

    @Override
    public String getOkResponseCode() {
        return "210";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "210 status [d/m/c = 2/12/58; 3.000r 0.000u 0.000s]"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "210 status [d/m/c = 2/12/58; 3.000r 0.000u 0.000s]"
        );
    }

}
