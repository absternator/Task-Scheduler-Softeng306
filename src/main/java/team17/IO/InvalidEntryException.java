package team17.IO;

/**
 * This class is a custom checked exception for when an entry is not valid
 */
public class InvalidEntryException extends Exception {
    public InvalidEntryException(String errMsg) {
        super(errMsg);
    }
}
