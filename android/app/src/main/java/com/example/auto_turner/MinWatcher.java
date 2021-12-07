package com.example.auto_turner;

import java.util.ArrayList;

public class MinWatcher implements Rule {
    public ArrayList<Note> bar;
    public int index;
    Note note; //연주중인 노트
    Note checkNote; //검사하기 위한 노트
    private boolean isHave;
    @Override
    public boolean check(Note note) {
        if(isHave) {
            for(int i=0;i<checkNote.length();i++) {
                if(checkNote.getNote(i)==note.getNote(0))
                    return true;
            }
            return false;
        }
        else {
            return false;
        }
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
            return bar.get(i).getNote(0)==note.getNote(0);
        }
        return false;
    }

    public void setCheckNote(Note note){
        this.checkNote = note;
    }
}
