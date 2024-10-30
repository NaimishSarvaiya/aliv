package com.iotsmartaliv.activity.booking;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.databinding.ActivityPaymentWebViewBinding;

public class PaymentWebView extends AppCompatActivity {
    ActivityPaymentWebViewBinding binding;
    String webUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using ViewBinding
        binding = ActivityPaymentWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the URL from the intent
        if (getIntent().getStringExtra("url") != null) {
            webUrl = getIntent().getStringExtra("url");
        }

        // Configure WebView settings
        WebSettings webSettings = binding.wvPayment.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);

        // Enable debugging for WebView (optional)
        WebView.setWebContentsDebuggingEnabled(true);

        // Set a WebViewClient to handle SSL errors and page load errors
        binding.wvPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // Ignore SSL certificate errors (only for testing)
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Handle page load finished event (e.g., hide loading indicator)
                Toast.makeText(PaymentWebView.this, "Page Loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle page load errors
                Toast.makeText(PaymentWebView.this, "Failed to load page: " + description, Toast.LENGTH_SHORT).show();
            }
        });

        // Load the URL in WebView
        if (!webUrl.isEmpty()) {
            binding.wvPayment.loadUrl(webUrl);
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
        }

        // Handle the back button click
        binding.imgBack.setOnClickListener(v -> {
            // Set the result code to RESULT_OK
            setResult(RESULT_OK);
            finish();
        });
    }
}
