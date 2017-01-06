package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockMatchCommand extends MockCommand {

    @Override
    public String getCommandCode() {
        return "MATCH";
    }

    @Override
    public String getTestCommandLine() {
        return "MATCH dict strategy word";
    }

    @Override
    public String getOkResponseCode() {
        return "152";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "152 4 matches found",
            "gcide \"Orange\"",
            "gcide \"orange\"",
            "gcide \"Orange bird\"",
            "gcide \"Orange cowry\"",
            ".",
            "250 ok [d/m/c = 0/12/34; 0.000r 0.000u 0.000s]"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "gcide \"Orange\"",
            "gcide \"orange\"",
            "gcide \"Orange bird\"",
            "gcide \"Orange cowry\""
        );
    }

}
