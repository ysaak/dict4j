package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockShowStrategiesCommand extends MockCommand {
    
    @Override
    public String getCommandCode() {
        return "SHOW STRAT";
    }
    
    @Override
    public String getTestCommandLine() {
        return "SHOW STRAT";
    }

    @Override
    public String getOkResponseCode() {
        return "111";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "111 2 strategies present",
            "exact \"Match headwords exactly\"",
            "prefix \"Match prefixes\"",
            ".",
            "250 ok"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "exact \"Match headwords exactly\"",
            "prefix \"Match prefixes\""
        );
    }

}
