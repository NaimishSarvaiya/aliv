package com.iotsmartaliv.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iotsmartaliv.fragments.automation.RoomOneFragment;
import com.iotsmartaliv.model.AutomationRoomsData;

import java.util.List;

public class PagerAdapterforRoom extends FragmentStatePagerAdapter {
    public static List<AutomationRoomsData> data;
//        FloatingActionButton newFlotatingActionButton;

    PagerAdapterforRoom(FragmentManager manager, List<AutomationRoomsData> data) {
        super(manager);
        this.data = data;
//            this.newFlotatingActionButton = newFlotatingActionButton;
    }

    @Override
    public Fragment getItem(int position) {
//            if (data.get(position).getUserType().equalsIgnoreCase("User")){
//                floatingAddButton.setVisibility(View.GONE);
//            }else {
//                floatingAddButton.setVisibility(View.VISIBLE);
//            }
        return RoomOneFragment.newInstance(data.get(position ));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return data.get(position).getRoomName();
    }

    public void setData(List<AutomationRoomsData> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public static   AutomationRoomsData getItemPos(int pos) {
        return data.get(pos);
    }
}