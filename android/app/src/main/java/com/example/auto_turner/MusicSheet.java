package com.example.auto_turner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MusicSheet {
    private ArrayList<ArrayList<Note>> bars = new ArrayList<>();

    public void setSheet(JSONArray sheet){
        try{
            for (int i=0; i< sheet.length(); i++){
                JSONArray barData = sheet.getJSONArray(i);
                ArrayList<Note> bar = new ArrayList<>();
                for (int j=0; j<barData.length(); j++){
                    Note note = new Note();
                    JSONArray notes = barData.getJSONArray(j);
                    for (int n =0; n < notes.length(); n++) {
                        JSONObject object = notes.getJSONObject(n);
                        int index = object.getInt("note"), pitch = object.getInt("pitch");
                        note.add(index, pitch);
                    }
                    bar.add(note);
                }
                bars.add(bar);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String bar2string(int index){
        ArrayList<Note> bar = bars.get(index);
        String string = "";
        for(int i=0;i<bar.size();i++){
            string += bar.get(i).toString();
        }
        return string;
    }
}
