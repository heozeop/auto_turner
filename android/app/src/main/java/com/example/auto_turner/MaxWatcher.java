package com.example.auto_turner;

import java.util.ArrayList;

public class MaxWatcher implements Rule{
    private ArrayList<Note> bar = null;
    private int index;

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
    public void serBar(ArrayList<Note> bar) {
        this.bar = bar;
    }
}
