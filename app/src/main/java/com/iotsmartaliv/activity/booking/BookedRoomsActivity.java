package com.iotsmartaliv.activity.booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.DeviceDetailActivity;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.adapter.BookedRoomsAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.BookRoomsResponse;
import com.iotsmartaliv.model.BookingResponse;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;

/**
 * This activity class is used for Facility Booking .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class BookedRoomsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerViewRoom)
    RecyclerView recyclerViewRoom;
    ApiServiceProvider apiServiceProvider;
    String booking_ID;
    BookedRoomsAdapter bookedRoomsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_booking);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiServiceProvider = ApiServiceProvider.getInstance(this);
        booking_ID = getIntent().getStringExtra("booking_ID");
        bookedRoomsAdapter = new BookedRoomsAdapter(BookedRoomsActivity.this, new ArrayList<>(),
                (roomData, position) ->
                {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookedRoomsActivity.this);
                    alertDialogBuilder.setTitle("Alert!");
                    alertDialogBuilder.setMessage("are you sure you want to cancel")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id) -> {

                                SharePreference.getInstance(BookedRoomsActivity.this).putBoolean(SHAKE_ENABLE, true);

                                Util.checkInternet(this, new Util.NetworkCheckCallback() {
                                    @Override
                                    public void onNetworkCheckComplete(boolean isAvailable) {
                                        apiServiceProvider.cancelRoomOrBooking(LOGIN_DETAIL.getAppuserID(), booking_ID, roomData.getRoomID(), new RetrofitListener<BookingResponse>() {
                                            @Override
                                            public void onResponseSuccess(BookingResponse searchBookingResponse1, String apiFlag1) {
                                                if (searchBookingResponse1.getStatus().equalsIgnoreCase("OK")) {
                                                    bookedRoomsAdapter.removeCancelBooking(position);
                                                    if (bookedRoomsAdapter.getItemCount() == 0) {
                                                        Intent returnIntent = new Intent();
                                                        setResult(Activity.RESULT_OK, returnIntent);
                                                        finish();
                                                    }
                                                    Toast.makeText(BookedRoomsActivity.this, searchBookingResponse1.getMsg(), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(BookedRoomsActivity.this, searchBookingResponse1.getMsg(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag1) {
                                                Util.firebaseEvent(Constant.APIERROR, BookedRoomsActivity.this,Constant.UrlPath.SERVER_URL+apiFlag1, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                                                Toast.makeText(BookedRoomsActivity.this, "Something went wrong on server.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            })
                            .setNegativeButton("No", (dialog, id) -> {

                                dialog.dismiss();
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();



                });
        recyclerViewRoom.setLayoutManager(new LinearLayoutManager(BookedRoomsActivity.this));
        recyclerViewRoom.setItemAnimator(new DefaultItemAnimator());
        recyclerViewRoom.setAdapter(bookedRoomsAdapter);

        apiServiceProvider.getAllBookingRooms(booking_ID, new RetrofitListener<BookRoomsResponse>() {
            @Override
            public void onResponseSuccess(BookRoomsResponse searchBookingResponse, String apiFlag) {
                if (searchBookingResponse.getStatus().equalsIgnoreCase("OK")) {
                    if (searchBookingResponse.getData().size() > 0) {
                        bookedRoomsAdapter.setData(searchBookingResponse.getData());
                    } else {
                        Toast.makeText(BookedRoomsActivity.this, "Sorry No Room.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookedRoomsActivity.this, searchBookingResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                Util.firebaseEvent(Constant.APIERROR, BookedRoomsActivity.this,Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());

                Toast.makeText(BookedRoomsActivity.this, "Something went wrong on server.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
