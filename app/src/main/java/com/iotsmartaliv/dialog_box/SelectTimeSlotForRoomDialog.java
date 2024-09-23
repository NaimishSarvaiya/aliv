package com.iotsmartaliv.dialog_box;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.SelectRoomAdapter;
import com.iotsmartaliv.adapter.TimeSlotRoomDialogAdapter;
import com.iotsmartaliv.apiAndSocket.models.SearchBookingData;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;


public class SelectTimeSlotForRoomDialog extends Dialog implements View.OnClickListener {
    public Context context;
    public Button cancel_btn, done_btn;
    RecyclerView recyclerView;
    SearchBookingData searchBookingData;
    SelectRoomAdapter.SelectItemListener selectItemListener;
    private EditText purpose_ed;

    public SelectTimeSlotForRoomDialog(Context context, SearchBookingData searchBookingData, SelectRoomAdapter.SelectItemListener selectItemListener) {
        super(context);
        this.context = context;
        this.selectItemListener = selectItemListener;
        this.searchBookingData = searchBookingData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_select_time_slot);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cancel_btn = findViewById(R.id.cancel_btn);
        done_btn = findViewById(R.id.done_btn);
        cancel_btn.setOnClickListener(this);
        done_btn.setOnClickListener(this);
        purpose_ed = findViewById(R.id.purpose_ed);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new TimeSlotRoomDialogAdapter(searchBookingData.getTimeSlots()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                for (TimeSlot timeSlot : searchBookingData.getTimeSlots()) {
                    timeSlot.setSelected(false);
                }
                selectItemListener.cancelBtnClick();
                searchBookingData.setPurpuse("");
                dismiss();
                break;
            case R.id.done_btn:
                if (purpose_ed.getText().toString().trim().length() > 0) {

                    boolean isTimeSlotSelect = false;
                    for (TimeSlot timeSlot : searchBookingData.getTimeSlots()) {
                        if (timeSlot.isSelected()) {
                            isTimeSlotSelect = true;
                            break;
                        }
                    }
                    if (isTimeSlotSelect) {
                        searchBookingData.setPurpuse(purpose_ed.getText().toString().trim());
                        selectItemListener.doneBtnSelected();
                        dismiss();
                    } else {
                        Toast.makeText(context, "Select Time Slot.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    purpose_ed.setError("Please enter purpose.");
                    purpose_ed.requestFocus();
                }
                break;
            default:
                break;
        }
    }
}