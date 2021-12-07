package com.example.auto_turner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicSheet {
    private ArrayList<ArrayList<Note>> bars = new ArrayList<>();
    private ArrayList<Integer[]> histogram = new ArrayList<Integer[]>();

    public void setHistogram(){
        Integer[] Octave1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] Octave2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] Octave3 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] Octave4 = {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 1};
        Integer[] Octave5 = {1, 0, 6, 5, 0, 6, 4, 8, 0, 13, 12, 0};
        Integer[] Octave6 = {5, 0, 7, 1, 0, 1, 0, 0, 0, 0, 0, 0};
        Integer[] Octave7 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] Octave8 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        histogram.add(Octave1);
        histogram.add(Octave2);
        histogram.add(Octave3);
        histogram.add(Octave4);
        histogram.add(Octave5);
        histogram.add(Octave6);
        histogram.add(Octave7);
        histogram.add(Octave8);
    }
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
            setHistogram();
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

    public void sheetAnalysing(){

    }

    public int getNoteCount(Note note, int index){
        int notes=note.getNote(index);
        int pitch=note.getPitch(index);
        int noteIndex=(notes+9) % 12;
        int count=histogram.get(pitch-1)[noteIndex];
        return count;
    }

    public boolean hasOctave(int pitch){
        if(Arrays.equals(histogram.get(pitch - 1), new Integer[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}))
            return false;
        else
            return true;
    }

    public Note getMinNotes(){
        Note note = new Note();
        note.add(10, 4);
        note.add(1, 4);
        note.add(2, 4);
        return note;
    }

    public ArrayList<Note> getBar(int index){
        return bars.get(index);
    }
}
