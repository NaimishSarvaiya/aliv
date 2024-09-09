package com.iotsmartaliv.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.FaultActivity;
import com.iotsmartaliv.adapter.ServiceAdapter;
import com.iotsmartaliv.databinding.FragmentMaintainenceBinding;


//import butterknife.OnClick;
//import butterknife.Unbinder;

/**
 * This fragment class is used for maintenence fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class MaintenenceFragment extends Fragment {
    FragmentMaintainenceBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMaintainenceBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_maintainence, container, false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerView.setAdapter(new ServiceAdapter(getContext()));
        binding.reportFaultBtn.setOnClickListener(v-> startActivity(new Intent(getContext(), FaultActivity.class)));
        binding.recyclerView.setOnClickListener(v-> startActivity(new Intent(getContext(), FaultActivity.class)));
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }

//    @OnClick({R.id.report_fault_btn, R.id.recyclerView})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.report_fault_btn:
//                startActivity(new Intent(getContext(), FaultActivity.class));
//                break;
//
//        }
//    }
}
