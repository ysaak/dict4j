package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockShowServerCommand extends MockCommand {
    
    @Override
    public String getCommandCode() {
        return "SHOW SERVER";
    }
    
    @Override
    public String getTestCommandLine() {
        return "SHOW SERVER";
    }

    @Override
    public String getOkResponseCode() {
        return "114";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "114 server information",
            "dictd 1.12.1/rf on Linux 4.4.0-1-amd64",
            "On pan.alephnull.com: up 305+14:53:43, 30803684 forks (4199.6/hour)",
            "",
            "Database      Headwords         Index          Data  Uncompressed",
            "gcide              203645       3859 kB         12 MB         38 MB",
            "wn                 147311       3002 kB       9247 kB         29 MB",
            "moby-thesaurus      30263        528 kB         10 MB         28 MB",
            "elements              142          2 kB         17 kB         53 kB",
            "",
            ".",
            "250 ok"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "dictd 1.12.1/rf on Linux 4.4.0-1-amd64",
            "On pan.alephnull.com: up 305+14:53:43, 30803684 forks (4199.6/hour)",
            "",
            "Database      Headwords         Index          Data  Uncompressed",
            "gcide              203645       3859 kB         12 MB         38 MB",
            "wn                 147311       3002 kB       9247 kB         29 MB",
            "moby-thesaurus      30263        528 kB         10 MB         28 MB",
            "elements              142          2 kB         17 kB         53 kB",
            ""
        );
    }

}
