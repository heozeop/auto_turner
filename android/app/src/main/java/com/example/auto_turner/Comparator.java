package com.example.auto_turner;

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
    private Rule[] rules = new Rule[3];

    public Comparator(MusicSheet sheet, BlockingQueue<Note> play){
        this.sheet = sheet;
        this.play = play;
        sheetAnalyser = new SheetAnalyser(sheet);
    }

    public void initRules(){
        MinWatcher minWatcher = new MinWatcher();
        minWatcher.setCheckNote(sheetAnalyser.getMinNotes());
        minWatcher.setIndex(1);
        minWatcher.setBar(sheet.getBar(1));

        MaxWatcher maxWatcher = new MaxWatcher();
        maxWatcher.setCheckNote(sheetAnalyser.getMaxNotes());
        maxWatcher.setIndex(0);
        maxWatcher.setBar(sheet.getBar(0));

        AllWatcher allWatcher = new AllWatcher();
        allWatcher.setIndex(0);
        allWatcher.setBar(sheet.getBar(0));

        rules[0] = minWatcher;
        rules[1] = maxWatcher;
        rules[2] = allWatcher;
    }

    @Override
    public void run() {
        try{
            while(active){
                Note note = play.poll(timeout, TimeUnit.MICROSECONDS);
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

}
