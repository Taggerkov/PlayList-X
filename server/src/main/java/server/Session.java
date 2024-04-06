package server;

import com.google.gson.Gson;
import server.login.KeyChain;
import server.login.LoginException;
import server.login.UserName;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

class Session implements Runnable {
    private static final String ANW_TRUE = "SUCCESS";
    private static final String ANW_FALSE = "FAIL";
    private static final String ERROR_LOGOUT = "LOGOUT";
    private static final String ERROR_ACCESS = "RESTRICTED";
    private static final String BC_USERS = "USERS";
    private static final KeyChain keyChain = KeyChain.get();
    private static final Gson gson = new Gson();
    private final Socket socket;
    private final UDPBroadcaster broadcaster;
    private BufferedReader reader;
    private PrintWriter writer;
    private boolean login = false;
    private UserName client = null;

    public Session(Socket socket, UDPBroadcaster broadcaster) {
        this.socket = socket;
        this.broadcaster = broadcaster;
    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        try {
            execute();
        } catch (IOException IO) {
            IO.printStackTrace();
        }
    }

    private void execute() throws IOException {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            writer = new PrintWriter(outputStream);
            session:
            while (true) {
                String request = reader.readLine();
                switch (request) {
                    case "LOGIN" -> {
                        String answer;
                        client = UserName.fresh(reader.readLine());
                        try {
                            login = keyChain.checkCredentials(client, gson.fromJson(reader.readLine(), byte[].class));
                            if (login) {
                                answer = "LOGIN";
                                System.out.println(client + " has joined a session!");
                            } else answer = "LOGIN-PASSWORD";
                        } catch (LoginException e) {
                            answer = e.getMessage();
                        }
                        writer.println(answer);
                        writer.flush();
                    }
                    case "AVAILABLE" -> {
                        writer.println(gson.toJson(keyChain.isAvailable(reader.readLine())));
                        writer.flush();
                    }
                    case "SIGNUP" -> {
                        writer.println(gson.toJson(keyChain.registerKey(UserName.fresh(reader.readLine()), gson.fromJson(reader.readLine(), byte[].class))));
                        writer.flush();
                        broadcaster.broadcast(BC_USERS);
                    }
                    case "CHANGE-USERNAME" ->
                            standardAnswer(keyChain.changeKey(client, UserName.fresh(reader.readLine()), gson.fromJson(reader.readLine(), byte[].class)));
                    case "CHANGE-PASSWORD" ->
                            standardAnswer(keyChain.changeKeyValue(client, gson.fromJson(reader.readLine(), byte[].class), gson.fromJson(reader.readLine(), byte[].class)));
                    case "DELETE-USER" -> {
                        standardAnswer(keyChain.deleteKey(client, gson.fromJson(reader.readLine(), byte[].class)));
                        broadcaster.broadcast(BC_USERS);
                    }
                    case "USERS-FETCH" -> {
                        writer.println(gson.toJson(keyChain.getAllUsers()));
                        writer.flush();
                    }
                    case "USERS-TOTAL" -> {
                        writer.println(gson.toJson(keyChain.size()));
                        writer.flush();
                    }
                    case "ECHO" -> {
                        try {
                            String msg = reader.readLine();
                            writer.println("Server received message: " + msg);
                        } catch (IOException e) {
                            writer.println("ECHO");
                        }
                        writer.flush();
                    }
                    case "EXIT" -> {
                        System.out.println(client.toString() + " has left the session!");
                        break session;
                    }
                }
            }
        } catch (SocketException e) {
            String client = "";
            if (login) client = "(" + this.client.toString() + ") ";
            System.out.println("Connection to client " + client + "lost!");
        } finally {
            socket.close();
        }
    }

    private void standardAnswer(boolean answer) {
        if (login) {
            if (answer) writer.println(ANW_TRUE);
            else writer.println(ANW_FALSE);
        } else writer.println(ERROR_LOGOUT);
        writer.flush();
    }
}