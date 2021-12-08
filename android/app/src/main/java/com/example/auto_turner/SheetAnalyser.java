package com.example.auto_turner;

import java.util.ArrayList;
import java.util.Iterator;

public class SheetAnalyser {
    private MusicSheet sheet;
    private ArrayList<Note> minNotes = new ArrayList<>();
    private ArrayList<Note> maxNotes = new ArrayList<>();

    public SheetAnalyser(MusicSheet sheet){
        this.sheet = sheet;
        init();
    }

    public Iterator<Note> getMinIterator(){
        return minNotes.iterator();
    }

    public Iterator<Note> getMaxIterator(){
        return maxNotes.iterator();
    }

    private void init(){
        ArrayList<Integer[]> histogram = sheet.getHistogram();

        int min = findMinValue(histogram);
        for(int i=0; i<histogram.size(); i++) {
            for (int j = 0; j < 12; j++) {
                if (histogram.get(i)[j] == min) {
                    Note note = new Note();
                    note.add((j + 3) % 12, i + 1);
                    minNotes.add(note);
                }
            }
        }

        int max = findMaxValue(histogram);
        for(int i=0; i<histogram.size(); i++) {
            for (int j = 0; j < 12; j++) {
                if (histogram.get(i)[j] == max) {
                    Note note = new Note();
                    note.add((j + 3) % 12, i + 1);
                    minNotes.add(note);
                }
            }
        }
    }

    private int findMinValue(ArrayList<Integer[]> histogram){
        int min = Integer.MAX_VALUE;

        for(int i=0; i<histogram.size(); i++)
            for(int j=0; j<12; j++)
                if(histogram.get(i)[j] != 0 && histogram.get(i)[j] < min)
                    min = histogram.get(i)[j];

        return min;
    }
    private int findMaxValue(ArrayList<Integer[]> histogram){
        int max = 0;

        for(int i=0; i<histogram.size(); i++)
            for(int j=0; j<12; j++)
                if(histogram.get(i)[j] > max)
                    max = histogram.get(i)[j];

        return max;
    }
}
