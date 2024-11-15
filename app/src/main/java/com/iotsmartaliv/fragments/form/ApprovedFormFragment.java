package com.iotsmartaliv.fragments.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.FragmentApplyFormBinding;
import com.iotsmartaliv.databinding.FragmentApprovedFormBinding;
import com.iotsmartaliv.viewModel.ApplyFormViewModel;
import com.iotsmartaliv.viewModel.ApprovedFormViewModel;

public class ApprovedFormFragment extends Fragment {
    FragmentApprovedFormBinding binding;
    ApprovedFormViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentApprovedFormBinding.inflate(inflater, container, false);

        init();
        return binding.getRoot();
        // Inflate the layout for this fragment
    }

    void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(ApprovedFormViewModel.class);
        binding.layoutNoData.tvTitle.setText("You currently have no pending online forms awaiting approval");
        binding.layoutNoData.tvDetail.setText("This section displays all online form applications you have submitted to management that are still pending processing and approval. Once an online form application is processed, it will appear in either the \"Approved\" or \"Not Approved\" section above.\n" +
                "\n" +
                "To submit a new application, please navigate to the \"Apply\" section above. For additional information, feel free to contact management through the feedback module.");
    }
}