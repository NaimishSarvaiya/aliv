package com.iotsmartaliv.modules.cardManager;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentUserListBinding;

import java.util.ArrayList;
import java.util.List;


//import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment implements RetrofitListener<CardUserListModel> {
    ApiServiceProvider apiServiceProvider;
    List<CardUserList> cardUserLists = new ArrayList<>();

    String deviceId, communityId;
    public UserListFragment() {
        // Required empty public constructor
    }
    FragmentUserListBinding binding;
    UserListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserListBinding.inflate(inflater,container,false);
        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

       /* CardModel[] myListData = new CardModel[]{
                new CardModel("Ravi Thakur", "ravi@maili.com", 0),
                new CardModel("Akash Gupta", "aksh.gupta.com", 0),
                new CardModel("Rajesh Jain", "Rajesh@jain.com", 1),
                new CardModel("Isha Sharma", "isha@ragav.com", 1),
                new CardModel("Rohit Sharma", "sharam01@gmail.com", 0),
                new CardModel("Manish Pandy", "Pandy@hotmail.com", 1),
                new CardModel("Arti Agrawal", "arti@gmail.com", 1),
                new CardModel("Deepika gupta", "Depika@gmail.com", 0),
                new CardModel("Jon N", "jon@gmail.com", 1),
                new CardModel("Boris Chan", "borish@mail.com", 1)

        };

        UserListAdapter adapter = new UserListAdapter(myListData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);*/
        if (getArguments() != null) {
            deviceId = getArguments().getString(Constant.DEVICE_ID);
            communityId = getArguments().getString(Constant.COMMUNITY_ID);
        }
         adapter = new UserListAdapter(cardUserLists);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        apiServiceProvider = ApiServiceProvider.getInstance(getContext(),false);
        apiServiceProvider.callForCardUserList(communityId, deviceId, LOGIN_DETAIL.getAppuserID(), this);
        binding.searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        return binding.getRoot();
    }
    private void filter(String text) {
        List<CardUserList> card = new ArrayList<>();
        for (CardUserList d : cardUserLists) {

            if (!d.getUserFullName().isEmpty() || !d.getUidNumber().isEmpty() || !d.getUserEmail().isEmpty()){
                if (d.getUserFullName().toLowerCase().contains(text.toLowerCase()) || d.getUidNumber().toLowerCase().contains(text.toLowerCase()) || d.getUserEmail().toLowerCase().contains(text.toLowerCase())) {
                    card.add(d);
                }
            }else {
//                if (d.getDeviceName().toLowerCase().contains(text.toLowerCase()) ) {
//                    device.add(d);
//                }
            }
        }
        adapter.updateList(card);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

    @Override
    public void onResponseSuccess(CardUserListModel sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.GET_CARD_USERLIST:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    if (sucessRespnse.getData().size() > 0) {
                        cardUserLists = sucessRespnse.getData();
                        adapter.add(cardUserLists);
                    } else {
                        Toast.makeText(getContext(), "List is empty", Toast.LENGTH_SHORT).show();
                    }

                } else
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

    }
}
