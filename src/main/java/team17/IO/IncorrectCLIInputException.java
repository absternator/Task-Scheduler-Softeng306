package team17.IO;

/**
 * This class is the custom checked exception for reading CLI input
 */
public class IncorrectCLIInputException extends IllegalArgumentException{
    public IncorrectCLIInputException(String errMsg) {
        super(errMsg);
    }
}
