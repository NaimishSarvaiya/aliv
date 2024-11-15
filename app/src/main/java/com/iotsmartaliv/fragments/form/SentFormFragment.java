package com.iotsmartaliv.fragments.form;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.FragmentSentFormBinding;
import com.iotsmartaliv.viewModel.SentFormViewModel;

public class SentFormFragment extends Fragment {

  FragmentSentFormBinding binding;
  SentFormViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSentFormBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
        // Inflate the layout for this fragment
    }
    void init(){
        viewModel = new ViewModelProvider(requireActivity()).get(SentFormViewModel.class);
        binding.layoutNoData.tvTitle.setText("All Clear!");
        binding.layoutNoData.tvDetail.setText("\n" +
                "All online form applications not approved by management will be displayed in this section. Currently, you do not have any rejected applications. To submit a new application, please go to the \"Apply\" section above.\n" + "\n" +
                "For further assistance, feel free to reach out to management via the feedback module.");
    }
}