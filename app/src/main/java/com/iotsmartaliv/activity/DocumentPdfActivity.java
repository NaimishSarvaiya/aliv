package com.iotsmartaliv.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.iotsmartaliv.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class DocumentPdfActivity extends AppCompatActivity {

    PDFView pdfView;
    ImageView imgBack,imgShare,imgDocImage;
    ProgressBar progressBar;
    ArrayList<String> mUrl = new ArrayList<>();
    String mPdfUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_pdf);

        pdfView = findViewById(R.id.pdfView);
        imgBack = findViewById(R.id.img_back);
        imgShare = findViewById(R.id.img_share);
        progressBar = findViewById(R.id.progress);
        imgDocImage = findViewById(R.id.img_doc);
        progressBar.setVisibility(View.GONE);


        mUrl = getIntent().getStringArrayListExtra("PDFUrl");/*getIntent().getExtras().getString("PDFUrl");*/

        for (String url : mUrl){
//            if (url.endsWith(".pdf")){
                mPdfUrl = url;
//            }
        }
        if (mPdfUrl.endsWith(".pdf")) {
            new RetrivePDFfromUrl().execute(mPdfUrl);
            imgDocImage.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
        }else {
            imgDocImage.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            Glide.with(this).load(mPdfUrl).into(imgDocImage);
        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    /*shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Aliv");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";*/
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mPdfUrl);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });


    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected InputStream doInBackground(String... strings) {

            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {

            OnLoadCompleteListener onLoadCompleteListener = nbPages -> progressBar.setVisibility(View.GONE);

            pdfView.fromStream(inputStream)
                    .defaultPage(0)
                    .onPageChange(null)
                    .enableAnnotationRendering(true)
                    .onLoad(onLoadCompleteListener)
                    .scrollHandle(new DefaultScrollHandle(DocumentPdfActivity.this))
                    .spacing(20)
                    .load();

        }
    }

}