package com.iotsmartaliv.activity.feedback;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CommunityDialogAdapter;
import com.iotsmartaliv.adapter.FeedbackCategoryDialogAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.ResArrayObjectData;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
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
import java.util.ArrayList;
import java.util.List;

public class CreateFeedbackActivity extends AppCompatActivity implements RetrofitListener<SuccessArrayResponse> {
    ApiServiceProvider apiServiceProvider;
    String communityID, categoryId;
    TextView tvCommunity, tvType;
    EditText etFeedbackTitle, etFeedbackDiscription;
    CustomCommunityDialog customCommunityDialog;

    CustomFeedbackCategoryDialog feedbackCategoryDialog;
    RelativeLayout rlSelectCommunity, rlSelectCategory, rlsendFeedBack, rlAddDocument;
    private Uri fileUri;

    ImageView imgDoc;

    String docUrl = "";
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 1002;
    private static final int DOCUMENT_REQUEST_CODE = 1003;
    private static final int PICK_FILE_REQUEST = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feedback);
        init();

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
        imgDoc = findViewById(R.id.img_doc);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        rlSelectCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCommunity();
            }
        });

        rlSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFeedbackCategory();
            }
        });

        rlsendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

        rlAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickerOptions();
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
            ApiServiceProvider apiServiceProvider = ApiServiceProvider.getInstance(this);

            AddFeedbackRequest request = new AddFeedbackRequest(
                    LOGIN_DETAIL.getAppuserID(), // appuser_id
                    communityID,    // comm_id
                    categoryId,      // cat_id
                    etFeedbackTitle.getText().toString().trim(), // feedback_title
                    etFeedbackDiscription.getText().toString().trim(), // feedback_description
                    docUrl // feedback_doc_fullpath
            );

            apiServiceProvider.addFeedback(request, new RetrofitListener<AddFeedbackResponse>() {
                @Override
                public void onResponseSuccess(AddFeedbackResponse successResponse, String apiFlag) {
                    Toast.makeText(CreateFeedbackActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    // Handle success response
                }

                @Override
                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                    // Handle error response
                }
            });
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
                        public void onResponseSuccess(FeedBackCategoryModel sucessRespnse, String apiFlag) {
                            List<FeedbackCategoryData> categoryData = new ArrayList<>();
                            if (sucessRespnse.getStatusCode() == 200) {
                                categoryData = sucessRespnse.getData();
                                if (categoryData != null && categoryData.size() != 0) {
                                    FeedbackCategoryDialogAdapter dataAdapter = new FeedbackCategoryDialogAdapter(categoryData, data -> {
                                        categoryId = data.getId();
                                        tvType.setText(data.getCatName());
                                        feedbackCategoryDialog.dismiss();
                                    });
                                    feedbackCategoryDialog = new CustomFeedbackCategoryDialog(CreateFeedbackActivity.this, dataAdapter, categoryData);
                                    feedbackCategoryDialog.setCanceledOnTouchOutside(false);
                                    if (feedbackCategoryDialog != null) {
                                        feedbackCategoryDialog.show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

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
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_LIST_API:
                if (successArrayResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (successArrayResponse.getData().size() > 0) {
                        List<ResArrayObjectData> mDataseta = new ArrayList<>();
                        for (int i = 0; i < successArrayResponse.getData().size(); i++) {
                            Log.e("Booking Authority", successArrayResponse.getData().get(i).getBookingmanagement().toString());
                            if (successArrayResponse.getData().get(i).getBookingmanagement().toString().equalsIgnoreCase("1")) {
                                mDataseta.add(successArrayResponse.getData().get(i));
                            }
                        }
                        if (mDataseta == null || mDataseta.size() == 0) {
                            Toast.makeText(CreateFeedbackActivity.this, "Please Contact Administrator for booking System", Toast.LENGTH_SHORT).show();
                        } else {
                            CommunityDialogAdapter dataAdapter = new CommunityDialogAdapter(mDataseta, data -> {
                                communityID = data.getCommunityID();
                                tvCommunity.setText(data.getCommunityName());
                                customCommunityDialog.dismiss();
                            });
                            customCommunityDialog = new CustomCommunityDialog(CreateFeedbackActivity.this, dataAdapter, mDataseta);
                            customCommunityDialog.setCanceledOnTouchOutside(false);
                            if (customCommunityDialog != null) {
                                customCommunityDialog.show();
                            }
                        }
                    } else {
//                                    getActivity().finish();
                        Toast.makeText(CreateFeedbackActivity.this, "Please join the Community.", Toast.LENGTH_LONG).show();
                    }
                } else {
//                                getActivity().finish();
                    Toast.makeText(CreateFeedbackActivity.this, successArrayResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.COMMUNITY_LIST_API:
                Util.firebaseEvent(Constant.APIERROR, CreateFeedbackActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                try {
                    Toast.makeText(CreateFeedbackActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CreateFeedbackActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Method to open file picker
//    private void openFilePicker() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*"); // Allows any file type to be selected
//        String[] mimeTypes = {"image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_FILE_REQUEST);
//    }

    // Handle the selected file
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            fileUri = data.getData();
//            String fileName = "test.png"; // Replace with your desired file name
//            uploadFileToServer(fileName, fileUri);
//        }
//    }

//    private void uploadFileToServer(String fileName, Uri fileUri) {
//        String filePath = Util.getPathFromUri(this, fileUri); // Ensure correct context is passed
//        if (filePath != null) {
//            File file = new File(filePath);
//            apiServiceProvider.uploadFeedbackDocument(fileName, file, new RetrofitListener<UploadFeedbackDocumentResponse>() {
//                @Override
//                public void onResponseSuccess(UploadFeedbackDocumentResponse responseBody, String apiFlag) {
//                    // Handle successful upload response
//                    Toast.makeText(CreateFeedbackActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
//                    String imageUrl = responseBody.getFilePath();// Construct the full URL
//                    docUrl = imageUrl;
//                    Log.e("DocUrl", imageUrl);
//                    // Use Glide to load and compress the image for display
//                    Glide.with(CreateFeedbackActivity.this)
//                            .load(imageUrl)
//                            .override(300, 300) // Resize the image to 300x300 pixels
//                            .placeholder(R.drawable.add_doc_feedback) // Optional: placeholder while loading
////                            .error(R.drawable.error) // Optional: error image if loading fails
//                            .into(imgDoc);
////                    Glide.with(CreateFeedbackActivity.this).load(imageUrl).into(imgDoc); // Use Glide or another image loading library
//
//                }
//
//                @Override
//                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                    // Handle error response
//                    Toast.makeText(CreateFeedbackActivity.this, "File upload failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        } else {
//            Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void showImagePickerOptions() {
        // Create and display a dialog for selecting image
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image");
        String[] options = {"Camera", "Gallery", "Documents"};
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openCamera(); // Open camera
                    break;
                case 1:
                    openGallery(); // Open gallery
                    break;
                case 2:
                    openDocuments(); // Open documents
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
                handleCameraImage(photo); // Handle camera image
            } else if (requestCode == GALLERY_REQUEST_CODE || requestCode == DOCUMENT_REQUEST_CODE) {
                fileUri = data.getData();
                handleSelectedFile(fileUri); // Handle gallery or document selection
            }
        }
    }

    private void handleCameraImage(Bitmap photo) {
        // Convert Bitmap to Uri and upload it
        Uri tempUri = Util.getImageUri(this, photo);
        String filePath = Util.getPathFromUri(this, tempUri);
        uploadFileToServer("camera_image.png", tempUri);
    }

    private void handleSelectedFile(Uri fileUri) {
        String fileName = "selected_file"; // Replace with your desired file name
        uploadFileToServer(fileName, fileUri);
    }
    private void uploadFileToServer(String fileName, Uri fileUri) {
        String filePath = Util.getPathFromUri(this, fileUri);
        if (filePath != null) {
            File file = new File(filePath);
            apiServiceProvider.uploadFeedbackDocument(fileName, file, new RetrofitListener<UploadFeedbackDocumentResponse>() {
                @Override
                public void onResponseSuccess(UploadFeedbackDocumentResponse responseBody, String apiFlag) {
                    Toast.makeText(CreateFeedbackActivity.this, "File uploaded successfully", Toast.LENGTH_SHORT).show();
                    String imageUrl = responseBody.getFilePath();
                    docUrl = imageUrl;
                    Log.e("DocUrl", imageUrl);
                    Glide.with(CreateFeedbackActivity.this)
                            .load(imageUrl)
                            .override(300, 300)
                            .placeholder(R.drawable.add_doc_feedback)
                            .into(imgDoc);
                }

                @Override
                public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                    Toast.makeText(CreateFeedbackActivity.this, "File upload failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Failed to get file path", Toast.LENGTH_SHORT).show();
        }
    }


}