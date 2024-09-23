package com.iotsmartaliv.activity.automation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.HomeAutomationBinding;
import com.iotsmartaliv.fragments.automation.RoomOneFragment;
import com.iotsmartaliv.model.AutomationRoomsData;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.utils.Util;

import java.util.List;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 16/8/19 :August : 2019 on 15 : 21.
 */
public class HomeAutomationActivity extends AppCompatActivity implements RetrofitListener<AutomationRoomsResponse> {
    public static int SCHEDULE_CREATED = 12001;
    ApiServiceProvider apiServiceProvider;
    ViewPagerAdapter viewPagerAdapter;
    String automationId;
    int currentSelectPos = 0;
    String isAutomationManagementEnable = "0";
    HomeAutomationBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeAutomationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        boolean isSubAdmin = false;
        for (String rolid : LOGIN_DETAIL.getRoleIDs()) {
            if (rolid.equalsIgnoreCase("1")) {
                isSubAdmin = true;
                break;
            }
        }
        if (!LOGIN_DETAIL.getAppuserType().equalsIgnoreCase("1") && !isSubAdmin) {
            binding.floatingAddButton.hide();
        }

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                isAutomationManagementEnable = getAutomationManagementEnable(viewPagerAdapter.getItemPos(i).getRolePermission());
                if (viewPagerAdapter.getItemPos(i).getUserType().equalsIgnoreCase("User")) {
                    binding.floatingAddButton.setVisibility(View.GONE);
                } else if (viewPagerAdapter.getItemPos(i).getUserType().equalsIgnoreCase("Senior Admin")) {
                    if (isAutomationManagementEnable.equalsIgnoreCase("1")) {
                        binding.floatingAddButton.setVisibility(View.VISIBLE);
                    } else {
                        binding.floatingAddButton.setVisibility(View.GONE);
                    }
                } else {
                    binding.floatingAddButton.setVisibility(View.VISIBLE);
                }
                currentSelectPos = i;
                Glide.with(HomeAutomationActivity.this)
                        .load(viewPagerAdapter.getItemPos(i).getRoomImage())
                        .placeholder(R.mipmap.ic_room)
                        .fitCenter()
                        .into(binding.imageView);
                automationId = viewPagerAdapter.getItemPos(i).getAutomationID();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });


        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getAutomationRoomList(LOGIN_DETAIL.getAppuserID(), HomeAutomationActivity.this);

                }
            }
        });
        binding.floatingAddButton.setOnClickListener(v-> onViewClicked());

//        @OnClick(R.id.floatingAddButton)
//        public void onViewClicked() {
//            Intent intent = new Intent(this, CreateSchedulesActivity.class);
//            intent.putExtra("automation_ID", automationId);
//            startActivityForResult(intent, SCHEDULE_CREATED);
//        }

    }

    public String getAutomationManagementEnable(String rolePermission) {
        try {
            // Parse the JSON string into a JSONObject
            JSONObject jsonObject = new JSONObject(rolePermission);

            // Check if "automation_management_Enable" is present
            if (!jsonObject.has("automation_management_Enable")) {
                // If not present, set it to "0"
                jsonObject.put("automation_management_Enable", "0");
            }

            // Return the value of "automation_management_Enable"
            return jsonObject.getString("automation_management_Enable");

        } catch (JSONException e) {
            e.printStackTrace();
            // In case of an error, return "0" as a default
            return "0";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCHEDULE_CREATED) {
            if (resultCode == Activity.RESULT_OK) {
                Util.checkInternet(this, new Util.NetworkCheckCallback() {
                    @Override
                    public void onNetworkCheckComplete(boolean isAvailable) {
                        apiServiceProvider.getAutomationRoomList(LOGIN_DETAIL.getAppuserID(), HomeAutomationActivity.this);
                    }
                });
            }
        }
    }

    public void onViewClicked() {
        Intent intent = new Intent(this, CreateSchedulesActivity.class);
        intent.putExtra("automation_ID", automationId);
        startActivityForResult(intent, SCHEDULE_CREATED);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
            if (sucessRespnse.getData().size() > 0) {
                viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), sucessRespnse.getData());
                binding.viewPager.setAdapter(viewPagerAdapter);
                if (sucessRespnse.getData().get(0).getUserType().equalsIgnoreCase("User")) {
                    binding.floatingAddButton.setVisibility(View.GONE);
                } else {
                    binding.floatingAddButton.setVisibility(View.VISIBLE);
                }
                Glide.with(HomeAutomationActivity.this)
                        .load(viewPagerAdapter.getItemPos(currentSelectPos).getRoomImage())
                        .placeholder(R.mipmap.ic_room)
                        .fitCenter()
                        .into(binding.imageView);
                automationId = viewPagerAdapter.getItemPos(currentSelectPos).getAutomationID();
                binding.viewPager.setCurrentItem(currentSelectPos);
                binding.imageView.setVisibility(View.VISIBLE);
                binding.tabLayout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(HomeAutomationActivity.this, "Sorry! No room added in automation.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(HomeAutomationActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        Util.firebaseEvent(Constant.APIERROR, HomeAutomationActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

        Toast.makeText(HomeAutomationActivity.this, "Something went wrong on server.", Toast.LENGTH_SHORT).show();
    }

    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public List<AutomationRoomsData> data;
//        FloatingActionButton newFlotatingActionButton;

        ViewPagerAdapter(FragmentManager manager, List<AutomationRoomsData> data) {
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
            return RoomOneFragment.newInstance(data.get(position));
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

        AutomationRoomsData getItemPos(int pos) {
            return data.get(pos);
        }
    }
}
