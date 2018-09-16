package com.example.xavkh.browserpbe;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import java.util.ArrayList;

public class Reproductor extends AppCompatActivity {

    VideoView videoView;
    // variable de control sobre la reproduccio de video
    int currentVideo;
    // variables utilitzades per l'algoritme adapatatiu
    long start;
    long end;
    long bw;
    // contenidors per emmagatzemar les possibles llistes
    ArrayList<MasterModel> masterList;
    ArrayList<FragmentModel> normalList;

    //etiqueta requerida per utilitzar l'OnInfoListener
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);
        //path will be the url of the video (in case only a .ts file is pressed)
        String path;
        // variable constant provinent del browser que indica quin tipus de llista es reproduira
        final int typeList = this.getIntent().getIntExtra("type", 2);
        videoView = (VideoView) findViewById(R.id.videoView);

        switch (typeList) {
            case Constants.MASTERPLAYLIST:   // Master Playlist
                masterList = (ArrayList<MasterModel>) this.getIntent().getSerializableExtra("list");
            case Constants.PLAYLIST:   // Playlist
                normalList = (ArrayList<FragmentModel>) this.getIntent().getSerializableExtra("list");
            case Constants.OTHER:   // .ts
                path = this.getIntent().getStringExtra("url");
                videoView.setVideoURI(Uri.parse(path));
                videoView.start();
        }

        currentVideo = 0;


        if (typeList == 0)
            // Setegem un Listener amb tal de que estigui pendent de quan comenci l'streaming
            // i quan acabi per tal de capturar el temps per calcular el bandwidth
            videoView.setOnInfoListener(new android.media.MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(android.media.MediaPlayer mediaPlayer, int i, int i1) {
                    if (mediaPlayer.MEDIA_INFO_BUFFERING_START == i) { //comença a carregar el video
                        getStartTime();
                        return true;
                    }
                    if (mediaPlayer.MEDIA_INFO_BUFFERING_END == i) {   //acaba
                        getEndTime();
                        bw = 100 / (end - start);
                        return true;
                    }
                    return false;
                }
            });

        // Setegem un altre listener per tal de que capturi quan acabi la reproduccio i començi la següent
        // (incloent la possibilitat de que sigui Adaptatiu o no
        videoView.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mediaPlayer) {
                if (typeList == 1) playNext();
                else if (typeList == 0) playNextAdaptive();
                else return;
            }
        });
    }

    //Reproduir el següent fragment del video(no adaptatiu)
    public void playNext() {
        if(currentVideo < normalList.size()){
            videoView.setVideoURI(Uri.parse(normalList.get(currentVideo).url.toString()));
            videoView.start();
            currentVideo++;
        }
        else return;
    }

    // Reproduir el següent fragment del video(adaptatiu)
    public void playNextAdaptive() {
        if (currentVideo < masterList.get(0).frags.size()) {
            int bandwidth = 0;
            for (MasterModel master : masterList) {
                 if(bandwidth <= bw){
                    videoView.setVideoURI(Uri.parse(master.frags.get(currentVideo).url.toString()));
                    bandwidth = master.bw;
                 }
            }
            videoView.start();
            currentVideo++;
        }
        else return;
    }
    public void getStartTime(){
        start = System.currentTimeMillis();
    }
    public void getEndTime(){
        end = System.currentTimeMillis();
    }
}
