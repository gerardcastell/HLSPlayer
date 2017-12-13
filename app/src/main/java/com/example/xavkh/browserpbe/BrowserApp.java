package com.example.xavkh.browserpbe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.endsWith(".m3u8") || url.endsWith(".ts")) {
                            Intent intent = new Intent(BrowserApp.this, Player.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                            return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl("http://www.google.es/");



        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url1 = url.getText().toString();
                        if (!url1.contains("http://")) webView.loadUrl("http://" + url1);
                        webView.loadUrl(url1);
                    }
                }
        );

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
