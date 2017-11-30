package com.example.xavkh.browserpbe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Reproductor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        //path will be the url of the video (can be .m3u8 or .ts)
        String path = this.getIntent().getStringExtra("url");

        //Vitameo shit here:
    }
}
