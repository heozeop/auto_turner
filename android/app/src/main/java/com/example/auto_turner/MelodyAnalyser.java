package com.example.auto_turner;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class MelodyAnalyser {
    private final Queue<JSONArray> queue = new LinkedBlockingQueue<>();
    private JSONArray lastData = new JSONArray();

    public int length(){
        int length = queue.size();
        queue.clear();
        return length;
    }

    public void recvJson(JSONArray array) {
        try{
            for (int i =0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                if (isNew(object.getInt("note"), object.getInt("pitch"))){

                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        lastData = array;
        
    }

    private boolean checkDiff(JSONArray array){
        return !array.equals(lastData);
    }
    private boolean isNew(int note, int pitch){

        return false;
    }
}
