package com.playlistx.utils.exceptions;

public class InputException extends RuntimeException {
    public InputException(String msg) {
        super("Invalid Input: " + msg);
    }
}
