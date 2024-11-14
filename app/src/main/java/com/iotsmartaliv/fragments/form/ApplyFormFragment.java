package com.iotsmartaliv.fragments.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.FragmentApplyFormBinding;

public class ApplyFormFragment extends Fragment {

    FragmentApplyFormBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApplyFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}