package com.iotsmartaliv.lifeGuard.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
public class LgDrawerFragment extends Fragment {


    public LgDrawerFragment() {
        // Required empty public constructor
    }

    public static LgDrawerFragment newInstance() {
        LgDrawerFragment fragment = new LgDrawerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lg_drawer, container, false);
    }
}