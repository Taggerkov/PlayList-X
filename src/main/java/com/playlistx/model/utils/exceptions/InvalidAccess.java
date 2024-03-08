package com.playlistx.model.utils.exceptions;

public class InvalidAccess extends RuntimeException {
    public InvalidAccess(String msg) {
        super("Invalid Access: " + msg);
    }
}
