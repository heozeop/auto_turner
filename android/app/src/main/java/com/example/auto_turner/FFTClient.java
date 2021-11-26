package com.example.auto_turner;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.OutputStream;

public class FFTClient {
    private WebSocket ws;
    private String host = null;

    public void setHost(String host){
        this.host = host;
    }

    public void connect() throws Exception {
        this.connect(this.host, 5000);
    }

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

    public void sendBytes(byte[] bytes){
        if(ws.isOpen()){
            ws.sendBinary(bytes);
        }
    }
}
