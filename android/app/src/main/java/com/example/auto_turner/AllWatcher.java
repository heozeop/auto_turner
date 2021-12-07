package com.example.auto_turner;

import java.util.ArrayList;

public class AllWatcher implements Rule{
    private ArrayList<Note> bar = null;
    private int index = 0;

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
