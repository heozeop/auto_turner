package com.example.auto_turner;

import java.util.ArrayList;


public class MaxWatcher implements Rule {
    public ArrayList<Note> bar;
    public int index;
    Note note; //연주중인 노트
    Note checkNote; //검사하기 위한 노트
    private boolean isHave;
    @Override
    public boolean check(Note note) {
        if(isHave) {
            return false;
        }
        else {
            for(int i=0;i<checkNote.length();i++) {
                if(checkNote.getNote(i)==note.getNote(0))
                    return true;
            }
            return false;
        }
    }

    @Override
    public int next() {
        index=index+1;
        return index;
    }

    public boolean isThere() {
        for(int i=0; i<bar.size();i++) {
            return bar.get(i).getNote(0)==note.getNote(0);
        }
        return false;
    }


    @Override
    public void setBar(ArrayList<Note> nextBar) {
        bar=nextBar;
        isHave=isThere();
    }
}
