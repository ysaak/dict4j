package net.java.dict4j.socket.command;

import java.util.Arrays;
import java.util.List;

public class MockDefineCommand extends MockCommand {

    @Override
    public String getCommandCode() {
        return "DEFINE";
    }
    
    @Override
    public String getTestCommandLine() {
        return "DEFINE dict word";
    }

    @Override
    public String getOkResponseCode() {
        return "150";
    }

    @Override
    public List<String> getSocketResponse() {
        return Arrays.asList(
            "150 2 definitions retrieved",
            "151 \"Orange\" gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "Orange \\Or\"ange\\ ([o^]r\"[e^]nj), n. [F.; cf. It. arancia,",
            "   arancio, LL. arangia, Sp. naranjia, Pg. laranja; all fr. Ar.",
            "   n[=a]ranj, Per. n[=a]ranj, n[=a]rang; cf. Skr. n[=a]ranga",
            "   orange tree. The o- in F. orange is due to confusion with or",
            "   gold, L. aurum, because the orange resembles gold in color.]",
            "   [1913 Webster]",
            ".",
            "151 \"orange\" gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "orange \\or\\\"ange\\, a.",
            "   Of or pertaining to an orange; of the color of an orange;",
            "   reddish yellow; as, an orange ribbon.",
            "   [1913 Webster]",
            ".",
            "250 ok [d/m/c = 2/0/17; 0.000r 0.000u 0.000s]"
        );
    }

    @Override
    public List<String> getExpectedResponse() {
        return Arrays.asList(
            "151 \"Orange\" gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "Orange \\Or\"ange\\ ([o^]r\"[e^]nj), n. [F.; cf. It. arancia,",
            "   arancio, LL. arangia, Sp. naranjia, Pg. laranja; all fr. Ar.",
            "   n[=a]ranj, Per. n[=a]ranj, n[=a]rang; cf. Skr. n[=a]ranga",
            "   orange tree. The o- in F. orange is due to confusion with or",
            "   gold, L. aurum, because the orange resembles gold in color.]",
            "   [1913 Webster]",
            "151 \"orange\" gcide \"The Collaborative International Dictionary of English v.0.48\"",
            "orange \\or\\\"ange\\, a.",
            "   Of or pertaining to an orange; of the color of an orange;",
            "   reddish yellow; as, an orange ribbon.",
            "   [1913 Webster]"
        );
    }

}
