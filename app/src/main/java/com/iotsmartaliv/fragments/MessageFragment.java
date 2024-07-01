package com.iotsmartaliv.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;

/**
 * This fragment class is used for left fragment drawer.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class MessageFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_message, container, false);
        return view;
    }


}
