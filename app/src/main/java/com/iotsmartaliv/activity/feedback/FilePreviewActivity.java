package com.iotsmartaliv.activity.feedback;

import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityFilePreviewBinding;

public class FilePreviewActivity extends AppCompatActivity {

    ActivityFilePreviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent().getStringExtra(Constant.PATH).equals(Constant.FROM_CREATE_FEEDBACK)) {
            createFeedFilePreview();
        }else {
            feedDetailsFilePreview();
        }
        binding.rlBackCreateFeedBack.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    void createFeedFilePreview(){
        Uri fileUri = getIntent().getParcelableExtra(Constant.FILE_URI);
        String mimeType = getIntent().getStringExtra(Constant.MIME_TYPE);
        if (mimeType != null) {
            if (mimeType.startsWith("image/")) {
                // Show the image preview using Glide
                binding.pdfPreview.setVisibility(View.GONE); // Hide WebView if displaying an image
                binding.imgPreview.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(fileUri)
                        .override(800, 800) // Resize the image for preview
                        .placeholder(R.drawable.add_doc_feedback) // Placeholder while loading
                        .into(binding.imgPreview);
            } else if (mimeType.equals("application/pdf")) {
                // Show the PDF preview using WebView
                binding.imgPreview.setVisibility(View.GONE); // Hide ImageView if displaying a PDF
                binding.pdfPreview.setVisibility(View.VISIBLE);
                binding.pdfPreview.fromUri(fileUri)
                        .enableSwipe(true) // Allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .load();
//                binding.pdfPreview.setWebViewClient(new WebViewClient());
//                binding.pdfPreview.getSettings().setJavaScriptEnabled(true);
//                String pdfUrl = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + fileUri.toString();
//                binding.pdfPreview.loadUrl(pdfUrl);
            } else {
                Toast.makeText(this, "Unsupported file type", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Unable to determine file type", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    void feedDetailsFilePreview() {
        String fileUri = getIntent().getStringExtra(Constant.FILE_URI);

        if (fileUri.endsWith(".pdf")) {
            binding.imgPreview.setVisibility(View.GONE);
            binding.pdfPreview.setVisibility(View.GONE);
            binding.webPreview.setVisibility(View.VISIBLE);

            displayPdfInWebView(fileUri);
        } else if (fileUri.endsWith(".jpg") || fileUri.endsWith(".png") || fileUri.endsWith(".jpeg")) {
            binding.pdfPreview.setVisibility(View.GONE);
            binding.webPreview.setVisibility(View.GONE);
            binding.imgPreview.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(fileUri)
                    .into(binding.imgPreview);
        } else {
            binding.imgPreview.setVisibility(View.GONE);
            binding.pdfPreview.setVisibility(View.GONE);
            binding.webPreview.setVisibility(View.VISIBLE);

            displayPdfInWebView(fileUri);
        }
    }
    private void displayPdfInWebView(String pdfUrl) {
        // Show the loader initially

        showLoader(this);

        // Using Google Docs Viewer to display the PDF in WebView
        String googleDocsUrl = "https://docs.google.com/viewer?embedded=true&url=" + pdfUrl;

        binding.webPreview.getSettings().setJavaScriptEnabled(true);
        binding.webPreview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                // Hide the loader when the page is fully loaded
                hideLoader();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideLoader();
                Toast.makeText(getApplicationContext(), "Failed to load PDF. Please check your internet connection or try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.webPreview.loadUrl(googleDocsUrl);
    }
}