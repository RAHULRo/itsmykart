package com.example.shoping;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    WebView webview;
    Handler handler;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            webview = findViewById(R.id.webview);
            relativeLayout = findViewById(R.id.mainactivityid);

            handler = new Handler();
            final int delay = 1000;




        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(final WebView webView, final int errorCode, final String description, final String failingUrl) {
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }
                String sHtmlTemplate = "<html><head></head><body><img src=\"file:///android_asset/logo.png\" " +
                        "alt=\"Image\" style=\"display: block; margin-left: auto;margin-right: auto; padding-top:110px ;width: 100%;\"></body></html>";
                WebView wb = new WebView(MainActivity.this);

                wb.loadDataWithBaseURL(null, sHtmlTemplate, "text/html", "utf-8",null);
                setContentView(wb);

                final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Check your internet connection.");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(network()==true)
                        startActivity(getIntent());
                        else onReceivedError(webView, errorCode, description, failingUrl);

                    }
                });
                alertDialog.setCancelable(false);
                alertDialog.show();
                super.onReceivedError(webView, errorCode, description, failingUrl);
            }


        });



        webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setSupportMultipleWindows(true);



        webview.loadUrl("https://itsmykart.com");


    }


    @Override
    public void onBackPressed() {
        if(webview.canGoBack()){
            webview.goBack();
        }else{
            final AlertDialog.Builder builder  = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Do you want to exit?");
            builder.setCancelable(true);
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webview.saveState(outState);
    }

    private boolean network() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
