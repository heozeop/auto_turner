package com.example.auto_turner;

import java.util.ArrayList;

public class Note {
    private String[] Notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private ArrayList<Integer> notes = new ArrayList<>();
    private ArrayList<Integer> pitchs = new ArrayList<>();
    private int length = 0;

    public void add(int note, int pitch){
        notes.add(note);
        pitchs.add(pitch);
        length = notes.size();
    }

    public boolean isEmpty(){
        this.length = notes.size();
        if(length == 0) return true;
        return false;
    }

    public String toString(){
        String result = "";
        for (int i=0;i<length;i++){
            result += Notes[notes.get(i)];
            result += pitchs.get(i).toString();
            result += "  ";
        }
        return result;
    }
}
