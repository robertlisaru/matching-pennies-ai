package ro.ulbs.ai.homework.network;

import java.io.IOException;

public interface Connection {

    void send(String message);

    String receive() throws IOException;
}
