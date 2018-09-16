package com.example.xavkh.browserpbe;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;


import java.net.MalformedURLException;
import java.net.URL;


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
                if(url.endsWith(".m3u8")) {
                    try {
                        new Handler_m3u8_Task(getApplicationContext()).execute(new URL(url));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
                if(url.endsWith(".ts")){
                    Intent intent = new Intent(getApplicationContext(), Reproductor.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);

        mButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.loadUrl(filterUrl(url.getText().toString()));
                        hideKeyboard();
                    }
                }
        );

        url.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if(keycode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    webView.loadUrl(filterUrl(url.getText().toString()));
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });


    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public String filterUrl (String url) {
        if (!url.contains("http://")) return "http://" + url;
        else return url;
    }
}
