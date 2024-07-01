package com.iotsmartaliv.adapter.automation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.Schedule;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 17/8/19 :August : 2019 on 11 : 57.
 */
public class SchedulesListAdapter extends RecyclerView.Adapter<SchedulesListAdapter.ViewHolder> {
    ScheduleActionListener scheduleActionListener;
    private List<Schedule> schedules;
    boolean isSubAdmin = false;
    String userType;
    String isAutomationManagementEnable = "0";
    String rolePermission;

    public SchedulesListAdapter(String userType, List<Schedule> schedule, String rolePermission, ScheduleActionListener scheduleActionListener) {
        this.schedules = schedule;
        this.userType = userType;
        this.rolePermission = rolePermission;
        this.scheduleActionListener = scheduleActionListener;
        for (String rolid : LOGIN_DETAIL.getRoleIDs()) {
            if (rolid.equalsIgnoreCase("1")) {
                isSubAdmin = true;
                break;
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedules_item_home_automation, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Schedule schedule = schedules.get(i);
        viewHolder.tvStartDate.setText("Start Date :" + schedule.getStartDate());
        viewHolder.tvEndDate.setText("End Date :" + schedule.getEndDate());
        viewHolder.tvDays.setText("");

        for (String day : schedule.getDays()) {
            if (viewHolder.tvDays.length() > 0) {
                viewHolder.tvDays.append(", ");
            }
            viewHolder.tvDays.append(day);
        }

        if (!LOGIN_DETAIL.getAppuserType().equalsIgnoreCase("1") && !isSubAdmin) {
            viewHolder.editLay.setVisibility(View.GONE);
            viewHolder.deleteLay.setVisibility(View.GONE);
        }
//        if (userType.equalsIgnoreCase("User")) {
//            viewHolder.llTimeslot.setGravity(Gravity.START);
//            viewHolder.editLay.setVisibility(View.GONE);
//            viewHolder.deleteLay.setVisibility(View.GONE);
//
//        }
        isAutomationManagementEnable = getAutomationManagementEnable(rolePermission);

        if (userType.equalsIgnoreCase("User")) {
            viewHolder.llTimeslot.setGravity(Gravity.START);
            viewHolder.editLay.setVisibility(View.GONE);
            viewHolder.deleteLay.setVisibility(View.GONE);
        } else if (userType.equalsIgnoreCase("Senior Admin")) {
            if (isAutomationManagementEnable.equalsIgnoreCase("1")) {
                viewHolder.editLay.setVisibility(View.VISIBLE);
                viewHolder.deleteLay.setVisibility(View.VISIBLE);
            } else {
                viewHolder.llTimeslot.setGravity(Gravity.START);
                viewHolder.editLay.setVisibility(View.GONE);
                viewHolder.deleteLay.setVisibility(View.GONE);
            }
        } else {
            viewHolder.editLay.setVisibility(View.VISIBLE);
            viewHolder.deleteLay.setVisibility(View.VISIBLE);
        }


        viewHolder.editSchedule.setOnClickListener(v -> scheduleActionListener.editSchedule(schedule, i));
        viewHolder.deleteSchedule.setOnClickListener(v -> scheduleActionListener.deleteSchedule(schedule, i));
        viewHolder.tvViewTimeSlot.setOnClickListener(v -> scheduleActionListener.viewSchedule(schedule, i));
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
    public int getItemCount() {
        return schedules.size();
    }

    public void removeItem(int pos) {
        schedules.remove(pos);
        notifyDataSetChanged();
    }

    public void addItem(Schedule schedule) {
        this.schedules.add(schedule);
        notifyDataSetChanged();
    }

    public List<Schedule> getSchedules() {
        return this.schedules;
    }

    public interface ScheduleActionListener {
        void deleteSchedule(Schedule roomData, int position);

        void editSchedule(Schedule roomData, int position);

        void viewSchedule(Schedule roomData, int position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_start_date)
        TextView tvStartDate;
        @BindView(R.id.tv_end_date)
        TextView tvEndDate;
        @BindView(R.id.tv_days)
        TextView tvDays;
        @BindView(R.id.tv_view_timeSlot)
        TextView tvViewTimeSlot;
        @BindView(R.id.edit_schedule)
        TextView editSchedule;
        @BindView(R.id.delete_schedule)
        TextView deleteSchedule;
        @BindView(R.id.delete_lay)
        LinearLayout deleteLay;

        @BindView(R.id.edit_lay)
        LinearLayout editLay;
        @BindView(R.id.ll_timeslot)
        LinearLayout llTimeslot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
