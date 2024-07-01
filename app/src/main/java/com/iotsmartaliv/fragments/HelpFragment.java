package com.iotsmartaliv.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.ChatAdapt;
import com.iotsmartaliv.model.Chat;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment class is used for help .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */

public class HelpFragment extends Fragment {
    private static String[] sender = {"Hello", "How are you", "Bye"};
    private static String[] receiver = {"Hi", "I'm fine", "BBye"};
    ListView listView;
    Context context;
    String msg;
    EditText edit_msg;
    ImageView imageView;
    String hello = "hello";
    private ChatAdapt mAdapter, mAdapter1;
    private List<Chat> arr_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.help_fragment, container, false);
        listView = view.findViewById(R.id.chatbot);
        imageView = view.findViewById(R.id.fab_img);
        edit_msg = view.findViewById(R.id.edit_msg);
        arr_list = new ArrayList<>();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = edit_msg.getText().toString().trim();
                Chat obj = new Chat();
                obj.setSend(msg);
                arr_list.add(obj);
                mAdapter.notifyDataSetChanged();
                if (msg.equalsIgnoreCase("hello") || msg.equalsIgnoreCase("hi") || msg.equalsIgnoreCase("hii")) {
                    Chat obj1 = new Chat();
                    obj1.setReceive("Hi");
                    arr_list.add(obj1);
                    listView.setSelection(arr_list.size());
                } else if (msg.equalsIgnoreCase("How are you")) {
                    Chat obj1 = new Chat();
                    obj1.setReceive("Im fine");
                    arr_list.add(obj1);
                    listView.setSelection(arr_list.size());
                } else if (!msg.equalsIgnoreCase("hello")) {
                    Chat obj1 = new Chat();
                    obj1.setReceive("Not in my dictionary");
                    arr_list.add(obj1);
                    listView.setSelection(arr_list.size());
                } else if (!msg.equalsIgnoreCase("how are you")) {
                    Chat obj1 = new Chat();
                    obj1.setReceive("Not in my dictionary");
                    arr_list.add(obj1);
                    listView.setSelection(arr_list.size());
                }
                edit_msg.setText("");
            }
        });

//        for (int i = 0; i < sender.length; i++) {
//            Chat chat1 = new Chat();
//            chat1.setSend(sender[i]);
//            chat1.setReceive(receiver[i]);
////            arr_list.add(chat1);
////            arr_list.setReceive(receiver[i]);
//            chat_re.add(chat1);
//
//        }
//        for (int i = 0; i < receiver.length; i++) {
//            Chat chat2 = new Chat();
//            chat2.setReceive(receiver[i]);
//            chat_re.add(chat2);
//        }

        mAdapter = new ChatAdapt(getActivity(), arr_list);
        listView.setAdapter(mAdapter);
//        startActivity(new Intent(getActivity(), ChatBoxActivity.class));
//        ChatBotFragment fragment;
//        fragment = new ChatBotFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.framelayout, fragment).commit();
        return view;
    }
}