package com.iotsmartaliv.fragments.booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.ActiveBookingAdapter;
import com.iotsmartaliv.databinding.FragmentBookingHistoryBinding;

public class BookingHistoryFragment extends Fragment {

    FragmentBookingHistoryBinding binding;
    ActiveBookingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingHistoryBinding.inflate(getLayoutInflater(),container,false);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.layoutNoHistoryBooking.tvTitle.setText(R.string.no_active_bookings);
        binding.layoutNoHistoryBooking.tvDetail.setText(R.string.you_currently_have_no_active_bookings_please_check_back_later_or_add_a_new_booking);
//       binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
//       binding.rvBooking.setVisibility(View.GONE);
        binding.rvHistoryBooking.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ActiveBookingAdapter(requireActivity());
        binding.rvHistoryBooking.setAdapter(adapter);
    }
}