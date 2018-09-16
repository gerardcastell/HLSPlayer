package com.example.xavkh.browserpbe;

import android.content.Context;

/**
 * Created by xavkh on 12/19/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Oscar on 17/12/2017.
 */

public class Handler_m3u8_Task extends AsyncTask<URL, Integer, String> {
    Context context;
    public Intent intent;

    public Handler_m3u8_Task(Context context) {
        this.context = context;
        this.intent = new Intent(this.context, Reproductor.class);
    }

    @Override
    protected String doInBackground(URL... url) {
        InputStream input = null;
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) url[0].openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // download the file
            input = connection.getInputStream();

            /*ArrayList<Integer> data = new ArrayList<Integer>();
            int byteReaded = input.read();
            while(byteReaded!=-1){
                data.add(byteReaded);
                byteReaded=input.read();
            }
            Scanner sc = new Scanner(data.toString());*/

            //Scanner sc = new Scanner(input).useDelimiter("");
            Scanner sc = new Scanner(input).useDelimiter("\n");


            switch (typeList(sc)) {
                case Constants.MASTERPLAYLIST:
                    //hls
                    //parse de la masterplaylist. Les llistes de fragments de cada element buides
                    ArrayList<MasterModel> masterList = parseMaster(sc);
                    //tanquem connexió amb la URL de la masterplaylist
                    connection.disconnect();
                    input.close();
                    sc.close();
                    //per cada url a una playlist..
                    for (MasterModel x : masterList) {
                        //obrim connexions, scanner,...
                        HttpURLConnection connectionMaster = (HttpURLConnection) x.url.openConnection();
                        connectionMaster.connect();
                        InputStream inputMaster = connectionMaster.getInputStream();
                        Scanner scMaster = new Scanner(inputMaster).useDelimiter("\n");
                        //omplim la llista de fragments buida
                        x.frags = parse(scMaster);
                        //desconnectem
                        connectionMaster.disconnect();
                        inputMaster.close();
                        scMaster.close();
                    }
                    //creem la intent
                    this.intent.putExtra("type", 0);
                    this.intent.putExtra("list", masterList);
                    break;
                case Constants.PLAYLIST:
                    //parse
                    ArrayList<FragmentModel> aux = parse(sc);
                    this.intent.putExtra("type", 1);
                    this.intent.putExtra("list", aux);

                    break;
                case Constants.OTHER:
                    //error
                    this.intent.putExtra("type", 2);
                    break;
            }

        } catch (Exception e) {
            return e.toString();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return "";
    }


    public FragmentModel getNextFragModel(Scanner sc) {
        FragmentModel frag = null;

        String time = "";
        String url = "";

        if (sc.hasNext()) {
            //canviem "time" fins que contingui l'etiqueta #EXTINF o #EXT-X-ENDLIST
            while ((!time.contains(Constants.Time)) && (!time.contains(Constants.EndList))) {
                time = sc.next();
            }
        }
        //si conté l'etiqueta de final de llista sortim retornant null
        if (time.contains(Constants.EndList)) return null;
        //treiem l'etiqueta #EXTINF i la coma del temps de reproducció
        time = time.substring(Constants.Time.length(), time.length() - 2);
        //el següent token es la url
        url = sc.next();
        //creem el fragment
        frag = new FragmentModel(new Float(time), url);

        return frag;
    }

    public ArrayList<FragmentModel> parse(Scanner sc) {
        ArrayList<FragmentModel> fragsList = new ArrayList<FragmentModel>();
        FragmentModel frag;
        //agafem tots el fragments i els afegim a la llista
        frag = getNextFragModel(sc);
        while (frag != null) {
            fragsList.add(frag);
            frag = getNextFragModel(sc);
        }

        return fragsList;
    }

    public ArrayList<MasterModel> parseMaster(Scanner sc) {
        ArrayList<MasterModel> masterList = new ArrayList<MasterModel>();
        MasterModel mastModel;
        while (sc.hasNext()) {
            String strInfo = sc.next();
            String url = sc.next();
            //agafem bandwidth i codecs de l'string d'informació
            Scanner strSc = new Scanner(strInfo).useDelimiter(",");
            strSc.next();
            //dues formes artístiques d'arribar al mateix lloc CA MACU
            int bw = new Integer(strSc.next().replaceAll(Constants.BW, ""));
            String codecs = strSc.next().split("=")[1];

            mastModel = new MasterModel(url, bw, codecs);

            masterList.add(mastModel);
            strSc.close();
        }
        return masterList;
    }


    public int typeList(Scanner sc) { //saber quin tipus de llista es
        int ret = 2;
        if (sc.hasNext()) {
            //saltem etiqueta #EXT3MU
            sc.next();
            sc.useDelimiter(":");
            //seguent etiqueta
            String intro = sc.next();
            if (intro.contains(Constants.MasterPlaylist)) ret = 0;
            else if (intro.contains("#EXT-X-TARGETDURATION")) ret = 1;

            sc.useDelimiter("\n");
        }
        return ret;
    }

    public void onPostExecute(String result) {
        super.onPostExecute(result);
        this.context.startActivity(this.intent);
    }

}
