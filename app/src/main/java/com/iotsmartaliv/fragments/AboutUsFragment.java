package com.iotsmartaliv.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.AboutUsFragmentBinding;

public class AboutUsFragment extends AppCompatActivity {

AboutUsFragmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AboutUsFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        try {
            PackageInfo pInfo =this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.tvVersion.setText("App Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = null;
//        binding = AboutUsFragmentBinding.inflate(inflater,container,false);
////        view = inflater.inflate(R.layout.about_us_fragment, container, false);
//
//        try {
//            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
//            String version = pInfo.versionName;
//            binding.tvVersion.setText("App Version: " + version);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
////        unbinder.unbind();
//    }
}