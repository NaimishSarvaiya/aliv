package com.iotsmartaliv.activity.feedback;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.adapter.FeedbackCategoryDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.ResArrayObjectData;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.dialog_box.CustomCommunityDialog;
import com.iotsmartaliv.dialog_box.CustomFeedbackCategoryDialog;
import com.iotsmartaliv.model.feedback.AddFeedbackRequest;
import com.iotsmartaliv.model.feedback.AddFeedbackResponse;
import com.iotsmartaliv.model.feedback.FeedBackCategoryModel;
import com.iotsmartaliv.model.feedback.FeedbackCategoryData;
import com.iotsmartaliv.model.feedback.UploadFeedbackDocumentResponse;
import com.iotsmartaliv.utils.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateFeedbackActivity extends AppCompatActivity implements RetrofitListener<SuccessArrayResponse> {
    ApiServiceProvider apiServiceProvider;
    String communityID, categoryId;
    TextView tvCommunity, tvType;
    EditText etFeedbackTitle, etFeedbackDiscription;
    CustomCommunityDialog customCommunityDialog;
    CustomFeedbackCategoryDialog feedbackCategoryDialog;
    RelativeLayout rlSelectCommunity, rlSelectCategory, rlsendFeedBack, rlAddDocument;
    private Uri fileUri;
    ImageView imgDoc, img_back, img_preview;
    String docUrl = "";
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 1002;
    private static final int DOCUMENT_REQUEST_CODE = 1003;
    private static final int STORAGE_PERMISSION_CODE = 1004;
    String fileNameforUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feedback);
        init();

        // Request permissions on start
        requestStoragePermission();
    }

    void init() {
        tvCommunity = findViewById(R.id.communityId);
        tvType = findViewById(R.id.categoryId);
        rlSelectCommunity = findViewById(R.id.rl_select_community);
        rlSelectCategory = findViewById(R.id.rl_select_categry);
        rlsendFeedBack = findViewById(R.id.rl_sendFeedBack);
        etFeedbackTitle = findViewById(R.id.et_feedbackTitle);
        etFeedbackDiscription = findViewById(R.id.et_feedbackDiscription);
        rlAddDocument = findViewById(R.id.rl_add_document);
        img_back = findViewById(R.id.img_back);
        imgDoc = findViewById(R.id.img_doc);
        etFeedbackTitle.setFilters(new InputFilter[]{Util.filterEmoji});
        etFeedbackDiscription.setFilters(new InputFilter[]{Util.filterEmoji});
        apiServiceProvider = ApiServiceProvider.getInstance(this);

        rlSelectCommunity.setOnClickListener(v -> getCommunity());
        rlSelectCategory.setOnClickListener(v -> getFeedbackCategory());
        rlsendFeedBack.setOnClickListener(v -> sendFeedback());
        rlAddDocument.setOnClickListener(v -> showImagePickerOptions());
        imgDoc.setOnClickListener(v -> {
            documentPreview();
        });
        img_back.setOnClickListener(v -> onBackPressed());
    }

    private void documentPreview() {
        if (fileUri != null) {
            String mimeType = getContentResolver().getType(fileUri);

            if (mimeType != null) {
                // Start the PreviewActivity with the file URI and MIME type
                Intent intent = new Intent(this, FilePreviewActivity.class);
                intent.putExtra(Constant.FILE_URI, fileUri);
                intent.putExtra(Constant.MIME_TYPE, mimeType);
                intent.putExtra(Constant.PATH, Constant.FROM_CREATE_FEEDBACK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Unable to determine file type", Toast.LENGTH_SHORT).show();
            }
//            previewSelectedFile(fileUri);
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
//                Toast.makeText(this, "Permission denied to read storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImagePickerOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        String[] options = {"Camera", "Gallery", "Documents"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera();
                    break;
                case 1:
                    openGallery();
                    break;
                case 2:
                    openDocuments();
                    break;
            }
        });
        builder.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), GALLERY_REQUEST_CODE);
    }

    private void openDocuments() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select File"), DOCUMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                handleCameraImage(photo);
            } else if (requestCode == GALLERY_REQUEST_CODE || requestCode == DOCUMENT_REQUEST_CODE) {
                fileUri = data.getData();
                handleSelectedFile(fileUri);
            }
        }
    }

    private void handleCameraImage(Bitmap photo) {
        Uri tempUri = Util.getImageUri(this, photo);
        fileUri = tempUri;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        fileNameforUpload = "IMG_" + timeStamp + ".png"; // Example: IMG_20240912_143200.png
        // Display the image in the ImageView using Glide
        Glide.with(this)
                .load(tempUri)
                .placeholder(R.drawable.attached_file)
                .into(imgDoc);
//        uploadFileToServer("camera_image.png");
    }

    private void handleSelectedFile(Uri fileUri) {
        String fileName = getFileNameFromUri(fileUri);
        if (fileName != null) {
            fileNameforUpload = fileName;
//            uploadFileToServer(fileName);
        } else {
            fileNameforUpload = "selected_file";
//            uploadFileToServer("selected_file");
        }
        String mimeType = getContentResolver().getType(fileUri);
        if (mimeType != null && mimeType.startsWith("image/")) {
            // Display the image in the ImageView using Glide
            Glide.with(this)
                    .load(fileUri)
                    .placeholder(R.drawable.attached_file)
                    .into(imgDoc);
        } else {
            // For documents, display a generic file icon
            imgDoc.setImageResource(R.drawable.attached_file); // Use your placeholder for documents
        }

    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) { // Ensure the index is valid
                        result = cursor.getString(nameIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void uploadFileToServer() {
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            File file = createTempFileFromUri(this, fileUri);

            if (file != null) {
                apiServiceProvider.uploadFeedbackDocument(fileNameforUpload, file, new RetrofitListener<UploadFeedbackDocumentResponse>() {
                    @Override
                    public void onResponseSuccess(UploadFeedbackDocumentResponse responseBody, String apiFlag) {
                        String imageUrl = responseBody.getFilePath();
                        docUrl = imageUrl;
                        apiCallForFeedback();
//                        Glide.with(CreateFeedbackActivity.this)
//                                .load(imageUrl)
//                                .override(300, 300)
//                                .placeholder(R.drawable.add_doc_feedback)
//                                .into(imgDoc);
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Toast.makeText(CreateFeedbackActivity.this, "File upload failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }

    public static File createTempFileFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File tempFile = File.createTempFile("tempFile", null, context.getCacheDir());
        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }

    private void apiCallForFeedback() {

        AddFeedbackRequest request = new AddFeedbackRequest(
                LOGIN_DETAIL.getAppuserID(),
                communityID,
                categoryId,
                etFeedbackTitle.getText().toString().trim(),
                etFeedbackDiscription.getText().toString().trim(),
                docUrl
        );

        apiServiceProvider.addFeedback(request, new RetrofitListener<AddFeedbackResponse>() {
            @Override
            public void onResponseSuccess(AddFeedbackResponse successResponse, String apiFlag) {
                showSuccessfullDailog("Thank you for your feedback! You can track the status of your feedback and send additional message if needed");
//                    Toast.makeText(CreateFeedbackActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                Toast.makeText(CreateFeedbackActivity.this, "Error sending feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendFeedback() {
        if (communityID == null) {
            Toast.makeText(this, "Please Select Community", Toast.LENGTH_SHORT).show();
        } else if (categoryId == null) {
            Toast.makeText(this, "Please select a feedback type", Toast.LENGTH_SHORT).show();
        } else if (etFeedbackTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please provide a title for your feedback", Toast.LENGTH_SHORT).show();
        } else if (etFeedbackDiscription.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please provide a description", Toast.LENGTH_SHORT).show();
        } else {
            if (fileUri != null) {
                uploadFileToServer();
            } else {
                apiCallForFeedback();
            }
        }
    }

    void getCommunity() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.callForListOfCommunity(LOGIN_DETAIL.getAppuserID(), CreateFeedbackActivity.this);
                } else {
                    hideLoader();
                }
            }
        });
    }

    void getFeedbackCategory() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getFeedbackCategory(new RetrofitListener<FeedBackCategoryModel>() {
                        @Override
                        public void onResponseSuccess(FeedBackCategoryModel successResponse, String apiFlag) {
                            List<FeedbackCategoryData> categoryData = successResponse.getData();
                            if (categoryData != null && !categoryData.isEmpty()) {
                                if (categoryData.size() == 1){
                                    categoryId = categoryData.get(0).getId();
                                    tvType.setText(categoryData.get(0).getCatName());
                                }else {
                                    FeedbackCategoryDialogAdapter dataAdapter = new FeedbackCategoryDialogAdapter(categoryData, data -> {
                                        categoryId = data.getId();
                                        tvType.setText(data.getCatName());
                                        feedbackCategoryDialog.dismiss();
                                    });
                                    feedbackCategoryDialog = new CustomFeedbackCategoryDialog(CreateFeedbackActivity.this, dataAdapter, categoryData);
                                    feedbackCategoryDialog.setCanceledOnTouchOutside(false);
                                    feedbackCategoryDialog.show();
                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            // Handle error
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onResponseSuccess(SuccessArrayResponse successArrayResponse, String apiFlag) {
        if (Constant.UrlPath.COMMUNITY_LIST_API.equals(apiFlag) && "OK".equalsIgnoreCase(successArrayResponse.getStatus())) {
            List<ResArrayObjectData> communityData = successArrayResponse.getData();
            if (communityData != null && !communityData.isEmpty()) {
                List<ResArrayObjectData> filteredData = new ArrayList<>();
                for (ResArrayObjectData data : communityData) {
                    if ("1".equalsIgnoreCase(data.getBookingmanagement().toString())) {
                        filteredData.add(data);
                    }
                }
                if (filteredData.isEmpty()) {
                    Toast.makeText(this, "Please Contact Administrator for booking System", Toast.LENGTH_SHORT).show();
                } else {
                    if (filteredData.size() == 1) {
                        communityID = filteredData.get(0).getCommunityID();
                        tvCommunity.setText(filteredData.get(0).getCommunityName());
                    } else {
                        CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(filteredData, data -> {
                            communityID = data.getCommunityID();
                            tvCommunity.setText(data.getCommunityName());
                            customCommunityDialog.dismiss();
                        });
                        customCommunityDialog = new CustomCommunityDialog(this, dataAdapter, filteredData);
                        customCommunityDialog.setCanceledOnTouchOutside(false);
                        customCommunityDialog.show();
                    }
                }
            } else {
                Toast.makeText(this, "Please join the Community.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        if (Constant.UrlPath.COMMUNITY_LIST_API.equals(apiFlag)) {
            Util.firebaseEvent(Constant.APIERROR, this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void showSuccessfullDailog(String msg) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText(msg);
        rlOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            Intent returnIntent = new Intent();
            setResult(10, returnIntent);
            finish();
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }
}
