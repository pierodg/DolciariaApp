package com.example.piero.dolciariaapp1.Fragments;



import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.piero.dolciariaapp1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdTab extends Fragment {
    private WebView mWebView;



    public ThirdTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_third_tab, container, false);


        mWebView = (WebView) v.findViewById(R.id.webView);
        myWebView();

        return v;
    }

    public void myWebView() { // visualizza la WebView della pagina facebook
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);



        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());


        mWebView.loadUrl("https://www.facebook.com/fairyvalentina90");
    }

}
