package com.example.xavkh.browserpbe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Oscar on 17/12/2017.
 */

public class MasterModel {
    public URL url;
    public int bw;
    public String codecs;
    ArrayList<FragmentModel> frags;

    public MasterModel(String urlStr, int bw, String codecs){
        try {
            this.url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.bw = bw;
        this.codecs = codecs;
    }
}