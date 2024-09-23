package com.iotsmartaliv.apiAndSocket.Feedback;


import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketManager {
    private static final String TAG = "SocketManager";
    private Socket socket;
    private static final String SOCKET_URL = "https://camera-test.aliv.com.sg:5000/";
    private ChatCallback chatCallback;

    public interface ChatCallback {
        void onMessageReceived(String message);
        void onError(String error);
    }

    public SocketManager(ChatCallback chatCallback) {
        this.chatCallback = chatCallback;
        try {
            socket = IO.socket(SOCKET_URL);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Error creating socket: " + e.getMessage());
        }
    }

    public void connect() {
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on("message_from_admin", onNewMessage); // Listener for messages from admin
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.connect();
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Connected to the socket server");
            if (chatCallback != null) {
                chatCallback.onMessageReceived("Connected to the server");
            }
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String message = args[0].toString();
            Log.d(TAG, "New message received: " + message);
            if (chatCallback != null) {
                chatCallback.onMessageReceived(message);
            }
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG, "Disconnected from the server");
            if (chatCallback != null) {
                chatCallback.onError("Disconnected from the server");
            }
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "Connection Error: " + args[0]);
            if (chatCallback != null) {
                chatCallback.onError("Connection Error: " + args[0]);
            }
        }
    };

    public void sendMessage(String appuserID, String feedbackID, String messageFrom, String chatMessage, String messageDocument) {
        try {
            JSONObject params = new JSONObject();
            params.put("appuser_ID", appuserID);
            params.put("feedback_ID", feedbackID);
            params.put("message_from", messageFrom);

            if (!chatMessage.isEmpty()) {
                params.put("chat_message", chatMessage);
            }

            if (!messageDocument.isEmpty()) {
                params.put("message_document", messageDocument);
            }

            socket.emit("message_from_user", params);
            Log.d(TAG, "Message sent: " + params.toString());
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
        }
    }

    public void disconnect() {
        socket.disconnect();
        socket.off();
    }
}
