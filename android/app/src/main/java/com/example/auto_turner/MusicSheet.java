package com.example.auto_turner;

import java.util.ArrayList;

public class MusicSheet {
    private ArrayList<ArrayList<Note>> bars;
    private ArrayList<Integer[]> histogram = null;

    public MusicSheet(ArrayList<ArrayList<Note>> bars){
        this.bars = bars;
        calcHistogram();
    }

    public String bar2string(int index){
        ArrayList<Note> bar = bars.get(index);
        String string = "";
        for(int i=0;i<bar.size();i++){
            string += bar.get(i).toString();
        }
        return string;
    }

    public int getNoteCount(Note note, int index){
        return histogram.get(note.getNote(index)[1]-1)[(note.getNote(index)[0]+9)%12];
    }

    public boolean hasOctave(int octave){
        for(int i=0; i<12; i++)
            if(histogram.get(octave - 1)[i] != 0) return true;
        return false;
    }

    public ArrayList<Note> getBar(int index){
        return bars.get(index);
    }

    private void calcHistogram(){
        for(int i=0; i<8; i++){
            Integer[] ints = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            histogram.add(ints);
        }

        for (ArrayList<Note> bar : bars)
            for (Note note : bar)
                for(int i=0; i<note.length(); i++)
                    histogram.get(note.getNote(i)[1]-1)[note.getNote(i)[0]] += 1;
    }
}
