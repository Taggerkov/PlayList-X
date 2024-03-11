package com.playlistx.model.login;

public class LoginException extends RuntimeException {
    public LoginException(){
    }
    public LoginException(String msg){
        super(msg);
    }
}
