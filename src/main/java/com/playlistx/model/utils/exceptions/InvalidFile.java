package com.playlistx.model.utils.exceptions;

public class InvalidFile extends RuntimeException {
    public InvalidFile(String msg){
        super("Invalid File: " + msg);
    }
}
