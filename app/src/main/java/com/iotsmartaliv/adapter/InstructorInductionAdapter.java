package com.iotsmartaliv.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.InstructorActivity;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.InstructorInductionData;
import com.iotsmartaliv.utils.CommanUtils;

import java.util.HashMap;
import java.util.List;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */
public class InstructorInductionAdapter extends RecyclerView.Adapter<InstructorInductionAdapter.ViewHolder> implements RetrofitListener<SuccessResponse> {
    Activity context;
    ApiServiceProvider apiServiceProvider;
    List<InstructorInductionData> instructorDataList;
    int currentActivePos = -1;
    private String TAG = "InstructorInductionAdapter";

    public InstructorInductionAdapter(Activity context, List<InstructorInductionData> instructorInductionList) {
        this.context = context;
        this.instructorDataList = instructorInductionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_instructor_induction, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvInstructor.setText(instructorDataList.get(i).getVisitorName());
        viewHolder.tvInstructorMobileNumber.setText("Instructor mobile number: " + instructorDataList.get(i).getVisitorContact());
        viewHolder.tvActivity.setText("Activity: " + instructorDataList.get(i).getActivity());
        viewHolder.tvCompany.setText("Company: " + instructorDataList.get(i).getInstructorCompany());
        viewHolder.tvDay.setText("Day: " + CommanUtils.getDaysOnFormateString(instructorDataList.get(i).getRecurrenceDays()));
        viewHolder.tvStartDate.setText("Start date: " + instructorDataList.get(i).getProgramStartDate());
        viewHolder.tvEndDate.setText("End date: " + instructorDataList.get(i).getProgramEndDate());
        viewHolder.tvStartTime.setText("Start time: " + instructorDataList.get(i).getProgramStartTime());
        viewHolder.tvEndTime.setText("End time: " + instructorDataList.get(i).getProgramEndTime());
        viewHolder.tvApprove.setOnClickListener(v -> {
            apiServiceProvider = ApiServiceProvider.getInstance(context);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
            hashMap.put("iapproval_ID", instructorDataList.get(i).getIapprovalID());
            hashMap.put("response", "1");
            currentActivePos = i;
            apiServiceProvider.inductionHrResponse(hashMap, InstructorInductionAdapter.this);

        });
        viewHolder.tvReject.setOnClickListener(v -> {
            apiServiceProvider = ApiServiceProvider.getInstance(context);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
            hashMap.put("iapproval_ID", instructorDataList.get(i).getIapprovalID());
            hashMap.put("response", "2");
            currentActivePos = i;
            apiServiceProvider.inductionHrResponse(hashMap, InstructorInductionAdapter.this);
            //  instructorDataList.remove(i);
            //  notifyDataSetChanged();
            //notifyItemRemoved(position);
            //notifyItemRangeChanged(position, mDataSet.size());
        });
    }


    @Override
    public int getItemCount() {
        return instructorDataList.size();
    }

    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.INSTRUCTOR_INDUCTION_HR_RESPONSE:
                Log.d(TAG, "onResponseSuccess: " + sucessRespnse.toString());
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    String message = sucessRespnse.getMsg();
                    Log.d(TAG, "onResponseSuccess: " + message);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    instructorDataList.remove(currentActivePos);
                    notifyDataSetChanged();
                    ((InstructorActivity) context).refreshPage();
                } else {
                    Toast.makeText(context, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.INSTRUCTOR_INDUCTION_HR_RESPONSE:
                Toast.makeText(context, errorObject.getMessage(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstructor, tvInstructorMobileNumber, tvActivity, tvCompany, tvDay, tvStartDate, tvEndDate, tvStartTime, tvEndTime, tvApprove, tvReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInstructor = itemView.findViewById(R.id.tv_instructor);
            tvInstructorMobileNumber = itemView.findViewById(R.id.tv_instructor_mobile_number);
            tvActivity = itemView.findViewById(R.id.tv_activity);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvStartDate = itemView.findViewById(R.id.tv_startDate);
            tvEndDate = itemView.findViewById(R.id.tv_endDate);
            tvStartTime = itemView.findViewById(R.id.tv_startTime);
            tvEndTime = itemView.findViewById(R.id.tv_endTime);
            tvApprove = itemView.findViewById(R.id.tv_approve);
            tvReject = itemView.findViewById(R.id.tv_reject);
        }
    }
}
