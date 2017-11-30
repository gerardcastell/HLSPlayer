package com.example.xavkh.browserpbe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


public class BrowserApp extends AppCompatActivity {

    EditText url;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_app);
        url = findViewById(R.id.edittext);
        Button mButton = (Button) findViewById(R.id.button);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("http://www.google.es/");

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.loadUrl(url.getText().toString());
                    }
                }
        );
        webView.setOnTouchListener(
                new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        String url = webView.getUrl();
                        TextView tx = (TextView) findViewById(R.id.textView);
                        tx.setText(url);
                        if(url.endsWith(".m3u8") || url.endsWith(".ts")) {
                            Intent intent = new Intent(BrowserApp.this, Reproductor.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                        return false;
                    }
                }
        );
        webView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = webView.getUrl();
                        TextView tx = (TextView) findViewById(R.id.textView);
                        tx.setText(url);
                        //if(url.endsWith(".m3u8") || url.endsWith(".ts")) {
                            Intent intent = new Intent(BrowserApp.this, Reproductor.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        //}
                    }
                }
        );
    }
}
