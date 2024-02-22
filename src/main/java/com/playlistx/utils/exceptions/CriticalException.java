package com.playlistx.utils.exceptions;

class CriticalException extends RuntimeException {
    protected CriticalException(String msg) {
        super("Critical exception encountered while Runtime! Exiting system... \n" + msg);
        System.exit(0);
    }
}
