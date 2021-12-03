package com.example.auto_turner;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Map;

public class SocketAdapter extends WebSocketAdapter {
    private MelodyAnalyser analyser = null;

    public SocketAdapter(MelodyAnalyser analyser){
        this.analyser = analyser;
    }

    @Override
    public void onTextMessage(WebSocket websocket, String text) {
        try{
            JSONArray array = new JSONArray(text);
            analyser.recvJson(array);
        } catch(JSONException e){
            e.printStackTrace();
        }
    }



    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        super.onConnected(websocket, headers);
        Log.e("SOCKET", "Connected");
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        Log.e("SOCKET", "Disconnected");
    }
}
