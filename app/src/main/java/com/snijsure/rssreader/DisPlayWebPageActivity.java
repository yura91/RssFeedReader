package com.snijsure.rssreader;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DisPlayWebPageActivity extends Activity {

    WebView webview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        Intent in = getIntent();
        String page_url = in.getStringExtra("page_url");

        webview = (WebView) findViewById(R.id.webpage);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(page_url);

        webview.setWebViewClient(new DisPlayWebPageActivityClient());
    }


    private class DisPlayWebPageActivityClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}
