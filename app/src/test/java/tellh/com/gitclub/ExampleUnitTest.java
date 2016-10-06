package tellh.com.gitclub;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tellh.com.gitclub.common.utils.StringUtils;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void testParseUrl() {
//        Matcher m = Pattern.compile("https://github\\.com/(?<user>.*?)/(((?<repo1>.*?)/.*)|(?<repo2>.*))")
//                .matcher("https://github.com/apple/swift-protobuf-plugin");
//                .matcher("https://github.com/nibnait/algorithms/tree/master/src/SwordOffer");
        String[] strings = StringUtils.parseGithubUrl("https://github.com/nibnait/algorithms/tree/master/src/SwordOffer");

    }

    @Test
    public void parseGithubUrl() {
        String rawUrl = "https://github.com/nibnait/algorithms/tree/master/src/SwordOffer";
        Matcher m = Pattern.compile("https://github\\.com/(.*?)/(?:(?:(.*?)/.*)|(.*))")
                .matcher(rawUrl);
        String[] result = new String[2];
        if (m.find()) {
            if (!isEmpty(m.group(1)))
                result[0] = m.group(1);
            if (!isEmpty(m.group(5)))
                result[1] = m.group(2);
            else if (!isEmpty(m.group(3)))
                result[1] = m.group(3);
        }
        assertEquals(result[0], "nibnait");
        assertEquals(result[1], "algorithms");
    }

    public boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
}