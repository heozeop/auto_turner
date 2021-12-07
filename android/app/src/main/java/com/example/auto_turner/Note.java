package com.example.auto_turner;

import java.util.ArrayList;

public class Note {
    private final static String[] Notes = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    private ArrayList<Integer[]> notes = new ArrayList<>();
    private int length = 0;

    public void add(int pitch, int octave){
        if(!isHave(pitch, octave)){
            Integer[] note = {pitch, octave};
            notes.add(note);
            length++;
        }
    }

    public boolean isEmpty(){
        if(length == 0) return true;
        return false;
    }

    public int length(){
        return length;
    }

    public Integer[] getNote(int index){
        return notes.get(index);
    }

    public String toString(){
        String result = "";
        for (int i=0;i<length;i++){
            result += Notes[notes.get(i)[0]];
            result += notes.get(i)[1];
            result += "  ";
        }
        return result;
    }

    private boolean isHave(int pitch, int octave){
        for(int i=0; i<length; i++)
            if(pitch == notes.get(i)[0] && octave == notes.get(i)[1])
                return true;
        return false;
    }
}
