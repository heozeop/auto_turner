package com.example.auto_turner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;

public class MelodyAnalyser {
    private BlockingQueue<Note> queue;
    private JSONArray lastData = new JSONArray();

    public MelodyAnalyser(BlockingQueue<Note> quere){
        this.queue = quere;
    }

    public void recvJson(JSONArray array) {
        if (array.length() == 0){
            lastData = array;
            return;
        }

        Note note = new Note();
        try{
            for (int i =0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                int pitch = object.getInt("pitch"), octave = object.getInt("octave");
                if (isNew(pitch, octave))
                    note.add(pitch, octave);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        lastData = array;
        if (!note.isEmpty()) {
            queue.add(note);
        }
    }

    private boolean isNew(int pitch, int octave) throws JSONException{
        for (int i = 0; i < lastData.length(); i++){
            JSONObject object = lastData.getJSONObject(i);
            if (pitch == object.getInt("pitch") && octave == object.getInt("octave"))
                return false;
        }
        return true;
    }
}
