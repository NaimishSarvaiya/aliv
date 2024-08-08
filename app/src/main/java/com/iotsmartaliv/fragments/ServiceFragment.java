package com.iotsmartaliv.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.BookServiceActivity;
import com.iotsmartaliv.adapter.ServiceAdapter;
import com.iotsmartaliv.databinding.FragmentServiceBinding;

/**
 * This fragment class is used for left fragment drawer.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class ServiceFragment extends Fragment {
FragmentServiceBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentServiceBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_service, container, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(new ServiceAdapter(getContext()));
        binding.bookService.setOnClickListener(v ->  startActivity(new Intent(getActivity(), BookServiceActivity.class)));
        return binding.getRoot();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    @OnClick({R.id.book_service, R.id.recyclerView})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.book_service:
//                startActivity(new Intent(getActivity(), BookServiceActivity.class));
//                break;
//        }
//    }
}