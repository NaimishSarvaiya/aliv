package com.iotsmartaliv.activity.feedback;

import static com.iotsmartaliv.constants.Constant.LOGIN;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.adapter.FeedbackChatAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityChatBinding;
import com.iotsmartaliv.model.feedback.AddFeedbackRequest;
import com.iotsmartaliv.model.feedback.MessageHistory;
import com.iotsmartaliv.model.feedback.MessageHistoryData;
import com.iotsmartaliv.model.feedback.MessageStatusResponse;
import com.iotsmartaliv.model.feedback.MessageStatusUpdateRequest;
import com.iotsmartaliv.model.feedback.UploadFeedbackDocumentResponse;
import com.iotsmartaliv.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

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

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {
    private FeedbackChatAdapter adapter;
    private List<MessageHistoryData> messageList = new ArrayList<>();
    ActivityChatBinding binding;
    private Socket socket;
    String feedbackId;
    private ApiServiceProvider apiServiceProvider;
    private boolean isLoading = false;
    private int page = 1;
    private String limit = "20";
    int totalRecords = 0;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private static final int GALLERY_REQUEST_CODE = 1002;
    private static final int DOCUMENT_REQUEST_CODE = 1003;
    private static final int STORAGE_PERMISSION_CODE = 1004;
    private Uri fileUri;
    String fileNameforUpload;
    String docUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       if (getIntent().getStringExtra("limit")!=null && !getIntent().getStringExtra("limit").equals("")){
          limit =  getIntent().getStringExtra("limit");
       }
        if (getIntent().getExtras().getString("feedback_ID") != null) {
            feedbackId = getIntent().getStringExtra("feedback_ID");
        } else {
            String data = getIntent().getExtras().get("data").toString();
            // Convert the string to a JSONObject
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(data);
                feedbackId = jsonObject.getString("feedback_ID");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            // Retrieve the values using the keys
        }

        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        setupRecyclerView();
        getChatHistory();
        setupSocket(); // Initialize Socket.IO connection
        binding.tvChatIdheader.setText("#" + feedbackId);

        binding.btnSendMessage.setOnClickListener(v -> {
            String message = binding.etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(Util.encodeChatString(message));
            }
        });

//        binding.rvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!isLoading && !recyclerView.canScrollVertically(-1)) {
//                    if (messageList.size() < totalRecords) {
//                        loadMoreMessages();
//                    }
//                }
//            }
//        });

        binding.imgSendDoc.setOnClickListener(v -> showImagePickerOptions());
        binding.imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void setupSocket() {
        try {
            socket = IO.socket("https://camera-test.aliv.com.sg:5000/");
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Constant.MESSAGE_FROM_ADMIN, onMessageReceived); // Listener for messages from admin
            socket.on(Constant.STATUS_CHANGED_FOR_USER, onMessageStatusChange); // Listener for messages status change from admin
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.connect();
            sendMessageStatuse();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Socket connection error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private final Emitter.Listener onConnect = args -> runOnUiThread(() -> {
//        Toast.makeText(this, "Connected to server", Toast.LENGTH_SHORT).show();
    });

    private final Emitter.Listener onMessageReceived = args -> runOnUiThread(() -> {
        String message = args[0].toString();
        handleIncomingMessage(message);
        sendMessageStatuse();
    });

    private final Emitter.Listener onMessageStatusChange = args -> runOnUiThread(() -> {
//        String message = args[0].toString();
        for (MessageHistoryData message : messageList) {
            message.setRead_status("1");
        }
        adapter.updateData(messageList);
    });


    private final Emitter.Listener onDisconnect = args -> runOnUiThread(() -> {
        Toast.makeText(this, "Disconnected from server", Toast.LENGTH_SHORT).show();
    });

    private final Emitter.Listener onConnectError = args -> runOnUiThread(() -> {
        Toast.makeText(this, "Connection Error: " + args[0], Toast.LENGTH_SHORT).show();
    });

    private void handleIncomingMessage(String messageJson) {

        try {
            // Parse the incoming JSON data
            JSONObject jsonObject = new JSONObject(messageJson);
            JSONObject dataObject = new JSONObject();
            // Extract the "data" object from the JSON
            if (jsonObject.has("data")) {
                dataObject = jsonObject.optJSONObject("data");
            }
            if (dataObject.has("feedback_ID")) {
                // Now, extract fields from the "data" object
                String feedbackId = dataObject.optString("feedback_ID");
                String appuserId = dataObject.optString("appuser_ID");
                String message = dataObject.optString("chat_message");
                String document = dataObject.optString("message_document");
                int messageFrom = dataObject.optInt("message_from");

                // Get current time as the message created time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String createdAt = dateFormat.format(new Date());

                // Create a new message object using the received data
                MessageHistoryData newMessage = new MessageHistoryData(message, document, "0", createdAt, String.valueOf(messageFrom));

                // Add the new message at the beginning of the list
                messageList.add(newMessage);
                List<MessageHistoryData> newMessages = new ArrayList<>();
                newMessages.add(newMessage);

                // Add the new message to the adapter
                adapter.addMessage(newMessages);

                // Optionally scroll to the top to show the new message
                binding.rvChat.scrollToPosition(0);
            } else if (jsonObject != null) {
                // Now, extract fields from the "data" object
                String feedbackId = jsonObject.optString("feedback_ID");
                String appuserId = jsonObject.optString("appuser_ID");
                String message = jsonObject.optString("chat_message");
                String document = jsonObject.optString("message_document");
                int messageFrom = jsonObject.optInt("message_from");

                // Get current time as the message created time
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String createdAt = dateFormat.format(new Date());

                // Create a new message object using the received data
                MessageHistoryData newMessage = new MessageHistoryData(message, document, "0", createdAt, String.valueOf(messageFrom));

                // Add the new message at the beginning of the list
                messageList.add(newMessage);
                List<MessageHistoryData> newMessages = new ArrayList<>();
                newMessages.add(newMessage);

                // Add the new message to the adapter
                adapter.addMessage(newMessages);

                // Optionally scroll to the top to show the new message
                binding.rvChat.scrollToPosition(0);
            } else {
                Toast.makeText(this, "Invalid data received", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse incoming message", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendMessage(String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        // Emit the message to the server
        JSONObject params = new JSONObject();
        try {
            params.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
            params.put("feedback_ID", feedbackId);
            params.put("message_from", "1");
            params.put("chat_message", message);
            params.put("message_document", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(Constant.MESSAGE_FROM_USER, params);
        binding.etMessage.setText("");
        handleIncomingMessage(params.toString());
    }

    private void sendDoc(String url) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        String formattedDate = dateFormat.format(currentDate);

        // Emit the document message to the server
        JSONObject params = new JSONObject();
        try {
            params.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
            params.put("feedback_ID", feedbackId);
            params.put("message_from", "1");
            params.put("chat_message", "");
            params.put("message_document", url);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        socket.emit(Constant.MESSAGE_FROM_USER, params);
        handleIncomingMessage(params.toString());
    }

    private void sendMessageStatuse() {
        // Emit the document message to the server
        JSONObject params = new JSONObject();
        try {
            params.put("feedback_ID", feedbackId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit(Constant.STATUS_CHANGED_FOR_ADMIN, params);
        updateFeedbackStatus();
//        handleIncomingMessage(params.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    private void getChatHistory() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getFeedbackMessages(feedbackId, LOGIN_DETAIL.getAppuserID(), String.valueOf(page), limit, new RetrofitListener<MessageHistory>() {
                        @Override
                        public void onResponseSuccess(MessageHistory sucessRespnse, String apiFlag) {
                            isLoading = false;
                            List<MessageHistoryData> newMessages = sucessRespnse.getData();
                            totalRecords = Integer.parseInt(sucessRespnse.getTotalRecords());
                            if (newMessages != null && !newMessages.isEmpty()) {
                                messageList.addAll(newMessages);
                                adapter.updateData(newMessages);
                                binding.rvChat.smoothScrollToPosition(0);
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            isLoading = false;
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    private void updateFeedbackStatus() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    MessageStatusUpdateRequest request = new MessageStatusUpdateRequest(
                            LOGIN_DETAIL.getAppuserID(),
                            feedbackId
                    );
                    apiServiceProvider.callFeedbackMessageStatus(request, new RetrofitListener<MessageStatusResponse>() {
                        @Override
                        public void onResponseSuccess(MessageStatusResponse sucessRespnse, String apiFlag) {
//                            if (sucessRespnse.getStatusCode().equalsIgnoreCase("OK")) {
//                                Toast.makeText(ChatActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
//                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(ChatActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(ChatActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    private void loadMoreMessages() {
        isLoading = true;
        page++;
        getChatHistory();
    }

    private void setupRecyclerView() {
        binding.rvChat.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(layoutManager);
        adapter = new FeedbackChatAdapter(this, messageList);
        binding.rvChat.setAdapter(adapter);
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
                        sendDoc(imageUrl);
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Toast.makeText(ChatActivity.this, "File upload failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        uploadFileToServer();
    }

    private void handleSelectedFile(Uri fileUri) {
        String fileName = getFileNameFromUri(fileUri);
        if (fileName != null) {
            fileNameforUpload = fileName;
        } else {
            fileNameforUpload = "selected_file";
        }
        uploadFileToServer();
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
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
}
