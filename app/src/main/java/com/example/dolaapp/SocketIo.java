package com.example.dolaapp;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIo {
    public static final String ServerUrl="http://10.200.0.84:4000";
    private static SocketIo instance;
    private Socket mSocket;
    private SocketIo() {
        {
            try {
                mSocket = IO.socket(ServerUrl);
                mSocket.connect();

            } catch (URISyntaxException e) {}
        }
    }

    public synchronized static SocketIo getInstance() {
        if (instance==null) {
            instance=new SocketIo();
        }
        return instance;
    }

    public Socket getmSocket(){
        return mSocket;
    }
}
