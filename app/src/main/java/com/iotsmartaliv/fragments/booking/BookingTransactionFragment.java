package com.iotsmartaliv.fragments.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.STRIPE_CUSTOMER_ID;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.app.Dialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.booking.BookingTransactionAdapter;
import com.iotsmartaliv.adapter.booking.CardAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.databinding.FragmentBookingTransactionBinding;
import com.iotsmartaliv.model.booking.CreateCustomerOnStripRequest;
import com.iotsmartaliv.model.booking.CreateCustomerResponse;
import com.iotsmartaliv.model.booking.CustomerDepositModel;
import com.iotsmartaliv.model.booking.TransactionModel;
import com.iotsmartaliv.model.booking.TransactionResponse;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingTransactionFragment extends Fragment implements BookingTransactionAdapter.OnItemClickListener {
    FragmentBookingTransactionBinding binding;
    BookingTransactionAdapter adapter;
    List<TransactionModel> transactionList = new ArrayList<>();
    private ApiServiceProvider apiServiceProvider;
    private boolean isRefreshing = false; // Flag to check if pull-to-refresh is active

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingTransactionBinding.inflate(getLayoutInflater(), container, false);
        apiServiceProvider = ApiServiceProvider.getInstance(requireActivity(), true);
        init();
        return binding.getRoot();
    }

    private void init() {
        binding.layoutNoTransaction.tvTitle.setText(R.string.no_active_deposit);
        binding.layoutNoTransaction.tvDetail.setText(R.string.currently_there_are_no_active_deposits_please_note_that_your_deposit_amount_will_be_held_on_your_debit_credit_card_but_no_transaction_will_occur_unless_your_deposit_is_forfeited_check_back_later);
        binding.layoutNoTransaction.llNoBooking.setVisibility(View.VISIBLE);
        binding.rvTransaction.setVisibility(View.GONE);
        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new BookingTransactionAdapter(requireActivity(), transactionList, BookingTransactionFragment.this);
        binding.rvTransaction.setAdapter(adapter);
        transactionHistory();
        binding.pullToRefresh.setOnRefreshListener(() -> {
            isRefreshing = true; // Set flag to true when pull-to-refresh starts
            transactionHistory();
        });
    }

    void transactionHistory() {
        if (SharePreference.getInstance(requireActivity()).getString(STRIPE_CUSTOMER_ID) == null || SharePreference.getInstance(requireActivity()).getString(STRIPE_CUSTOMER_ID).equalsIgnoreCase("")) {

            createCustomerOnStripe();
//            binding.rvTransaction.setVisibility(View.GONE);
//            binding.rvTransaction.setLayoutManager(new LinearLayoutManager(requireActivity()));
        } else {
            getTotalDepositOfCustome(SharePreference.getInstance(requireActivity()).getString(STRIPE_CUSTOMER_ID));
//            getDefaultCard(SharePreference.getInstance(this).getString(STRIPE_CUSTOMER_ID));
        }
    }

    private void getTransaction(String customerID) {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getTransaction(customerID, new RetrofitListener<TransactionResponse>() {
                        @Override
                        public void onResponseSuccess(TransactionResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getTransactions() != null && !sucessRespnse.getTransactions().isEmpty()) {
                                    transactionList = sucessRespnse.getTransactions();
                                    adapter.addData(transactionList);
                                    binding.layoutNoTransaction.llNoBooking.setVisibility(View.GONE);
                                    binding.rvTransaction.setVisibility(View.VISIBLE);

                                } else {
                                    binding.layoutNoTransaction.llNoBooking.setVisibility(View.VISIBLE);
                                    binding.rvTransaction.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(requireActivity(), sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
                isRefreshing = false;
                binding.pullToRefresh.setRefreshing(false);
            }
        });
    }

    void createCustomerOnStripe() {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            CreateCustomerOnStripRequest request = new CreateCustomerOnStripRequest(LOGIN_DETAIL.getUserEmail(), LOGIN_DETAIL.getUserFullName());

            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.createCustomerOnStripe(request, new RetrofitListener<CreateCustomerResponse>() {
                        @Override
                        public void onResponseSuccess(CreateCustomerResponse sucessRespnse, String apiFlag) {
//                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getCustomerID() != null) {
                                    getTotalDepositOfCustome(sucessRespnse.getCustomerID());
                                    Log.e("ClientId", sucessRespnse.getCustomerID());
                                    SharePreference.getInstance(requireActivity()).putString(STRIPE_CUSTOMER_ID, sucessRespnse.getCustomerID());
                                }

                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    private void getTotalDepositOfCustome(String customerID) {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(requireActivity(), new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getTotalDepositOfCustome(customerID, new RetrofitListener<CustomerDepositModel>() {
                        @Override
                        public void onResponseSuccess(CustomerDepositModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                binding.tvTotalDepost.setText("Total Deposit: $" + String.valueOf(sucessRespnse.getTotalDeposit()));
                            } else {
                                binding.tvTotalDepost.setText("Total Deposit: $0");
//                                Toast.makeText(requireActivity(),sucessRespnse.getMsg(),Toast.LENGTH_LONG).show();
                            }
                            getTransaction(customerID);
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }

    private void showTransactionDetailsDialog(int position, TransactionModel transactionModel) {
        // Create a new dialog
        final Dialog dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.transaction_detail_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Get the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        TextView tvBookingId = dialog.findViewById(R.id.tvBookingId);
        TextView tvRoomName = dialog.findViewById(R.id.tvRoomName);
        TextView tvDate = dialog.findViewById(R.id.tvDate);
        TextView tvTransactionId = dialog.findViewById(R.id.tvTransactionId);
        TextView tvCardNumber = dialog.findViewById(R.id.tvCardNumber);
        TextView tvAmount = dialog.findViewById(R.id.tv_rate);
        TextView tvRefunded = dialog.findViewById(R.id.tv_refunded);
        TextView btn_done = dialog.findViewById(R.id.btn_done);
        ImageView cardImage = dialog.findViewById(R.id.img_CardImgae);

        Map<String, String> metadata = transactionModel.getMetadata();
        // Set room name and booking ID
        if (metadata != null) {
            String roomName = metadata.get("room_name");
            String bookingId = metadata.get("booking_id");

            if (roomName != null) {
                tvRoomName.setText(roomName);
            } else {
                tvRoomName.setText("N/A");
            }

            if (bookingId != null) {
                tvBookingId.setText(bookingId);
            } else {
                tvBookingId.setText("N/A");
            }
        }
        // Set date
        tvDate.setText(convertTimestampToDate(transactionModel.getCreated()));
        tvTransactionId.setText(transactionModel.getPayment_intent());
        if (transactionModel.getPayment_method_details().getType().equalsIgnoreCase("card")) {
            setCardBrandImage(transactionModel.getPayment_method_details().getCard().getBrand(), cardImage);
            tvCardNumber.setText("Card End With " + transactionModel.getPayment_method_details().getCard().getLast4());
        } else {
            cardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            cardImage.setImageResource(R.drawable.pay_now);
            tvCardNumber.setText("Paynow");
        }

        if (transactionModel.isCaptured()) {
            tvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            tvRefunded.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            tvAmount.setText("$" + (transactionModel.getAmount_captured() / 100.0)+ " " + "(Paid)");

            if (transactionModel.getAmount() != transactionModel.getAmount_captured() &&
                    transactionModel.getAmount() != transactionModel.getAmount_refunded()) {
                tvRefunded.setVisibility(View.VISIBLE);
                tvAmount.setText("$" + (transactionModel.getAmount_captured() / 100.0) + " " + "(Paid)");
                tvRefunded.setText("$" + (transactionModel.getAmount_refunded() / 100.0) + " " + "(Refunded)");
            } else if (transactionModel.getAmount() != transactionModel.getAmount_captured() ||
                    transactionModel.getAmount_refunded() != 0 && transactionModel.getAmount() != transactionModel.getAmount_refunded()) {
                tvRefunded.setVisibility(View.VISIBLE);
                tvAmount.setText("$" + (transactionModel.getAmount_captured() - transactionModel.getAmount_refunded()) / 100 + " " + "(Paid)");
                tvRefunded.setText("$" + (transactionModel.getAmount_refunded() / 100.0) + " " + "(Refunded)");
            } else {
                tvRefunded.setVisibility(View.GONE);
            }

        } else if (transactionModel.isRefunded()) {
            tvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            tvRefunded.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            tvAmount.setText("$" + (transactionModel.getAmount_refunded() / 100.0)+ " " + "(Refunded)");

            if (transactionModel.getAmount() != transactionModel.getAmount_received() &&
                    transactionModel.getAmount() != transactionModel.getAmount_refunded()) {
                tvRefunded.setVisibility(View.VISIBLE);
                tvRefunded.setText("$" + (transactionModel.getAmount() - transactionModel.getAmount_received()) / 100.0 +" " + "(Paid)");
            } else {
                tvRefunded.setVisibility(View.GONE);
            }

        } else {
            tvAmount.setTextColor(ContextCompat.getColor(getContext(), R.color.newNavyBuleBaseColor));
            tvRefunded.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
            tvAmount.setText("$" + (transactionModel.getAmount() / 100.0)+ " " + "(Hold)");

            if (transactionModel.getAmount() != transactionModel.getAmount_captured() && transactionModel.getAmount_captured() > 0) {
                tvRefunded.setVisibility(View.VISIBLE);
                tvRefunded.setText("$" + (transactionModel.getAmount_captured() / 100.0));
            } else {
                tvRefunded.setVisibility(View.GONE);
            }
        }
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });


        // Show the dialog
        dialog.show();
    }

    private String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' h:mm:ss a", Locale.getDefault());
        return sdf.format(date);
    }

    @Override
    public void onItemClick(int position, TransactionModel transactionData) {
        showTransactionDetailsDialog(position, transactionData);
    }

    public void setCardBrandImage(String brand, ImageView imageView) {
        int imageResId = R.drawable.no_card;
        switch (brand != null ? brand.toLowerCase() : "") {
            case "visa":
                imageResId = R.drawable.card_visa;
                break;
            case "mastercard":
                imageResId = R.drawable.card_master;
                break;
            case "amex":
                imageResId = R.drawable.card_mx;
                break;
            case "discover":
                imageResId = R.drawable.card_discover;
                break;
            case "jcb":
                imageResId = R.drawable.card_jcb;
                break;
            case "dinersclub":
                imageResId = R.drawable.card_diner_club;
                break;
            case "unionpay":
                imageResId = R.drawable.card_union_pay;
                break;
            default:
                imageResId = R.drawable.no_card;
                break;
        }
        imageView.setImageResource(imageResId);
    }
}