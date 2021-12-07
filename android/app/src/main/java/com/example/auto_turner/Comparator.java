package com.example.auto_turner;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Comparator implements Runnable{
    private MusicSheet sheet = null;
    private BlockingQueue<Note> play = null;
    private Queue<Note> refined = new LinkedBlockingQueue<>();
    private boolean active = true;
    private long timeout = 1000;

    public Comparator(MusicSheet sheet, BlockingQueue<Note> play){
        this.sheet = sheet;
        this.play = play;
    }

    @Override
    public void run() {
        try{
            while(active){
                Note note = play.poll(timeout, TimeUnit.MICROSECONDS);
                if(note != null){
                    System.out.println(note.toString());
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
        for(int i=0;i<note.length();i++){
            if(sheet.hasOctave(note.getPitch(i)))
                newNote.add(note.getNote(i), note.getPitch(i));
        }
        return newNote;
    }
    private Note checkUsed(Note note){
        Note newNote = new Note();
        for(int i=0;i<note.length();i++){
            if(sheet.getNoteCount(note) > 0);

        }
        return newNote;
    }

}
