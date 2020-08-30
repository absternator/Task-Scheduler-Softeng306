import org.junit.Assume;
import org.junit.Test;

import java.io.IOException;

public class compareCategorisedGraphs8p {
    private String[] _args;
    private final int _timeout = 60000 * 2; // 2 minutes

    @Test(timeout = _timeout)
    public void test10pairs () throws IOException {
        _args = new String[]{"src/test/resources/10R10L-Pairs.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void test20Rsingles () throws IOException {
        _args = new String[]{"src/test/resources/20R-Singles.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void testHarem () throws IOException {
        _args = new String[]{"src/test/resources/1R19L-Harem.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void testReverseHarem () throws IOException {
        _args = new String[]{"src/test/resources/19R1L-ReverseHarem.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void testDiamond () throws IOException {
        _args = new String[]{"src/test/resources/1R1L-Diamond.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test (timeout = _timeout)
    public void testString () throws IOException {
        _args = new String[]{"src/test/resources/1R1L-String.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }
}
