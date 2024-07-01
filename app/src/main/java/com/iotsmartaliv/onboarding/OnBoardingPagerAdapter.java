package com.iotsmartaliv.onboarding;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;


public class OnBoardingPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;

    public OnBoardingPagerAdapter(FragmentManager manager,ArrayList<Fragment> mFragmentList) {
        super(manager);
        this.mFragmentList = mFragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
    }


}