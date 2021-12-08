package com.example.auto_turner;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Comparator implements Runnable{
    private MusicSheet sheet;
    private SheetAnalyser sheetAnalyser;
    private BlockingQueue<Note> play;
    private boolean active = true;
    private long timeout = 1000;
    private ArrayList<Rule> rules = new ArrayList<>();

    public Comparator(MusicSheet sheet, BlockingQueue<Note> play){
        this.sheet = sheet;
        this.play = play;
        sheetAnalyser = new SheetAnalyser(sheet);
    }

    public void initRules(){
        for(Iterator<Note> iter = sheetAnalyser.getMinIterator(); iter.hasNext();){
            MinWatcher minWatcher = new MinWatcher();
            minWatcher.setCheckNote(iter.next());
            minWatcher.setIndex(1);
            minWatcher.setBar(sheet.getBar(1));
            rules.add(minWatcher);
        }
        for(Iterator<Note> iter = sheetAnalyser.getMaxIterator(); iter.hasNext();){
            MaxWatcher maxWatcher = new MaxWatcher();
            maxWatcher.setCheckNote(iter.next());
            maxWatcher.setIndex(0);
            maxWatcher.setBar(sheet.getBar(0));
            rules.add(maxWatcher);
        }
        AllWatcher allWatcher = new AllWatcher();
        allWatcher.setIndex(0);
        allWatcher.setBar(sheet.getBar(0));
        rules.add(allWatcher);
    }

    @Override
    public void run() {
        int nowBar = 0;
        try{
            while(active){
                boolean next = false;
                Note note = play.poll(timeout, TimeUnit.MICROSECONDS);
                note = checkOctave(note);
                note = checkPitch(note);
                for (Rule rule : rules){
                    next = next || rule.check(note);
                }
                if(next){
                    for (Rule rule : rules){
                        int index = rule.next();
                        rule.setBar(sheet.getBar(index));
                    }
                    nowBar++;
                    Log.d("Sheet", "The Bar you are currently watching is " + nowBar);
                }
            }
        } catch(InterruptedException e){
            e.printStackTrace();
        }

    }

    public void start(){
        Thread thread = new Thread(this);
        active = true;
        thread.setDaemon(true);
        thread.start();
    }

    public void stop(){
        this.active = false;
    }

    private Note checkOctave(Note note){
        Note newNote = new Note();

        for(int i=0;i<note.length();i++)
            if(sheet.hasOctave(note.getNote(i)[1]))
                newNote.add(note.getNote(i)[0], note.getNote(i)[1]);

        return newNote;
    }

    private Note checkPitch(Note note){
        Note newNote = new Note();

        for(int i=0;i<note.length();i++)
            if(sheet.getNoteCount(note.getNote(i)[0], note.getNote(i)[1]) != 0)
                newNote.add(note.getNote(i)[0], note.getNote(i)[1]);

        return newNote;
    }
}
