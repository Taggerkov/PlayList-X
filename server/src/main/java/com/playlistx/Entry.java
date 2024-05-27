package com.playlistx;

import com.playlistx.model.proxy.PersonalSession;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * PlayList X server executable.
 * <br> Runs the server through RMI connections.
 *
 * @author Sergiu Chirap
 * @version final
 * @since 0.1
 */
public class Entry {
    /**
     * Main method and entry access of main {@link Thread}.
     * <br> Sets up the server.
     *
     * @param args Ignored entry parameters.
     * @throws RemoteException RMI Connection Error.
     */
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("session", new PersonalSession());
        System.out.println("Server started!");
    }
}