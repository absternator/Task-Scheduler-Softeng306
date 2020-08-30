import org.junit.Assume;
import org.junit.Test;

import java.io.IOException;

public class compareCategorisedGraphs8p {
    private String[] _args;
    private final int _timeout = 60000 * 5; // 5 minutes

    @Test(timeout = _timeout)
    public void test10pairs() throws IOException {
        _args = new String[]{"src/test/resources/10R10L-Pairs.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void test20Rsingles() throws IOException {
        _args = new String[]{"src/test/resources/20R-Singles.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testHarem() throws IOException {
        _args = new String[]{"src/test/resources/1R19L-Harem.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testReverseHarem() throws IOException {
        _args = new String[]{"src/test/resources/19R1L-ReverseHarem.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testDiamond() throws IOException {
        _args = new String[]{"src/test/resources/1R1L-HalfDiamond.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testString() throws IOException {
        _args = new String[]{"src/test/resources/1R1L-String.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testBinaryTree() throws IOException {
        _args = new String[]{"src/test/resources/1R10L-BinaryTree.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testPairs() throws IOException {
        _args = new String[]{"src/test/resources/5R5L-Pairs.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testTriangles() throws IOException {
        _args = new String[]{"src/test/resources/7R13L-Triangles.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testHalfReverseHarem() throws IOException {
        _args = new String[]{"src/test/resources/9R1L-HalfReverseHarem.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testReverseBinaryTree() throws IOException {
        _args = new String[]{"src/test/resources/10R1L-ReverseBinaryTree.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }

    @Test(timeout = _timeout)
    public void testReverseTrianglesTree() throws IOException {
        _args = new String[]{"src/test/resources/13R7L-ReverseTriangles.dot", "8"};
        CompareAlgorithms.compareAlgorithmsFor(_args);
    }
}
