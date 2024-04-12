package com.playlistx.utils.exceptions;

public class FileException extends RuntimeException {
    public FileException(String msg){
        super("Invalid File: " + msg);
    }
}
