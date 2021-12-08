package com.example.auto_turner;

import java.util.ArrayList;

public class AllWatcher implements Rule{
    private ArrayList<Note> bar = null;
    private int index = 0;

    @Override
    public boolean check(Note note) {
        boolean check = true;
        for(int i=0; i<bar.size(); i++)
            for(int j=0; j<note.length(); j++)
                if(bar.get(i).getNote(0)[0] == note.getNote(j)[0] && bar.get(i).getNote(0)[1] == note.getNote(j)[1])
                    return false;

        return check;
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

    @Override
    public void setIndex(int index) {
        this.index = index;
    }
}
