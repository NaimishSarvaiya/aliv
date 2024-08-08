package com.iotsmartaliv.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.MessageListAdapter;
import com.iotsmartaliv.databinding.ActivityChatBoxBinding;
import com.iotsmartaliv.model.Chat;

import java.util.ArrayList;


/**
 * This activity class is used for chat box activity.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ChatBoxActivity extends AppCompatActivity {
    private static String[] sender = {"Hello", "How are you", "Bye"};
    private static String[] receiver = {"Hi", "I'm fine", "BBye"};
    Context context;
    String msg;

    private MessageListAdapter mMessageAdapter;
    ActivityChatBoxBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
//        ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ArrayList<Chat> chats = new ArrayList<>();
        chats.add(new Chat("Hi!", "", true));
        chats.add(new Chat("", "Hello", false));
        chats.add(new Chat("How may I help you?", "", true));
        mMessageAdapter = new MessageListAdapter(this, chats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // linearLayoutManager.setReverseLayout(true);
        binding.chatbot.setLayoutManager(linearLayoutManager);
        binding.chatbot.setAdapter(mMessageAdapter);
        binding.endBtn.setOnClickListener(view -> {
            if (binding.chatED.getText().toString().trim().length() > 0) {
                msg = binding.chatED.getText().toString().trim();
                Chat obj = new Chat(msg, "", true);
                mMessageAdapter.addChat(obj);
                binding.chatED.setText("");
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (msg.equalsIgnoreCase("hello")) {
                            Chat obj = new Chat("", "Hi", false);
                            mMessageAdapter.addChat(obj);
                        } else if (msg.equalsIgnoreCase("How are you")) {
                            Chat obj = new Chat("", "Im fine", false);
                            mMessageAdapter.addChat(obj);
                        } else {
                            Chat obj = new Chat("", "Sorry!", false);
                            mMessageAdapter.addChat(obj);
                        }

                        //Do something after 100ms
                    }
                }, 1000);

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
