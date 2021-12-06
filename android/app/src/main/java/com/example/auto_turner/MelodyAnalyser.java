package com.example.auto_turner;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MelodyAnalyser {
    private BlockingQueue<Note> queue = null;
    private JSONArray lastData = new JSONArray();

    public MelodyAnalyser(BlockingQueue<Note> quere){
        this.queue = quere;
    }

    public void recvJson(JSONArray array) {
        if (array.length() == 0){
            lastData = array;
            return;
        }

        Note frame = new Note();
        try{
            for (int i =0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int note = object.getInt("note"), pitch = object.getInt("pitch");
                if (isNew(note, pitch))
                    frame.add(note, pitch);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        lastData = array;
        if (!frame.isEmpty()) {
            queue.add(frame);
        }
    }

    private boolean isNew(int note, int pitch) throws JSONException{
        for (int i = 0; i < lastData.length(); i++){
            JSONObject object = lastData.getJSONObject(i);
            if (note == object.getInt("note") && pitch == object.getInt("pitch"))
                return false;
        }
        return true;
    }
}
