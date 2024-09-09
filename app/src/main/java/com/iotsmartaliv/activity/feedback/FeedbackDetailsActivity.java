package com.iotsmartaliv.activity.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.FeedbackChatAdapter;
import com.iotsmartaliv.model.feedback.Message;

import java.util.ArrayList;
import java.util.List;

public class FeedbackDetailsActivity extends AppCompatActivity {

    RecyclerView rvChatHistory;
    private FeedbackChatAdapter adapter;
    private List<Message> messageList;
    RelativeLayout rl_retusOrChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);
        rvChatHistory = findViewById(R.id.rv_chatHistory);
        rl_retusOrChat = findViewById(R.id.rl_retusOrChat);
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

        adapter = new FeedbackChatAdapter(this, messageList);
        rvChatHistory.setLayoutManager(new LinearLayoutManager(this));
        rvChatHistory.setAdapter(adapter);
        rl_retusOrChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FeedbackDetailsActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
    }
}