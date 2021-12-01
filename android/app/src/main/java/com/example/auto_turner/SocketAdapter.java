package com.example.auto_turner;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONArray;

public class SocketAdapter extends WebSocketAdapter {
    private MelodyAnalyser analyser = null;

    public SocketAdapter(MelodyAnalyser analyser){
        this.analyser = analyser;
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        Log.d("TAG", "onTextMessage: " + text);
        analyser.recvJson(new JSONArray(text));
    }
}
