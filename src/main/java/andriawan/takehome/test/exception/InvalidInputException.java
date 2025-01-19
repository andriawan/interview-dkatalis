package andriawan.takehome.test.exception;

public class InvalidInputException extends Exception {
    public InvalidInputException(String error) {
        super(error);
    }
}
