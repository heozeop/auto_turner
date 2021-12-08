package com.example.auto_turner;

import java.util.ArrayList;

public class MinWatcher implements Rule {
    public ArrayList<Note> bar;
    public int index; //현재 마디
    private int pitch; //주시할 노트
    private int octave;
    private boolean isHave;
    @Override
    public boolean check(Note note) {
        if(isHave)
            for(int i=0; i<note.length(); i++)
                if(note.getNote(i)[0] == pitch && note.getNote(i)[1] == octave)
                    return true;

        return false;
    }

    @Override
    public int next() {
        index=index+1;
        return index;
    }

    @Override
    public void setBar(ArrayList<Note> nextBar) {
        bar=nextBar;
        isHave=isThere();
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isThere() {
        for(int i=0; i<bar.size();i++) {
            Note tempNote=bar.get(i);
            if(tempNote.getNote(0)[0]==this.pitch && tempNote.getNote(0)[1]==this.octave)
                return true;
        }
        return false;
    }

    public void setCheckNote(Note note){
        this.pitch = note.getNote(0)[0];
        this.octave = note.getNote(0)[1];
    }
}
