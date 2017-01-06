package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockShowDatabasesCommand extends MockCommand {
    
    @Override
    public String getCommandCode() {
        return "SHOW DB";
    }
    
    @Override
    public String getTestCommandLine() {
        return "SHOW DB";
    }

    @Override
    public String getOkResponseCode() {
        return "110";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "110 3 databases present",
            "gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "wn \"WordNet (r) 3.0 (2006)\"",
            "moby-thesaurus \"Moby Thesaurus II by Grady Ward, 1.0\"",
            ".",
            "250 ok"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "wn \"WordNet (r) 3.0 (2006)\"",
            "moby-thesaurus \"Moby Thesaurus II by Grady Ward, 1.0\""
        );
    }

}
