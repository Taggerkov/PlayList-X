package com.playlistx.model.utils;

/**
 * Custom file writing related {@link RuntimeException}.
 *
 * @author Sergiu Chirap
 * @version final
 * @see FileHandler
 * @since 0.1
 */
public class FileException extends RuntimeException {
    /**
     * Exception Constructor.
     * @param msg Message to attach.
     */
    public FileException(String msg) {
        super("Invalid File: " + msg);
    }
}