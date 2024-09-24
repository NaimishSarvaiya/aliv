package com.iotsmartaliv.fragments.booking;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.BookListAdapter;
import com.iotsmartaliv.adapter.booking.BookingTransactionAdapter;
import com.iotsmartaliv.databinding.FragmentBookingTransactionBinding;
import com.iotsmartaliv.model.booking.TransactionModel;

import java.util.ArrayList;

public class BookingTransactionFragment extends Fragment {
    FragmentBookingTransactionBinding binding;
    BookingTransactionAdapter adapter;
    ArrayList<TransactionModel> transactionList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingTransactionBinding.inflate(getLayoutInflater(), container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",0));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",1));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",2));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",0));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",0));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",1));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",1));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",2));
        transactionList.add(new TransactionModel("Meeting Room","23-Sep-2024","$40",2));
        binding.layoutNoTransaction.tvTitle.setText(R.string.no_active_deposit);
        binding.layoutNoTransaction.tvDetail.setText(R.string.currently_there_are_no_active_deposits_please_note_that_your_deposit_amount_will_be_held_on_your_debit_credit_card_but_no_transaction_will_occur_unless_your_deposit_is_forfeited_check_back_later);
//       binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
//       binding.rvBooking.setVisibility(View.GONE);
        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookingTransactionAdapter(requireActivity(),transactionList);
        binding.rvTransaction.setAdapter(adapter);
    }
}