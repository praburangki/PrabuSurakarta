package Applet;

public class NoMoreMovesException extends Exception {

    public NoMoreMovesException() {}

    public NoMoreMovesException(String message) {
        super(message);
    }
}