package com.playlistx.model.login;

/* CLASS H-COUNTER: (Dear colleagues, please remember to add the hours contributed to this code!) 0.17h */

/**
 * The class {@code LoginException} extends {@link java.lang.RuntimeException} and it's thrown during login related issues.
 *
 * @author Sergiu Chirap
 * @version 1.0
 * @see java.lang.RuntimeException
 * @since 0.1
 */
public class LoginException extends RuntimeException {
    /**
     * {@code LoginException} initializer.
     *
     * @param msg An {@link java.lang.String} which contains the exception reason.
     */
    public LoginException(String msg) {
        super("Login Failure due to " + msg);
    }
}