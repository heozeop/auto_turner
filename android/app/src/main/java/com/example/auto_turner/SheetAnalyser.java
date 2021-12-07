package com.example.auto_turner;

public class SheetAnalyser {
    private MusicSheet sheet;

    public SheetAnalyser(MusicSheet sheet){
        this.sheet = sheet;
    }

    public Note getMinNotes(){
        return new Note();
    }

    public Note getMaxNotes(){
        return new Note();
    }
}
