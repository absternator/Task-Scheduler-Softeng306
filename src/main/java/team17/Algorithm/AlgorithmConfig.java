package team17.Algorithm;

/**
 * This class stores configuration information for the algorithm
 */
public class AlgorithmConfig {
    private static int _numOfProcessors;
    private static int _totalNodeWeight;

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
