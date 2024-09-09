package com.iotsmartaliv.fragments;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.AboutUsFragmentBinding;


//import butterknife.Unbinder;

/**
 * This fragment class is used for about us..
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class AboutUsFragment extends Fragment {


//    @BindView(R.id.tv_version)
//    TextView tvVersion;
//    Unbinder unbinder;
AboutUsFragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        binding = AboutUsFragmentBinding.inflate(inflater,container,false);
//        view = inflater.inflate(R.layout.about_us_fragment, container, false);

        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            binding.tvVersion.setText("App Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
    }
}