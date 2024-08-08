package com.iotsmartaliv.activity.booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.adapter.SelectRoomAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SearchBookingData;
import com.iotsmartaliv.apiCalling.models.SearchBookingResponse;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.SelectRoomActivityBinding;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.fragments.booking.SearchRoomFragment.SEARCH_ROOMS;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 23/7/19 :July.
 */
public class SelectRoomActivity extends AppCompatActivity implements View.OnClickListener {

    ApiServiceProvider apiServiceProvider;
    SelectRoomActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SelectRoomActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.rlBook.setOnClickListener(this);
        binding.recyclerViewRoom.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewRoom.setItemAnimator(new DefaultItemAnimator());
        binding.recyclerViewRoom.setAdapter(new SelectRoomAdapter(this, SEARCH_ROOMS.getData()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_book:
                ArrayList<SearchBookingData> bookedRoomArrayList = new ArrayList<>();
                for (SearchBookingData searchBookingData : SEARCH_ROOMS.getData()) {
                    if (searchBookingData.isSelectedForBooing()) {
                        bookedRoomArrayList.add(searchBookingData);
                    }
                }
                if (bookedRoomArrayList.size() > 0) {
                    Util.checkInternet(SelectRoomActivity.this, new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                apiServiceProvider.callAPIForRoomBooking(SEARCH_ROOMS.getSearchCriteria(), bookedRoomArrayList, new RetrofitListener<SearchBookingResponse>() {
                                    @Override
                                    public void onResponseSuccess(SearchBookingResponse sucessRespnse, String apiFlag) {
                                        if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                            showSuccessfullDailog(sucessRespnse.getMsg());
                                        } else {
                                            showFailedDialog(sucessRespnse.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                        Util.firebaseEvent(Constant.APIERROR, SelectRoomActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());
                                        Toast.makeText(SelectRoomActivity.this, "Something went Wrong Server.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Please Select The Room.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void showFailedDialog(String msg) {
        final AlertDialog dialogBuilder2 = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        TextView tv_title = dialogView.findViewById(R.id.tv_title);
        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        ImageView iv_error = dialogView.findViewById(R.id.iv_greenCheck);
        iv_error.setImageResource(R.mipmap.ic_error);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText(msg);
        tv_title.setText("Booking Failed!");
        rlOk.setOnClickListener(v -> dialogBuilder2.dismiss());
        dialogBuilder2.setView(dialogView);
        Window window = dialogBuilder2.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder2.show();
    }

    public void showSuccessfullDailog(String msg) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_successful_booked, null);
        RelativeLayout rlOk = dialogView.findViewById(R.id.rl_ok);
        TextView tvMessage = dialogView.findViewById(R.id.tv_message);
        tvMessage.setText(msg);
        rlOk.setOnClickListener(v -> {
            dialogBuilder.dismiss();
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
        dialogBuilder.setView(dialogView);
        Window window = dialogBuilder.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogBuilder.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
