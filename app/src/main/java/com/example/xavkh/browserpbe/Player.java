package com.example.xavkh.browserpbe;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import io.vov.vitamio.provider.MediaStore;

public class Player extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        //path will be the url of the video (can be .m3u8 or .ts)
        String path = this.getIntent().getStringExtra("url");
        //Vitameo shit here:
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(new MediaController(Player.this));
        videoView.requestFocus();
        videoView.postInvalidateDelayed(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                videoView.start();
            }
        }).start();
    }
}
