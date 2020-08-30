package team17.Algorithm;

import team17.DAG.Graph;

/**
 * This class stores configuration information for the algorithm
 */
public class AlgorithmConfig {
    private static int _numOfProcessors;
    private static int _totalNodeWeight;
    private static int _totalNodes;

    public static int get_totalNodes() {
        return _totalNodes;
    }

    public static void set_totalNodes(int totalNodes) {
       _totalNodes = totalNodes;
    }

    public static int getTotalNodeWeight() {
        return _totalNodeWeight;
    }

    public static void setTotalNodeWeight(int totalNodeWeight) {
        _totalNodeWeight = totalNodeWeight;
    }

    public static void setNumOfProcessors(int num){
        _numOfProcessors = num;
    }
    public static int getNumOfProcessors(){
        return _numOfProcessors;
    }
}
