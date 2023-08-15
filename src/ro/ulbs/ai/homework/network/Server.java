package ro.ulbs.ai.homework.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Connection {
    private ServerSocket serverSocket;
    private Socket client;
    private int portNumber = 54321;
    private PrintWriter out;
    private BufferedReader in;
    private boolean clientConected = false;

    public Server() throws IOException {
        serverSocket = new ServerSocket(portNumber);
    }

    public boolean isClientConected() {
        return clientConected;
    }

    public void accept() throws IOException {
        client = serverSocket.accept();
        clientConected = true;
        out = new PrintWriter(client.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));

    }

    public void close() throws IOException {
        serverSocket.close();
        clientConected = false;
    }

    @Override
    public void send(String message) {
        out.println(message);
    }

    @Override
    public String receive() throws IOException {
        return in.readLine();
    }
}
