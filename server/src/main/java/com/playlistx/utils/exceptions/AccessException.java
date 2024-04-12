package com.playlistx.utils.exceptions;

public class AccessException extends RuntimeException {
    public AccessException(String msg) {
        super("Invalid Access: " + msg);
    }
}
