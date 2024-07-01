package com.iotsmartaliv.onboarding;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.constants.Constant;


public class Page1To3Fragment extends Fragment {


    private View view;
    private String title,description;
    private int imgSrc ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        title = b.getString(Constant.ONBOARDING_PAGE_TITLE);
        description = b.getString(Constant.ONBOARDING_PAGE_DESC);
        imgSrc = b.getInt(Constant.ONBOARDING_PAGE_IMG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_page_1_to_3, container, false);
        TextView tvTitle,tvDesc;
        ImageView imgPageImage;

        tvDesc = view.findViewById(R.id.tvDesc);
        tvTitle = view.findViewById(R.id.tvTitle);
        imgPageImage = view.findViewById(R.id.imgPageImage);

        tvTitle.setText(title);
        tvDesc.setText(description);
        imgPageImage.setImageResource(imgSrc);


        return  view;
    }
}