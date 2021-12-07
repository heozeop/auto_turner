package com.example.auto_turner;

import java.util.ArrayList;

public interface Rule {
    public boolean check(Note note);
    public int next();
    public void setBar(ArrayList<Note> bar);
}
