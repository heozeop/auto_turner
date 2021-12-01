package com.example.auto_turner;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MelodyAnalyser {
    private final String[] notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private final Queue<JSONArray> queue = new LinkedBlockingQueue<>();
    private JSONArray lastData = new JSONArray();

    public MelodyAnalyser(){

    }

    public void recvJson(JSONArray array){
        if(checkDiff(array)) {
            queue.add(array);
            lastData = array;
            Log.d("MELODY", "data =" + array.toString());
        }
    }

    private boolean checkDiff(JSONArray array){
        return !array.equals(lastData);
    }
}
