package com.iotsmartaliv.fragments.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.FragmentNotApprovedFormBinding;
import com.iotsmartaliv.viewModel.ApprovedFormViewModel;

public class NotApprovedFormFragment extends Fragment {
    FragmentNotApprovedFormBinding binding;
    ApprovedFormViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotApprovedFormBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(ApprovedFormViewModel.class);
        binding.layoutNoData.tvTitle.setText("We Couldn't Find Any Results");
        binding.layoutNoData.tvDetail.setText("All online form applications approved by management are displayed in this section. At the moment, you do not have any approved applications. To submit a new application, please visit the \"Apply\" section\n" + "\n" +
                "above. For additional assistance, you may contact management through the feedback module.");
    }
}