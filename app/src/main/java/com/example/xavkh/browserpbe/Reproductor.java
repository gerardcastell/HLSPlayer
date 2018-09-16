package com.example.xavkh.browserpbe;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import io.vov.vitamio.provider.MediaStore;

public class Reproductor extends AppCompatActivity {

    private URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        //path will be the url of the video (can be .m3u8 or .ts)
        String path = this.getIntent().getStringExtra("url");
        //Vitameo shit here:
        try {
            this.url = new URL(path);
            InputStream input = this.url.openStream();
            URLConnection cnnct = this.url.openConnection();
            while(input.read() != -1) {

            }

        } catch (Exception ex ){}
        final VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(new MediaController(Reproductor.this));
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
