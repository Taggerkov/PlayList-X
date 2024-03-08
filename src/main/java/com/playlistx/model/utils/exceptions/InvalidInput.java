package com.playlistx.model.utils.exceptions;

public class InvalidInput extends RuntimeException {
    public InvalidInput(String msg) {
        super("Invalid Input: " + msg);
    }
}
