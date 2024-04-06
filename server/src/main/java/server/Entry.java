package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Entry {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        UDPBroadcaster broadcaster = new UDPBroadcaster("230.0.0.0", 8888);
        System.out.println("Chatty Services are Online!");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected.");
            Thread thread = new Thread(new Session(socket, broadcaster));
            thread.start();
        }
    }
}