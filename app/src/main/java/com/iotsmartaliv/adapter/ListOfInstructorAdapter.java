package com.iotsmartaliv.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iotsmartaliv.R;
import com.iotsmartaliv.model.InstructorListData;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 22/7/19 :July.
 */
public class ListOfInstructorAdapter extends RecyclerView.Adapter<ListOfInstructorAdapter.ViewHolder> {

    Context context;
    List<InstructorListData> instructorListData = new ArrayList<>();

    public ListOfInstructorAdapter(Context context, List<InstructorListData> instructorListData) {
        this.context = context;
        this.instructorListData = instructorListData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_list_of_instructor_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvInstructor.setText(instructorListData.get(i).getVisitorName());
        viewHolder.tvCompany.setText("Company: " + instructorListData.get(i).getInstructorCompany());
        viewHolder.tvValidity.setText("Validity: " + instructorListData.get(i).getValidDate());
        viewHolder.tvMobileNumber.setText("Mobile Number: " + instructorListData.get(i).getVisitorContact());
        if (instructorListData.get(i).getMainStatus().equalsIgnoreCase("Occupied")) {
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.orange));
        } else if (instructorListData.get(i).getMainStatus().equalsIgnoreCase("Free")) {
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
        }
        viewHolder.tvStatus.setText(instructorListData.get(i).getMainStatus());
    }

    @Override
    public int getItemCount() {
        return instructorListData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInstructor, tvCompany, tvValidity, tvMobileNumber, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInstructor = itemView.findViewById(R.id.tv_instructor);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvValidity = itemView.findViewById(R.id.tv_validity);
            tvMobileNumber = itemView.findViewById(R.id.tv_mobile_number);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
