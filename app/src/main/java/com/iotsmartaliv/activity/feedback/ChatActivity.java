package com.iotsmartaliv.activity.feedback;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.FeedbackChatAdapter;
import com.iotsmartaliv.model.feedback.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    RecyclerView rvChat;
    private FeedbackChatAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        rvChat = findViewById(R.id.rv_chat);
        messageList = new ArrayList<>();

        // Sample messages
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?I'm good, thanks! How about you? I'm good, thanks! How about you? I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?I'm good, thanks! How about you? I'm good, thanks! How about you? I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?I'm good, thanks! How about you? I'm good, thanks! How about you? I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?I'm good, thanks! How about you? I'm good, thanks! How about you? I'm good, thanks! How about you?", false));
        messageList.add(new Message("Hi there!", true));
        messageList.add(new Message("Hello!", false));
        messageList.add(new Message("How are you?", true));
        messageList.add(new Message("I'm good, thanks! How about you?", false));

        adapter = new FeedbackChatAdapter(this, messageList);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);
    }
}