package com.playlistx.utils.exceptions;

public class FXMLException extends CriticalException {

    public FXMLException() {
        super("Exception occurred while interacting with a FXML file!");
    }

    public FXMLException(String msg) {
        super("FXML Exception: " + msg);
    }
}
