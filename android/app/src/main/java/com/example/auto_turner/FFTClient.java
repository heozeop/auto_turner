package com.example.auto_turner;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

public class FFTClient {
    private WebSocket ws;

    public void connect(String host, int timeout) throws Exception{
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(timeout);
        ws = factory.createSocket(host);
        ws.addListener(new SocketAdapter());
        ws.connectAsynchronously();
    }

    public void disconnect(){
        if(ws.isOpen()){
            ws.disconnect();
            ws = null;
        }
    }

    public void sendMessage(){
        if(ws.isOpen()){
            ws.sendText("Message from Android");
        }
    }
}
