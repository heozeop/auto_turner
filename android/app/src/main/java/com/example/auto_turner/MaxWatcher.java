package com.example.auto_turner;

import java.util.ArrayList;

public class MaxWatcher implements Rule{
    private ArrayList<Note> bar = null;
    private int index = 0;
    private Note note = null;

    @Override
    public boolean check(Note note) {
        return false;
    }

    @Override
    public int next() {
        index++;
        return index;
    }

    @Override
    public void setBar(ArrayList<Note> bar) {
        this.bar = bar;
    }

    public void setNote(Note note){
        this.note = note;
    }
}
