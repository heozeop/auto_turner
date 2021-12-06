package com.example.auto_turner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.MockView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;

import android.widget.LinearLayout;
import android.widget.ListView;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    private PlayButton   playButton = null;
    private MediaPlayer   player = null;

    private FFTClient fftClient = null;
    private MicRecorder micRecorder = null;
    private MelodyAnalyser melodyAnalyser = null;
    private Comparator comparator = null;
    private MusicSheet sheet = null;
    private BlockingQueue<Note> playNotes = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        comparator.start();
    }

    private void stopPlaying() {
        comparator.stop();
    }

    private void startRecording() {
        micRecorder.startRecord();
    }

    private void stopRecording() {
        micRecorder.stopRecord();
    }
    class RecordButton extends AppCompatButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setText("Stop recording");
                } else {
                    setText("Start recording");
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setText("Start recording");
            setOnClickListener(clicker);
        }
    }
    class PlayButton extends AppCompatButton {
        boolean mStartPlaying = true;
        int num = 0 ;
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                num++;
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setText("Stop playing");
                } else {
                    setText("Start playing");
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setText("Start playing");
            setOnClickListener(clicker);
        }
    }
    static class ScoreImageButton extends AppCompatImageButton {
        int page = 0;
        int maxPage = 2;
        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                page++;
                switchScoreImage();
            }
        };
        private void switchScoreImage()
        {
            switch (page % maxPage){
                case 0:
                    setImageResource(R.drawable.score0);
                    setScaleType(ScaleType.FIT_XY);
                    break;
                case 1:
                    setImageResource(R.drawable.score1);
                    setScaleType(ScaleType.FIT_XY);
                    break;
//                case 2:
//                    setImageResource(R.drawable.score2);
//                    setScaleType(ScaleType.FIT_XY);
//                    break;
//                case 3:
//                    setImageResource(R.drawable.score3);
//                    setScaleType(ScaleType.FIT_XY);
//                    break;

            }
        }
        public ScoreImageButton(Context ctx) {
            super(ctx);
            switchScoreImage();
            setOnClickListener(clicker);
        }
    }
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        LinearLayout ll = new LinearLayout(this);
        ll.setVerticalScrollBarEnabled(true);
        ll.setOrientation(LinearLayout.VERTICAL);
        recordButton = new RecordButton(this);
        ll.addView(recordButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
        playButton = new PlayButton(this);
        ll.addView(playButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
        ScoreImageButton scoreImageButton = new ScoreImageButton(this);
        ll.addView(scoreImageButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));
        setContentView(ll);

        playNotes = new LinkedBlockingQueue<>();
        sheet = new MusicSheet();
        melodyAnalyser = new MelodyAnalyser(playNotes);
        comparator = new Comparator(sheet, playNotes);
        fftClient = new FFTClient(new SocketAdapter(melodyAnalyser));
        fftClient.setHost("ws://192.168.35.100:8000/audio");
        micRecorder = new MicRecorder(fftClient);

        FFTClient sheetLoad = new FFTClient(new WebSocketAdapter(){
            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                try{
                    sheet.setSheet(new JSONArray(text));
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        sheetLoad.setHost("ws://192.168.35.100:8000/sheet");
        sheetLoad.loadSheet();
        sheetLoad.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}