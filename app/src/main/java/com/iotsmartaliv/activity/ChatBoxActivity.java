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
import com.iotsmartaliv.model.Chat;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.chatbot)
    RecyclerView chatbot;
    @BindView(R.id.endBtn)
    Button endBtn;
    @BindView(R.id.chatED)
    EditText chatED;
    private MessageListAdapter mMessageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final ArrayList<Chat> chats = new ArrayList<>();
        chats.add(new Chat("Hi!", "", true));
        chats.add(new Chat("", "Hello", false));
        chats.add(new Chat("How may I help you?", "", true));
        mMessageAdapter = new MessageListAdapter(this, chats);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // linearLayoutManager.setReverseLayout(true);
        chatbot.setLayoutManager(linearLayoutManager);
        chatbot.setAdapter(mMessageAdapter);
        endBtn.setOnClickListener(view -> {
            if (chatED.getText().toString().trim().length() > 0) {
                msg = chatED.getText().toString().trim();
                Chat obj = new Chat(msg, "", true);
                mMessageAdapter.addChat(obj);
                chatED.setText("");
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
