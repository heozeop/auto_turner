package com.example.auto_turner;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

public class FFTClient {
    private WebSocket ws = null;
    private WebSocketAdapter adapter;
    private String host = null;

    public FFTClient(WebSocketAdapter adapter) {
        this.adapter = adapter;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void connect() {
        try{
            this.connect(this.host, 8000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void connect(String host, int timeout) throws Exception {
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(timeout);
        ws = factory.createSocket(host);
        ws.addListener(adapter);
        ws.connectAsynchronously();
    }

    public void disconnect() {
        if(ws.isOpen()){
            ws.disconnect();
            ws = null;
        }
    }

    public void sendBytes(byte[] bytes) {
        if(ws.isOpen())
            ws.sendBinary(bytes);
    }
}
