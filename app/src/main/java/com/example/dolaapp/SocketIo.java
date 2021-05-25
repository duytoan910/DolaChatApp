package com.example.dolaapp;


import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIo {
    // đường dẫn tới Server  socket
    public static final String ServerUrl="http://192.168.1.10:4000";
    private static SocketIo instance; // ins Class
    private Socket mSocket;
    private SocketIo() {
        {
            try {
                mSocket = IO.socket(ServerUrl);


            } catch (URISyntaxException e) {}
        }
    }
    // pattern
    public synchronized static SocketIo getInstance() {
        if (instance==null) {
            instance=new SocketIo();
        }
        return instance;
    }

    public Socket getmSocket(){
        return mSocket;
    }
    public  void connect(){
        mSocket.connect();
    }

}
