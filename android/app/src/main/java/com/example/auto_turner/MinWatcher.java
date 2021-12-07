package com.example.auto_turner;

import java.util.ArrayList;

public class MinWatcher implements Rule{
    @Override
    public boolean check(Note note) {
        return false;
    }

    @Override
    public int next() {
        return 0;
    }

    @Override
    public void setBar(ArrayList<Note> bar) {

    }

    public void setNote(Note note){

    }
}
