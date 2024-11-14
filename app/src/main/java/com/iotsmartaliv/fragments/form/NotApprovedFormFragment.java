package com.iotsmartaliv.fragments.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.FragmentNotApprovedFormBinding;

public class NotApprovedFormFragment extends Fragment {
    FragmentNotApprovedFormBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotApprovedFormBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }
}