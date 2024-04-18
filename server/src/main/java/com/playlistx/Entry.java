package com.playlistx;

import com.playlistx.model.proxy.PersonalSession;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Entry {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("session", new PersonalSession());
        System.out.println("Server started!");
    }
}