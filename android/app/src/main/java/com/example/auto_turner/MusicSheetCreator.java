package com.example.auto_turner;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MusicSheetCreator {
    private FFTClient fftClient;
    private ArrayList<ArrayList<Note>> bars = null;

    public MusicSheetCreator(FFTClient fftClient){
        this.fftClient = fftClient;
        init();
        fftClient.connect2sheet();
        fftClient.disconnect();
    }

    public MusicSheet getSheet(String filename){
        return new MusicSheet(bars);
    }

    private void init(){
        fftClient.addListener(new WebSocketAdapter(){
            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                try{
                    JSONArray array = new JSONArray(text);
                    recvJson(array);
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void recvJson(JSONArray array){
        bars = new ArrayList<>();
        try{
            for (int i=0; i< array.length(); i++){
                JSONArray barData = array.getJSONArray(i);
                ArrayList<Note> bar = new ArrayList<>();
                for (int j=0; j<barData.length(); j++){
                    Note note = new Note();
                    JSONArray notes = barData.getJSONArray(j);
                    for (int n =0; n < notes.length(); n++) {
                        JSONObject object = notes.getJSONObject(n);
                        int pitch = object.getInt("pitch"), octave = object.getInt("octave");
                        note.add(pitch, octave);
                    }
                    bar.add(note);
                }
                bars.add(bar);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}
