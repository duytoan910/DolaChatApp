package com.example.dolaapp;


import com.example.dolaapp.Others.Session;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketIo {
    public static final String ServerUrl="http://10.200.0.84:3000";
    private static SocketIo instance;
    private Socket mSocket;
    private String UserPhone = Session.GetUserPhone();
    private SocketIo() {
        {
            try {
                mSocket = IO.socket(ServerUrl);
                mSocket.connect();
                mSocket.emit("new-User",UserPhone);

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
    public void Destroy(){
        mSocket.disconnect();
        mSocket = null;
        instance=null;
    }
}
