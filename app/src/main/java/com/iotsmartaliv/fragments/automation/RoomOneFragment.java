package com.iotsmartaliv.fragments.automation;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.automation.EditScheduleActivity;
import com.iotsmartaliv.adapter.automation.SchedulesListAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.AutomationScheduleResponse;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.databinding.RoomOneFragmentBinding;
import com.iotsmartaliv.dialog_box.ShowScheduleDetailDialog;
import com.iotsmartaliv.model.AutomationRoomsData;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.model.Schedule;

import java.util.HashMap;
import java.util.Iterator;

import static com.iotsmartaliv.activity.automation.HomeAutomationActivity.SCHEDULE_CREATED;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 16/8/19 :August : 2019 on 17 : 04.
 */
public class RoomOneFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    RoomOneFragmentBinding binding;
    public static final String SCHEDULE_DATA = "SCHEDULE_DATA";
    private static final String ROOM_DATA = "Room_Data_key";
    AutomationRoomsData automationRoomsData;
    ApiServiceProvider apiServiceProvider;
    SchedulesListAdapter schedulesListAdapter;

    String userType;
    ToggleButton  switchSelection;

    String isAutomationManagementEnable = "0";

    public static RoomOneFragment newInstance(AutomationRoomsData describable) {
        RoomOneFragment fragment = new RoomOneFragment();
//        floatingActionButtonView = floatingActionButton;
        Bundle bundle = new Bundle();
        bundle.putSerializable(ROOM_DATA, describable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RoomOneFragmentBinding.inflate(inflater,container,false);
        View view = inflater.inflate(R.layout.room_one_fragment, container, false);

        automationRoomsData = (AutomationRoomsData) getArguments().getSerializable(ROOM_DATA);
        if(automationRoomsData.getUserType()!=null){
            userType = automationRoomsData.getUserType();
        }
        binding.lightToggle.setChecked(automationRoomsData.getLights().equalsIgnoreCase("1"));
        binding.airConditionerToggle.setChecked(automationRoomsData.getAirconditioner().equalsIgnoreCase("1"));
        binding.recyclerViewSchedules.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSchedules.setNestedScrollingEnabled(false);
        binding.recyclerViewSchedules.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        apiServiceProvider = ApiServiceProvider.getInstance(getContext());
        if (!automationRoomsData.getRolePermission().equalsIgnoreCase("") ||automationRoomsData.getRolePermission()!=null ) {
            isAutomationManagementEnable = getAutomationManagementEnable(automationRoomsData.getRolePermission());
        }
        boolean isSubAdmin = false;

        setSwitch(automationRoomsData);
        for (String rolid : LOGIN_DETAIL.getRoleIDs()) {
            if (rolid.equalsIgnoreCase("1")) {
                isSubAdmin = true;
                break;
            }
        }
        if (userType.equalsIgnoreCase("User")) {
            binding.lightToggle.setEnabled(false);
            binding.airConditionerToggle.setEnabled(false);
//            floatingActionButtonView.setVisibility(View.GONE);
        }else if (userType.equalsIgnoreCase("Senior Admin")){
            if (isAutomationManagementEnable.equalsIgnoreCase("1")) {
                binding.lightToggle.setEnabled(true);
                binding.airConditionerToggle.setEnabled(true);
            }else {
                binding.lightToggle.setEnabled(false);
                binding.airConditionerToggle.setEnabled(false);
            }
//            floatingActionButtonView.setVisibility(View.VISIBLE);
        }else {
            binding.lightToggle.setEnabled(true);
            binding.airConditionerToggle.setEnabled(true);
        }
        if (automationRoomsData.getSchedule() == null || automationRoomsData.getSchedule().isEmpty()){
            binding.tvSchedule.setVisibility(View.GONE);
        }else {
            binding.tvSchedule.setVisibility(View.VISIBLE);
        }

        schedulesListAdapter = new SchedulesListAdapter(userType,automationRoomsData.getSchedule(),automationRoomsData.getRolePermission(), new SchedulesListAdapter.ScheduleActionListener() {
            @Override
            public void deleteSchedule(Schedule roomData, int position) {
                HashMap<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("operation", "delete");
                stringStringHashMap.put("type", "0");
                stringStringHashMap.put("id", roomData.getScheduleID());
                apiServiceProvider.deleteAutomationTimeSchedule(stringStringHashMap, new RetrofitListener<AutomationRoomsResponse>() {
                    @Override
                    public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                            Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            schedulesListAdapter.removeItem(position);
                        } else {
                            Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void editSchedule(Schedule roomData, int position) {
                apiServiceProvider.getAutomationSchedule(roomData.getScheduleID(), new RetrofitListener<AutomationScheduleResponse>() {
                    @Override
                    public void onResponseSuccess(AutomationScheduleResponse sucessRespnse, String apiFlag) {
                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                            Intent intent = new Intent(getContext(), EditScheduleActivity.class);
                            intent.putExtra(SCHEDULE_DATA, sucessRespnse.getData());
                            getActivity().startActivityForResult(intent, SCHEDULE_CREATED);
                        } else {
                            Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void viewSchedule(Schedule roomData, int position) {
                apiServiceProvider.getAutomationSchedule(roomData.getScheduleID(), new RetrofitListener<AutomationScheduleResponse>() {
                    @Override
                    public void onResponseSuccess(AutomationScheduleResponse sucessRespnse, String apiFlag) {
                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                            new ShowScheduleDetailDialog(getActivity(), sucessRespnse.getData()).show();
                        } else {
                            Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                        Toast.makeText(getContext(), "Something Went Wrong.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        binding.recyclerViewSchedules.setAdapter(schedulesListAdapter);
        binding.lightToggle.setOnCheckedChangeListener(this);
        binding.airConditionerToggle.setOnCheckedChangeListener(this);
        return binding.getRoot();
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
    void  setSwitch(AutomationRoomsData automationRoomsData){
        String statusAutomationJson = automationRoomsData.getStatusAutomation();
        String assignmentNameJson = automationRoomsData.getAssignmentName();
        HashMap<String, String[]> dataMap = new HashMap<>();

        try {
            JSONObject statusAutomation = null;
            if (!statusAutomationJson.equalsIgnoreCase("")) {
                 statusAutomation = new JSONObject(statusAutomationJson);
            }
            JSONObject assignmentName = new JSONObject(assignmentNameJson);

            Iterator<String> keys = assignmentName.keys();
            while (keys.hasNext()) {
                String key = keys. next();
                String status = "0";
                if (statusAutomation!= null) {
                    status = statusAutomation.optString(key, "0");// Default to "0" if the key is missing
                }else {
                    status = "0";
                }
                String name = assignmentName.getString(key);
                dataMap.put(key, new String[]{status, name});
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        LayoutInflater inflater = LayoutInflater.from(requireActivity());
        for (String key : dataMap.keySet()) {
            String[] value = dataMap.get(key);
            String status = value[0];
            String name = value[1];

            // Inflate the custom switch layout
            RelativeLayout switchLayout = (RelativeLayout) inflater.inflate(R.layout.custome_switch, binding.llSwitch, false);

            TextView switchTitle = switchLayout.findViewById(R.id.tv_switch);
            ToggleButton  switchComponent = switchLayout.findViewById(R.id.toggle_button);

            switchTitle.setText(name);
            switchComponent.setId(Integer.parseInt(key));
            switchComponent.setChecked(status.equals("1"));

            if (userType.equalsIgnoreCase("User")) {
                switchComponent.setEnabled(false);
//            floatingActionButtonView.setVisibility(View.GONE);
            }else if (userType.equalsIgnoreCase("Senior Admin")){
                    if (isAutomationManagementEnable.equalsIgnoreCase("1")) {
                        switchComponent.setEnabled(true);
                    }else {
                        switchComponent.setEnabled(false);
                    }
//            floatingActionButtonView.setVisibility(View.VISIBLE);
            }else {
                switchComponent.setEnabled(true);
            }

//            if (userType.equalsIgnoreCase("User")) {
////                lightToggle.setEnabled(false);
//                switchComponent.setEnabled(false);
////            floatingActionButtonView.setVisibility(View.GONE);
//            }else {
////                lightToggle.setEnabled(true);
//                switchComponent.setEnabled(true);
////            floatingActionButtonView.setVisibility(View.VISIBLE);
//            }
            switchComponent.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Handle switch state change
                if(!buttonView.isPressed()) {
                    return;
                }
                switchSelection = (ToggleButton) buttonView;
                String id = String.valueOf(buttonView.getId());
                String newStatus = isChecked ? "1" : "0";
                changeStatus(id,newStatus,automationRoomsData.getDeviceID());
                // Send data to the second screen
//                sendDataToSecondScreen(id, newStatus, name);
            });

            // Add the custom switch layout to the main layout
            binding.llSwitch.addView(switchLayout);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void changeStatus(String relay, String status, String deviceId) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("automation_ID", automationRoomsData.getAutomationID());
        stringStringHashMap.put("relay", relay);
        stringStringHashMap.put("device_ID", deviceId);
        stringStringHashMap.put("status", status);

        apiServiceProvider.chageStatusOfAutomationDevice(stringStringHashMap, new RetrofitListener<AutomationRoomsResponse>() {
            @Override
            public void onResponseSuccess(AutomationRoomsResponse sucessRespnse, String apiFlag) {
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                    if (status.equalsIgnoreCase("1")){
                        switchSelection.setChecked(true);
                    }else {
                        switchSelection.setChecked(false);
                    }
                } else {
                    Toast.makeText(getContext(), sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                    if (status.equalsIgnoreCase("1")){
                        switchSelection.setChecked(false);
                    }else {
                     switchSelection.setChecked(true);
                    }
//                    chagefailSatus(isOnline);
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                if (status.equalsIgnoreCase("1")){
                   switchSelection.setChecked(false);
                }else {
                   switchSelection.setChecked(true);
                }
//                chagefailSatus(isOnline);
            }
        });
    }

//    public void chagefailSatus(boolean isOnline) {
//        lightToggle.setOnCheckedChangeListener(null);
//        lightToggle.setChecked(!isOnline);
//        lightToggle.setOnCheckedChangeListener(RoomOneFragment.this);
//        airConditionerToggle.setOnCheckedChangeListener(null);
//        airConditionerToggle.setChecked(!isOnline);
//        airConditionerToggle.setOnCheckedChangeListener(RoomOneFragment.this);
//    }

    /* @OnClick(R.id.floatingAddButton)
     public void onViewClicked() {
         startActivity(new Intent(getActivity(), CreateSchedulesActivity.class).putExtra("automation_ID", automationRoomsData.getAutomationID()));
     }
 */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        changeStatus(buttonView.getId() == R.id.light_toggle, isChecked);
    }
}
