package com.example.xavkh.browserpbe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Reproductor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        String path = this.getIntent().getStringExtra("url");
    }
}
