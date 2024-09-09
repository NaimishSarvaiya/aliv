package com.iotsmartaliv.fragments.booking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.booking.BookedRoomsActivity;
import com.iotsmartaliv.adapter.BookingsAdapter;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.FragmentShowBookedRoomBinding;
import com.iotsmartaliv.model.BookingResponse;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
//

//import butterknife.Unbinder;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;

public class ShowBookedRoomFragment extends Fragment {

//    @BindView(R.id.recyclerViewRoom)
//    RecyclerView recyclerViewRoom;
//    Unbinder unbinder;
//
//    @BindView(R.id.item_not_found_tv)
//    TextView itemNotFoundTv;
    BookingsAdapter bookingsAdapter;
    ApiServiceProvider apiServiceProvider;
    FragmentShowBookedRoomBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShowBookedRoomBinding.inflate(inflater,container,false);
//        View view = inflater.inflate(R.layout.fragment_show_booked_room, container, false);

        apiServiceProvider = ApiServiceProvider.getInstance(getActivity());
        binding.recyclerViewRoom.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRoom.setItemAnimator(new DefaultItemAnimator());
        bookingsAdapter = new BookingsAdapter(getActivity(), new ArrayList<>(), (bookedRoomData, position) ->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity());
            alertDialogBuilder.setTitle("Alert!");
            alertDialogBuilder.setMessage("Are you sure you want to cancel this booking?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {

                        SharePreference.getInstance(requireActivity()).putBoolean(SHAKE_ENABLE, true);

                        apiServiceProvider.cancelRoomOrBooking(LOGIN_DETAIL.getAppuserID(), bookedRoomData.getBookingID(), null, new RetrofitListener<BookingResponse>() {
                            @Override
                            public void onResponseSuccess(BookingResponse searchBookingResponse, String apiFlag) {
                                if (searchBookingResponse.getStatus().equalsIgnoreCase("OK")) {
                                    bookingsAdapter.removeCancelBooking(position);
                                    if (bookingsAdapter.getItemCount() == 0) {
                                        binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                                    }
                                    Toast.makeText(getContext(), searchBookingResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), searchBookingResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
                                Toast.makeText(getContext(), "Something went wrong on server.", Toast.LENGTH_SHORT).show();
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




        binding.recyclerViewRoom.setAdapter(bookingsAdapter);
        callAPI();
        return binding.getRoot();
    }

    public void callAPI() {
            Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    apiServiceProvider.getAllBookings(LOGIN_DETAIL.getAppuserID(), new RetrofitListener<BookingResponse>() {
                        @Override
                        public void onResponseSuccess(BookingResponse searchBookingResponse, String apiFlag) {
                            if (searchBookingResponse.getStatus().equalsIgnoreCase("OK")) {
                                if (searchBookingResponse.getData().size() > 0) {
                                    bookingsAdapter.setData(searchBookingResponse.getData());
                                    binding.itemNotFoundTv.setVisibility(View.GONE);
                                } else {
                                    binding.itemNotFoundTv.setVisibility(View.VISIBLE);
                                    bookingsAdapter.setData(new ArrayList<>());
                                }
                            } else {
                                Toast.makeText(getContext(), searchBookingResponse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            Util.firebaseEvent(Constant.APIERROR, requireActivity(),Constant.UrlPath.SERVER_URL+apiFlag, LOGIN_DETAIL.getUsername(),LOGIN_DETAIL.getAppuserID(),errorObject.getStatus());
                            Toast.makeText(getContext(), "Something went wrong on server.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
