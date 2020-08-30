package team17.DAG;

/**
 * This class is the custom checked exception for when graph is invalid
 */
public class InvalidGraphException extends Exception {
    public InvalidGraphException(String errMsg) {
        super(errMsg);
    }
}
