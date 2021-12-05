package com.example.auto_turner;

import java.util.ArrayList;

public class Note {
    private ArrayList<Integer> notes = new ArrayList<>();
    private ArrayList<Integer> pitchs = new ArrayList<>();
    private int length = 0;

    public void add(int note, int pitch){
        notes.add(note);
        pitchs.add(pitch);
    }

    public boolean isEmpty(){
        this.length = notes.size();
        if(length == 0) return true;
        return false;
    }
}
