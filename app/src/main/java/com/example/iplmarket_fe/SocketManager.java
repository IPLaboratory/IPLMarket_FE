package com.example.iplmarket_fe;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketManager {
    private static Socket mSocket;

    public static Socket getSocket() throws URISyntaxException {
        if (mSocket == null) {
            try {
                mSocket = IO.socket("http://192.168.0.14:8080");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return mSocket;
    }
}
