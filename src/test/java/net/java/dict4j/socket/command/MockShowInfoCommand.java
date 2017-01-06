package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockShowInfoCommand extends MockCommand {
    
    @Override
    public String getCommandCode() {
        return "SHOW INFO";
    }
    
    @Override
    public String getTestCommandLine() {
        return "SHOW INFO dict";
    }

    @Override
    public String getOkResponseCode() {
        return "112";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "112 information for gcide",
            "============ gcide ============",
            "00-database-info",
            "   This file was converted from the original database on:",
            "             Sun Oct 2 11:28:39 2011",
            "",
            "   The original data is available from:",
            "             ftp://ftp.gnu.org/gnu/gcide",
            "   (However, this archive does not always contain the most",
            "   recent version of the dictionary.)",
            "",
            "The original data was distributed with the notice shown below.",
            "No additional restrictions are claimed. Please redistribute this",
            "changed version under the same conditions and restriction that",
            "apply to the original version.",
            "",
            ".",
            "250 ok"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "============ gcide ============",
            "00-database-info",
            "   This file was converted from the original database on:",
            "             Sun Oct 2 11:28:39 2011",
            "",
            "   The original data is available from:",
            "             ftp://ftp.gnu.org/gnu/gcide",
            "   (However, this archive does not always contain the most",
            "   recent version of the dictionary.)",
            "",
            "The original data was distributed with the notice shown below.",
            "No additional restrictions are claimed. Please redistribute this",
            "changed version under the same conditions and restriction that",
            "apply to the original version.",
            ""
        );
    }

}
