package com.playlistx.model.login;

/**
 * The class {@code LoginException} extends {@link RuntimeException} and it's thrown during login related issues.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see RuntimeException
 * @since 0.1
 */
public class LoginException extends RuntimeException {
    /**
     * {@code LoginException} initializer.
     *
     * @param msg An {@link String} which contains the exception reason.
     */
    public LoginException(String msg) {
        super("Login Failure due to " + msg);
    }
}