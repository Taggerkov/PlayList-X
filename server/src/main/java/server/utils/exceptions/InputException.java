package server.utils.exceptions;

public class InputException extends RuntimeException {
    public InputException(String msg) {
        super("Invalid Input: " + msg);
    }
}
