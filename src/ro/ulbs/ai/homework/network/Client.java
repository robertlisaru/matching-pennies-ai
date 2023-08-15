package ro.ulbs.ai.homework.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Connection {
    private int portNumber = 54321;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String hostName;
    private boolean connected = false;

    public void connect(String hostName) throws IOException {
        this.hostName = hostName;
        clientSocket = new Socket(hostName, portNumber);
        connected = true;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void close() throws IOException {
        clientSocket.close();
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    public void send(String message) {
        out.println(!Boolean.parseBoolean(message));
    }

    @Override
    public String receive() throws IOException {
        return in.readLine();
    }
}
