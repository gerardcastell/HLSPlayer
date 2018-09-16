package com.example.xavkh.browserpbe;


import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Oscar on 17/12/2017.
 */

public class FragmentModel {
    public float duration;
    public URL url;
    public float bw;
    public float download;

    public FragmentModel(float duration, String urlStr){
        this.duration = duration;
        try {
            this.url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}

