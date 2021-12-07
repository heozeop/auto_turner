package com.example.auto_turner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;

import java.io.OutputStream;

public class MicRecorder implements Runnable {
    private FFTClient fftClient;
    private int sample_rate = 44100;
    private boolean recording = true;

    MicRecorder (FFTClient fftClient){
        this.fftClient = fftClient;
    }

    @Override
    public void run() {
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);

        int bufferSize = AudioRecord.getMinBufferSize(sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        if(bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE){
            Log.e("AUDIO", "Buffer Size Error");
            return;
        }

        Log.e("AUDIO", "buffersize = " + bufferSize);

        byte[] audioBuffer = new byte[bufferSize];
        AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, sample_rate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        if(record.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e("AUDIO", "Audio Record can't initialize");
            return;
        }
        record.startRecording();
        Log.e("AUDIO", "Started Recording");

        try{
            fftClient.connect2audio();
        } catch (Exception e){
            e.printStackTrace();
        }
        while(recording) {
            int numberOfBytes = record.read(audioBuffer, 0, audioBuffer.length);
            fftClient.sendBytes(audioBuffer);
        }

        record.stop();
        record.release();
        fftClient.disconnect();
        Log.e("AUDIO", "Streaming stopped");
    }

    public void startRecord() {
        this.recording = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stopRecord() {
        this.recording = false;
    }
}
