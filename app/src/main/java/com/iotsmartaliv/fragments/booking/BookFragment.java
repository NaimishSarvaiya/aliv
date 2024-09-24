package com.iotsmartaliv.fragments.booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.BookListAdapter;
import com.iotsmartaliv.databinding.FragmentBookBinding;

public class BookFragment extends Fragment {

    FragmentBookBinding binding;
    BookListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookBinding.inflate(inflater,container,false);
        init();
        return binding.getRoot();
    }
    private void init() {
        binding.layoutNoData.tvTitle.setText(R.string.we_couldn_t_find_any_result);
        binding.layoutNoData.tvDetail.setText(R.string.we_couldn_t_locate_any_relevant_results_in_your_communities_right_now_please_try_again_later);
//       binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
//       binding.rvBooking.setVisibility(View.GONE);
        binding.rvBooking.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookListAdapter(requireActivity());
        binding.rvBooking.setAdapter(adapter);
    }
}