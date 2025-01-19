package andriawan.takehome.test.exception;

public class CommandTerminateException extends RuntimeException {
    public CommandTerminateException() {
        super("end of normal executed command");
    }
}
