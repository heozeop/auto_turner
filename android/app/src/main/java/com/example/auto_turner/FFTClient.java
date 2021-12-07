package com.example.auto_turner;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

public class FFTClient {
    private WebSocket ws = null;
    private WebSocketAdapter adapter = null;
    private final String server = "ws://192.168.35.100:8000";
    private String audioSocket = "/audio";
    private String sheetSocket = "/sheet";

    public FFTClient() {
    }

    public void addListener(WebSocketAdapter listener){
        this.adapter = listener;
    }

    public void connect2audio() {
        try{
            this.connect(this.server + audioSocket, 8000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void connect2sheet() {
        try{
            this.connect(this.server + sheetSocket, 8000);
        } catch(Exception e){

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
