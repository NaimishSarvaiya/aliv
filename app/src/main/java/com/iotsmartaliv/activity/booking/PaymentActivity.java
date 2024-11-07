package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.PAY_DEPOSIT_BY_CARD;
import static com.iotsmartaliv.constants.Constant.PAY_DEPOSIT_BY_PAYNOW;
import static com.iotsmartaliv.constants.Constant.PAY_FEES_BY_CARD;
import static com.iotsmartaliv.constants.Constant.PAY_FEES_BY_PAYNOW;
import static com.iotsmartaliv.constants.Constant.STRIPE_CUSTOMER_ID;
import static com.iotsmartaliv.constants.Constant.STRIPE_TEST_KEY;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;
import static com.iotsmartaliv.utils.Util.convertDateFormatForBooking;
import static com.iotsmartaliv.utils.Util.getBookingStatusDescription;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityPaymentBinding;
import com.iotsmartaliv.model.booking.AddBookingSlotRequest;
import com.iotsmartaliv.model.booking.AddBookingSlotResponseModel;
import com.iotsmartaliv.model.booking.BookingDetailsModel;
import com.iotsmartaliv.model.booking.BookingSlotDetailData;
import com.iotsmartaliv.model.booking.BookingSlotDetailResponse;
import com.iotsmartaliv.model.booking.CancelBookingRequest;
import com.iotsmartaliv.model.booking.CancelBookingResponse;
import com.iotsmartaliv.model.booking.ComFeature;
import com.iotsmartaliv.model.booking.ConfirmBookingRequest;
import com.iotsmartaliv.model.booking.ConfirmBookingResponse;
import com.iotsmartaliv.model.booking.CreateCustomerOnStripRequest;
import com.iotsmartaliv.model.booking.CreateCustomerResponse;
import com.iotsmartaliv.model.booking.CreatePaymentStripeRequest;
import com.iotsmartaliv.model.booking.DefaultCardModel;
import com.iotsmartaliv.model.booking.PayNowRequest;
import com.iotsmartaliv.model.booking.PayNowResponseModel;
import com.iotsmartaliv.model.booking.PaymentResponseModel;
import com.iotsmartaliv.model.booking.PaymentType;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.stripe.android.Stripe;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding binding;
    BookingDetailsModel bookingDetails;
    BookingSlotDetailData bookingSlotData;
    String startDate, endDate, timeSlot, slotId;
    private ApiServiceProvider apiServiceProvider;
    String defaultCardId = "";
    Stripe stripe;
    int bookingID = 0;
    String path;
    int outStandingAmt;
    String roomName;
    boolean isPaynowDeposit = false;
    boolean isCardPayDeposit = false;
    boolean isPaynowFees = false;
    boolean isCardPayFees = false;
    private ActivityResultLauncher<Intent> addWebActivityResultLauncher;
    List<ComFeature> comFeatures = new ArrayList<>();
    String feesPaymentTpe;
    String depositPaymentTpe;
    String payDeposityBy = "";
    String payFeesBy = "";
    int paymentypeFees = 0;
    int paymentypeDeposit = 0;
    int reSchedulepaymentType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this, true);
        stripe = new Stripe(
                getApplicationContext(),
                STRIPE_TEST_KEY
        );
        if (SharePreference.getInstance(this).getString(STRIPE_CUSTOMER_ID) == null || SharePreference.getInstance(this).getString(STRIPE_CUSTOMER_ID).equalsIgnoreCase("")) {
            createCustomerOnStripe();
        } else {
            getDefaultCard(SharePreference.getInstance(this).getString(STRIPE_CUSTOMER_ID));
        }
        binding.tvChangeCardFees.setOnClickListener(v -> {
            Intent intent = new Intent(this, CardActivity.class);
            intent.putExtra(Constant.ROOM_TYPE, defaultCardId);
            startActivity(intent);
//            finish();
        });
        binding.llToolbar.imgBack.setOnClickListener(v -> {
            finish();
        });
        setData();
        // Set listener to ensure only one checkbox is selected at a time

        binding.checkboxPaynowFees.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxCardFees.setChecked(false);
                isCardPayFees = false;
                isPaynowFees = true;
                payFeesBy = Constant.PAY_FEES_BY_PAYNOW;
            }
        });

        binding.checkboxCardFees.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxPaynowFees.setChecked(false);
                isCardPayFees = true;
                isPaynowFees = false;
                payFeesBy = Constant.PAY_FEES_BY_CARD;
            }
        });

        binding.checkboxPaynowDeposit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.checkboxCardDepost.setChecked(false); // Uncheck Visa if PayNow is selected
                    isCardPayDeposit = false;
                    isPaynowDeposit = true;
//                    if (feesPaymentTpe.equals("2") && depositPaymentTpe.equals("2")) {
//                        payDeposityBy = Constant.PAY_DEPOSIT_BY_PAYNOW;
//                        payFeesBy = Constant.PAY_FEES_BY_PAYNOW;
//                    } else {
                        payDeposityBy = Constant.PAY_DEPOSIT_BY_PAYNOW;
//                    }
                }
            }
        });

        binding.checkboxCardDepost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Uncheck PayNow if Visa is selected
                    binding.checkboxPaynowDeposit.setChecked(false);
                    isPaynowDeposit = false;
                    isCardPayDeposit = true;
//                    if (feesPaymentTpe.equals("2") && depositPaymentTpe.equals("2")) {
//                        payDeposityBy = Constant.PAY_DEPOSIT_BY_CARD;
//                        payFeesBy = Constant.PAY_FEES_BY_CARD;
//                    } else {
                        payDeposityBy = Constant.PAY_DEPOSIT_BY_CARD;
//                    }
                }
            }
        });
        binding.rlConfirmBooking.setOnClickListener(v -> {
            if (path != null) {
                if (bookingSlotData.getFeesType() == 0) {
                    createPaymentWithDeposit();
                } else if (bookingSlotData.getFeesType() == 1) {
                    payNowWithDeposit("");
                } else {

                }
            } else {
                if (feesPaymentTpe.equalsIgnoreCase("2") && depositPaymentTpe.equalsIgnoreCase("2")) {
                    boolean isDepositValid = binding.checkboxPaynowDeposit.isChecked() || binding.checkboxCardDepost.isChecked();
                    if (!isDepositValid) {
                        Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        addBookingSlot();

                    }
                } else {
                    if (feesPaymentTpe.equalsIgnoreCase("2")) {
                        boolean isFeesValid = binding.checkboxPaynowFees.isChecked() || binding.checkboxCardFees.isChecked();
                        if (!isFeesValid) {
                            Toast.makeText(this, "Please select a payment method for Fees.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            addBookingSlot();

                        }
                    } else if (depositPaymentTpe.equalsIgnoreCase("2")) {
                        boolean isDepositValid = binding.checkboxPaynowDeposit.isChecked() || binding.checkboxCardDepost.isChecked();
                        if (!isDepositValid) {
                            Toast.makeText(this, "Please select a payment method for Deposit.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            addBookingSlot();
                        }
                    } else {
                        addBookingSlot();
                    }
                }
            }
//          else {
////                if (path != null) {
////                    if (isCardPayDeposit) {
////                        createPaymentWithOutDeposit();
////                    } else {
////                        payNowWithOutDeposit();
////                    }
////                } else {
//                addBookingSlot();
////                }
//            }
        });
        addWebActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // The card was successfully created, refresh the card list
                        getBookedSlotDetail();
                    }
                }
        );
    }


    private void setData() {
        Intent intent = getIntent();
        if (intent.getStringExtra(Constant.NAVIGATION_PATH) != null) {
            path = intent.getStringExtra(Constant.NAVIGATION_PATH);
            if (intent.getStringExtra(Constant.TIME_SLOT_ID) != null) {
                slotId = intent.getStringExtra(Constant.TIME_SLOT_ID);
            }
            if (intent.getSerializableExtra(Constant.BOOKING_DETAILS) != null) {
                bookingSlotData = (BookingSlotDetailData) intent.getSerializableExtra(Constant.BOOKING_DETAILS);
            }
            if (intent.getStringExtra(Constant.ROOM_START_DATE) != null) {
                startDate = intent.getStringExtra(Constant.ROOM_START_DATE);
            } else {
                startDate = "";
            }
            if (intent.getStringExtra(Constant.ROOM_END_DATE) != null) {
                endDate = intent.getStringExtra(Constant.ROOM_END_DATE);
            } else {
                endDate = "";
            }
            if (intent.getStringExtra(Constant.SELECTED_TIME_SLOT) != null) {
                timeSlot = intent.getStringExtra(Constant.SELECTED_TIME_SLOT);
            } else {
                timeSlot = "";
            }

            if (bookingSlotData.getRoomName() != null) {
                binding.tvRoomName.setText(bookingSlotData.getRoomName());
                binding.llToolbar.tvHeader.setText(bookingSlotData.getRoomName());
                roomName = bookingSlotData.getRoomName();
            } else {
                binding.tvRoomName.setText("");
                binding.llToolbar.tvHeader.setText("");
                roomName = "";
            }
            if (bookingSlotData.getCommunityName() != null) {
                binding.tvCommunity.setText(bookingSlotData.getCommunityName());
            } else {
                binding.tvCommunity.setText("");
            }
            binding.tvSlot.setText(timeSlot);
            if (startDate.equals(endDate)) {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate));
            } else {
                binding.tvBookingDate.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));
            }
            outStandingAmt = intent.getIntExtra(Constant.OUTSTANDING_AMOUNT, 0);
            binding.tvBookingFees.setText("$ " + String.valueOf(outStandingAmt).toString());
            binding.tvTotal.setText("$ " + String.valueOf(outStandingAmt).toString());
            bookingID = intent.getIntExtra(Constant.BOOKING_ID, 0);
            binding.llDeposit.setVisibility(View.GONE);
            setReschedulePaymentMethod(bookingSlotData.getFeesType());
        } else {
            binding.llDeposit.setVisibility(View.VISIBLE);
            if (intent.getStringExtra(Constant.TIME_SLOT_ID) != null) {
                slotId = intent.getStringExtra(Constant.TIME_SLOT_ID);
            }
            if (intent.getSerializableExtra(Constant.BOOKING_DETAILS) != null) {
                bookingDetails = (BookingDetailsModel) intent.getSerializableExtra(Constant.BOOKING_DETAILS);
            }
            if (intent.getStringExtra(Constant.ROOM_START_DATE) != null) {
                startDate = intent.getStringExtra(Constant.ROOM_START_DATE);
            } else {
                startDate = "";
            }
            if (intent.getStringExtra(Constant.ROOM_END_DATE) != null) {
                endDate = intent.getStringExtra(Constant.ROOM_END_DATE);
            } else {
                endDate = "";
            }
            if (intent.getStringExtra(Constant.SELECTED_TIME_SLOT) != null) {
                timeSlot = intent.getStringExtra(Constant.SELECTED_TIME_SLOT);
            } else {
                timeSlot = "";
            }
            if (bookingDetails != null) {
                ;
                if (bookingDetails.getComFeatures() != null) {
                    setPaymentMethod(bookingDetails.getComFeatures());
                }
                outStandingAmt = bookingDetails.getData().getFees();
                if (bookingDetails.getData().getRoomName() != null) {
                    binding.tvRoomName.setText(bookingDetails.getData().getRoomName());
                    binding.llToolbar.tvHeader.setText(bookingDetails.getData().getRoomName());

                } else {
                    binding.tvRoomName.setText("");
                    binding.llToolbar.tvHeader.setText("");
                }
                if (bookingDetails.getData().getCommunityName() != null) {
                    binding.tvCommunity.setText(bookingDetails.getData().getCommunityName());
                } else {
                    binding.tvCommunity.setText("");
                }
                binding.tvSlot.setText(timeSlot);
                if (startDate.equals(endDate)) {
                    binding.tvBookingDate.setText(convertDateFormatForBooking(startDate));
                } else {
                    binding.tvBookingDate.setText(convertDateFormatForBooking(startDate) + " - " + convertDateFormatForBooking(endDate));
                }
                if (bookingDetails.getData().getFees() != null) {
                    binding.tvBookingFees.setText("$ " + String.valueOf(bookingDetails.getData().getFees().toString()));
                    binding.tvTotal.setText("$ " + String.valueOf(bookingDetails.getData().getFees().toString()));
                } else {
                    binding.tvBookingFees.setText("");
                    binding.tvTotal.setText("");
                }
                if (bookingDetails.getData().getAdvanceDeposit() != null) {
                    binding.tvDeposit.setText("$ " + String.valueOf(bookingDetails.getData().getAdvanceDeposit().toString()));
                } else {
                    binding.tvDeposit.setText("");
                }
                // Use the booking details object
            }
        }
    }

    void setReschedulePaymentMethod(int reSchedulepaymentType) {
        binding.llCardPayFees.setVisibility(View.GONE);
        binding.llPayNowFees.setVisibility(View.GONE);
        binding.llPayCarDepost.setVisibility(View.GONE);
        binding.llPayNowDepost.setVisibility(View.GONE);
        binding.llCbPayCardDeposit.setVisibility(View.GONE);
        binding.llCbPayNowfees.setVisibility(View.GONE);
        binding.llCbCardFees.setVisibility(View.GONE);
        binding.llCbPaynowDeposit.setVisibility(View.GONE);
        binding.tvTitlePayFess.setVisibility(View.GONE);
        binding.tvPayDepost.setVisibility(View.GONE);
        binding.llBookingFees.setVisibility(View.GONE);

        if (reSchedulepaymentType == 0) {
            binding.tvPayDepost.setVisibility(View.VISIBLE);
            binding.tvPayDepost.setText("Pay fee by card");
            binding.llPayCarDepost.setVisibility(View.VISIBLE);
            binding.llCbPayCardDeposit.setVisibility(View.GONE);
            binding.llPayNowDepost.setVisibility(View.GONE);
            payFeesBy = PAY_FEES_BY_CARD;
        } else if (reSchedulepaymentType == 1) {
            binding.tvPayDepost.setVisibility(View.VISIBLE);
            binding.tvPayDepost.setText("Pay fee by paynow");
            binding.llPayNowDepost.setVisibility(View.VISIBLE);
            binding.llCbPaynowDeposit.setVisibility(View.GONE);
            binding.llPayCarDepost.setVisibility(View.GONE);
            payFeesBy = PAY_FEES_BY_PAYNOW;
        } else {

        }
    }

    void setPaymentMethod(List<ComFeature> comFeatures) {
        for (int i = 0; i < comFeatures.size(); i++) {
            if (comFeatures.get(i).getModuleName() != null && comFeatures.get(i).getModuleName().equalsIgnoreCase("BPM_Fees")) {
                feesPaymentTpe = comFeatures.get(i).getStatus();
            } else if (comFeatures.get(i).getModuleName() != null && comFeatures.get(i).getModuleName().equalsIgnoreCase("BPM_Deposit_Amount")) {
                depositPaymentTpe = comFeatures.get(i).getStatus();
            }
        }
        binding.llCardPayFees.setVisibility(View.GONE);
        binding.llPayNowFees.setVisibility(View.GONE);
        binding.llPayCarDepost.setVisibility(View.GONE);
        binding.llPayNowDepost.setVisibility(View.GONE);
        binding.llCbPayCardDeposit.setVisibility(View.GONE);
        binding.llCbPayNowfees.setVisibility(View.GONE);
        binding.llCbCardFees.setVisibility(View.GONE);
        binding.llCbPaynowDeposit.setVisibility(View.GONE);
        // If both fees and deposit have the same type, only show the deposit layout
        if (feesPaymentTpe.equals(depositPaymentTpe)) {
            // Handle visibility based on depositPaymentTpe
            if (depositPaymentTpe.equals("0")) {
                // Show card for deposit and hide PayNow
                binding.tvPayDepost.setText("Payment Method");
                binding.tvTitlePayFess.setVisibility(View.GONE);
                binding.llPayCarDepost.setVisibility(View.VISIBLE);
                binding.llCbPayCardDeposit.setVisibility(View.GONE);
                binding.llPayNowDepost.setVisibility(View.GONE);
                payFeesBy = PAY_FEES_BY_CARD;
                payDeposityBy = PAY_DEPOSIT_BY_CARD;
            }
            if (depositPaymentTpe.equals("1")) {
                binding.tvPayDepost.setText("Payment Method");
                binding.tvTitlePayFess.setVisibility(View.GONE);
                // Show PayNow for deposit and hide card
                binding.llPayNowDepost.setVisibility(View.VISIBLE);
                binding.llCbPaynowDeposit.setVisibility(View.GONE);
                binding.llPayCarDepost.setVisibility(View.GONE);
                payFeesBy = PAY_FEES_BY_PAYNOW;
                payDeposityBy = PAY_DEPOSIT_BY_PAYNOW;
            } if (depositPaymentTpe.equals("2")) {
                // Show both card and PayNow for deposit
                binding.tvTitlePayFess.setText("How whould you like to pay Fees?");
                // Show both card and PayNow for fees
                binding.llCardPayFees.setVisibility(View.VISIBLE);
                binding.llPayNowFees.setVisibility(View.VISIBLE);
                binding.llCbCardFees.setVisibility(View.VISIBLE);
                binding.llCbPayNowfees.setVisibility(View.VISIBLE);

                binding.tvPayDepost.setText("How whould you like to pay Deposit?");
                // Show both card and PayNow for deposit
                binding.llPayCarDepost.setVisibility(View.VISIBLE);
                binding.llPayNowDepost.setVisibility(View.VISIBLE);
                binding.llCbPaynowDeposit.setVisibility(View.VISIBLE);
                binding.llCbPayCardDeposit.setVisibility(View.VISIBLE);
            }
        } else {
            // Handle visibility based on feesPaymentTpe
            if (feesPaymentTpe.equals("0")) {
                binding.tvTitlePayFess.setText("Pay fee by card");
                // Show card for fees and hide PayNow
                binding.llCardPayFees.setVisibility(View.VISIBLE);
                binding.llCbCardFees.setVisibility(View.GONE);
                binding.llPayNowFees.setVisibility(View.GONE);
                payFeesBy = PAY_FEES_BY_CARD;
            } else if (feesPaymentTpe.equals("1")) {
                binding.tvTitlePayFess.setText("Pay fee by paynow");
                // Show PayNow for fees and hide card
                binding.llPayNowFees.setVisibility(View.VISIBLE);
                binding.llCbPaynowDeposit.setVisibility(View.GONE);
                binding.llCardPayFees.setVisibility(View.GONE);
                payFeesBy = PAY_FEES_BY_PAYNOW;
            } else if (feesPaymentTpe.equals("2")) {
                binding.tvTitlePayFess.setText("How whould you like to pay Fees?");
                // Show both card and PayNow for fees
                binding.llCardPayFees.setVisibility(View.VISIBLE);
                binding.llPayNowFees.setVisibility(View.VISIBLE);
                binding.llCbCardFees.setVisibility(View.VISIBLE);
                binding.llCbPayNowfees.setVisibility(View.VISIBLE);
            }

            // Handle visibility based on depositPaymentTpe
            if (depositPaymentTpe.equals("0")) {
                binding.tvPayDepost.setText("Pay deposit by card");
                // Show card for deposit and hide PayNow
                binding.llPayCarDepost.setVisibility(View.VISIBLE);
                binding.llCbCardFees.setVisibility(View.GONE);
                binding.llPayNowDepost.setVisibility(View.GONE);
                payDeposityBy = PAY_DEPOSIT_BY_CARD;
            } else if (depositPaymentTpe.equals("1")) {
                binding.tvPayDepost.setText("Pay deposit by paynow");
                // Show PayNow for deposit and hide card
                binding.llPayNowDepost.setVisibility(View.VISIBLE);
                binding.llCbPaynowDeposit.setVisibility(View.GONE);
                binding.llPayCarDepost.setVisibility(View.GONE);
                payDeposityBy = PAY_DEPOSIT_BY_PAYNOW;
            } else if (depositPaymentTpe.equals("2")) {
                binding.tvPayDepost.setText("How whould you like to pay Deposit?");
                // Show both card and PayNow for deposit
                binding.llPayCarDepost.setVisibility(View.VISIBLE);
                binding.llPayNowDepost.setVisibility(View.VISIBLE);
                binding.llCbPaynowDeposit.setVisibility(View.VISIBLE);
                binding.llCbPayCardDeposit.setVisibility(View.VISIBLE);
            }
        }
    }


    void setCardData(DefaultCardModel sucessRespnse) {
        binding.tvChangeCardFees.setText("Change");
        binding.tvChangeCardDeposit.setText("Change");
        setCardBrandImage(sucessRespnse.getPaymentMethod().getBrand(), binding.imgCard);
        setCardBrandImage(sucessRespnse.getPaymentMethod().getBrand(), binding.imgCardFees);
        binding.tvCardNumberFees.setText("Ending " + sucessRespnse.getPaymentMethod().getLast4());
        binding.tvCardNumberDepost.setText("Ending " + sucessRespnse.getPaymentMethod().getLast4());
    }

    public void setCardBrandImage(String brand, ImageView imageView) {
        // Default image for unknown brands
        int imageResId = R.drawable.no_card;

        // Check the card brand and set the corresponding image
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
                // If the brand doesn't match any of the above, it defaults to the credit card image.
                imageResId = R.drawable.no_card;
                break;
        }

        // Set the image resource to the ImageView (assuming `img` is your ImageView)
        imageView.setImageResource(imageResId);
    }

    void createCustomerOnStripe() {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
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
                                    Log.e("ClientId", sucessRespnse.getCustomerID());

                                    SharePreference.getInstance(PaymentActivity.this).putString(STRIPE_CUSTOMER_ID, sucessRespnse.getCustomerID());
                                    getDefaultCard(sucessRespnse.getCustomerID());
                                }

                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }

    private void getDefaultCard(String customerID) {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getDefaultCardStripe(customerID, new RetrofitListener<DefaultCardModel>() {
                        @Override
                        public void onResponseSuccess(DefaultCardModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {

                                defaultCardId = sucessRespnse.getPaymentMethod().getId();
                                setCardData(sucessRespnse);

                            } else {
                                if (sucessRespnse.getStatusCode() == 404) {
                                    noDefaultCard(1);
                                    binding.tvChangeCardFees.setText("Add");
                                    binding.tvCardNumberFees.setText("No Card added");
                                    binding.imgCard.setImageResource(R.drawable.no_card);
                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }

    void addBookingSlot() {
        AddBookingSlotRequest request;
        if (slotId != null) {
            if (bookingDetails.getData().getAdvanceDeposit() != null || bookingDetails.getData().getAdvanceDeposit() != 0) {
                request = new AddBookingSlotRequest(Integer.parseInt(bookingDetails.getData().getRoomID()), Integer.parseInt(LOGIN_DETAIL.getAppuserID()), startDate, endDate, slotId, bookingDetails.getData().getFees(), bookingDetails.getData().getAdvanceDeposit());
            } else {
                request = new AddBookingSlotRequest(Integer.parseInt(bookingDetails.getData().getRoomID()), Integer.parseInt(LOGIN_DETAIL.getAppuserID()), startDate, endDate, slotId, bookingDetails.getData().getFees());
            }
        } else {
            if (bookingDetails.getData().getAdvanceDeposit() != null || bookingDetails.getData().getAdvanceDeposit() != 0) {
                request = new AddBookingSlotRequest(Integer.parseInt(bookingDetails.getData().getRoomID()), Integer.parseInt(LOGIN_DETAIL.getAppuserID()), startDate, endDate, bookingDetails.getData().getFees(), bookingDetails.getData().getAdvanceDeposit());
            } else {
                request = new AddBookingSlotRequest(Integer.parseInt(bookingDetails.getData().getRoomID()), Integer.parseInt(LOGIN_DETAIL.getAppuserID()), startDate, endDate, bookingDetails.getData().getFees());
            }
        }
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(PaymentActivity.this);
                    apiServiceProvider.addBookingSlot(request, new RetrofitListener<AddBookingSlotResponseModel>() {
                        @Override
                        public void onResponseSuccess(AddBookingSlotResponseModel sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatusCode() == 200) {
                                bookingID = sucessRespnse.getBookingID();
                                if (payDeposityBy.equals(PAY_DEPOSIT_BY_CARD) && payFeesBy.equals(PAY_FEES_BY_CARD)) {
                                    createPaymentWithDeposit();
                                }
                                if (payDeposityBy.equals(PAY_DEPOSIT_BY_PAYNOW) && payFeesBy.equals(PAY_FEES_BY_PAYNOW)) {
                                    payNowWithDeposit("");
                                }
                                if (payDeposityBy.equals(PAY_DEPOSIT_BY_CARD) && payFeesBy.equals(PAY_FEES_BY_PAYNOW) || payDeposityBy.equals(PAY_DEPOSIT_BY_PAYNOW) && payFeesBy.equals(PAY_FEES_BY_CARD)) {
                                    createPaymentWithDeposit();
                                }

//                                createPaymentWithDeposit();
//                                if (bookingDetails.getData().getAdvanceDeposit() != null || bookingDetails.getData().getAdvanceDeposit() != 0) {
//                                    if (payDeposityBy.equalsIgnoreCase(payFeesBy)){
//                                        if (payDeposityBy.equalsIgnoreCase(PAY_DEPOSIT_BY_CARD)){
//                                            createPaymentWithDeposit();
//                                        }else {
//                                            payNowWithDeposit();
//                                        }
//                                    }
//                                    else if (payFeesBy.equals(PAY_FEES_BY_CARD) && payDeposityBy.equals(PAY_DEPOSIT_BY_PAYNOW)){
//                                        createPaymentWithDeposit();
//                                    }
//                                    if (feesPaymentTpe.equalsIgnoreCase("2") && depositPaymentTpe.equalsIgnoreCase("2")) {
//                                        if (isCardPayFees && isCardPayDeposit) {
//                                            createPaymentWithDeposit();
//                                        } else if (isPaynowDeposit && isPaynowFees) {
//                                            payNowWithDeposit();
//                                        }
//                                    }
//
////                                    if (isCardPayDeposit) {
////                                        createPaymentWithDeposit();
////                                    } else {
////                                        payNowWithDeposit();
////                                    }
//                                } else {
////                                    if (isCardPayDeposit) {
////                                        createPaymentWithOutDeposit();
////                                    } else {
//                                        payNowWithOutDeposit();
////                                    }
//                                }
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    int deposit = 0;
    int fees = 0;
    int paymentFor = 0;

    void createPaymentWithDeposit() {

        if (defaultCardId != null && !defaultCardId.isEmpty()) {

            if (payDeposityBy.equals(PAY_DEPOSIT_BY_CARD) && payFeesBy.equals(PAY_FEES_BY_CARD)) {
                paymentFor = 2;
                deposit = bookingDetails.getData().getAdvanceDeposit();
                fees = bookingDetails.getData().getFees();
                paymentypeDeposit = 0;
                paymentypeFees = 0;
            } else {
                if (payDeposityBy.equals(PAY_DEPOSIT_BY_CARD)) {
                    paymentFor = 1;
                    fees = 0;
                    if (path != null) {
                        if (bookingSlotData.getDeposit() != null) {
                            deposit = Integer.parseInt(bookingSlotData.getDeposit());
                        } else {
                            deposit = 0;
                        }
                    } else {
                        deposit = bookingDetails.getData().getAdvanceDeposit();
                    }
                    paymentypeDeposit = 0;
                    paymentypeFees = 1;
                }
                if (payFeesBy.equals(PAY_FEES_BY_CARD)) {
                    paymentFor = 0;
                    deposit = 0;
                    if (path != null) {
                        if (bookingSlotData.getFees() != null) {
                            fees = outStandingAmt;
                        } else {
                            fees = 0;
                        }
                    } else {
                        fees = bookingDetails.getData().getFees();
                    }
                    paymentypeDeposit = 1;
                    paymentypeFees = 0;
                }
            }
            String roomName = "";
            if (path!=null){
                roomName = bookingSlotData.getRoomName();
            }else {
                roomName =  bookingDetails.getData().getRoomName();
            }
            CreatePaymentStripeRequest request = new CreatePaymentStripeRequest(SharePreference.getInstance(PaymentActivity.this).getString(STRIPE_CUSTOMER_ID), fees, defaultCardId, deposit, roomName, bookingID, paymentFor);
            Util.checkInternet(this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        apiServiceProvider.createPaymentStripe(request, new RetrofitListener<PaymentResponseModel>() {
                            @Override
                            public void onResponseSuccess(PaymentResponseModel sucessRespnse, String apiFlag) {
                                if (sucessRespnse.getStatusCode() == 200) {
                                    if (paymentFor == 2) {
                                        confirmBooking(sucessRespnse.getAmountIntent().getId(), sucessRespnse.getDepositIntent().getId());
                                    } else {
                                        if (paymentFor == 1) {
                                            payNowWithDeposit(sucessRespnse.getDepositIntent().getId());
                                        } else {
                                            payNowWithDeposit(sucessRespnse.getAmountIntent().getId());
                                        }
                                    }

                                } else {
                                    hideLoader();
                                    Toast.makeText(PaymentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                    cancelBookingSlot();
//                                   confirmBooking(sucessRespnse);
                                }
                            }

                            @Override
                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                                hideLoader();
                                try {
                                    Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        hideLoader();
                    }
                }
            });
        } else {
            noDefaultCard(0);
        }

    }

    private void payNowWithDeposit(String intentId) {
        int deposit = 0;
        int fees = 0;
        String paymentId = "";
        String depositId = "";

        if (payDeposityBy.equals(PAY_DEPOSIT_BY_PAYNOW) && payFeesBy.equals(PAY_FEES_BY_PAYNOW)) {
            deposit = bookingDetails.getData().getAdvanceDeposit();
            fees = bookingDetails.getData().getFees();
            paymentypeDeposit = 1;
            paymentypeFees = 1;
            paymentId = intentId;
            depositId = intentId;
        } else {
            if (payDeposityBy.equals(PAY_DEPOSIT_BY_PAYNOW)) {
                paymentypeDeposit = 1;
                paymentypeFees = 0;
                fees = 0;
                if (path != null) {
                    if (bookingSlotData.getDeposit() != null) {
                        deposit = Integer.parseInt(bookingSlotData.getDeposit());
                    } else {
                        deposit = 0;
                    }
                    depositId = bookingSlotData.getDepositPaymentID();
                    paymentId = bookingSlotData.getPaymentID();
                } else {
                    deposit = bookingDetails.getData().getAdvanceDeposit();
                    paymentId = intentId;
                }

            }
            if (payFeesBy.equals(PAY_FEES_BY_PAYNOW)) {
                paymentypeDeposit = 0;
                paymentypeFees = 1;
                deposit = 0;
                if (path != null) {
                    fees = Integer.parseInt(bookingSlotData.getFees());
                    depositId = bookingSlotData.getDepositPaymentID();
                    paymentId = bookingSlotData.getPaymentID();
                } else {
                    fees = bookingDetails.getData().getFees();
                    depositId = intentId;
                }
            }
        }
        String roomName = "";
        if (path != null) {
            roomName = bookingSlotData.getRoomName();
        } else {
            roomName = bookingDetails.getData().getRoomName();
        }
        PaymentType paymentTypeRequest = new PaymentType(paymentypeFees, paymentypeDeposit);
        PayNowRequest request = new PayNowRequest(SharePreference.getInstance(PaymentActivity.this).getString(STRIPE_CUSTOMER_ID), fees, deposit, roomName, bookingID, paymentId, paymentTypeRequest, depositId);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.payNow(request, new RetrofitListener<PayNowResponseModel>() {
                        @Override
                        public void onResponseSuccess(PayNowResponseModel sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                Intent intent = new Intent(PaymentActivity.this, PaymentWebView.class);
                                intent.putExtra("url", sucessRespnse.getPaymentIntent().getNext_action().getPaynowDisplayQrCode().getHostedInstructionsUrl());
                                addWebActivityResultLauncher.launch(intent);
//                                confirmPayment(sucessRespnse.getPaymentIntent().getClient_secret());
//                                confirmBooking(sucessRespnse);

                            } else {
                                hideLoader();
                                Toast.makeText(PaymentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                cancelBookingSlot();
//                                   confirmBooking(sucessRespnse);
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }


    void confirmBooking(String paymentID, String depositId) {
        PaymentType paymentTypeRequest = new PaymentType(paymentypeFees, paymentypeDeposit);

        Log.e("Naimish", "BookingID" + bookingID);
        paymentypeDeposit = 0;
        ConfirmBookingRequest request = new ConfirmBookingRequest(paymentID, bookingID, depositId, paymentTypeRequest);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.confirmBooking(request, new RetrofitListener<ConfirmBookingResponse>() {
                        @Override
                        public void onResponseSuccess(ConfirmBookingResponse sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                bookingSuccessDialog();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });

    }

//


    private void bookingSuccessDialog() {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.booking_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        // Find the buttons from the custom layout
        RelativeLayout rlOk = dialog.findViewById(R.id.rl_ok);
        TextView message = dialog.findViewById(R.id.tv_message);
        if (path != null) {
            message.setText("Your booking has been successfully rescheduled!. Your new Booking ID is" + bookingID + "." + "\nWe look forward to your visit.");
        } else {
            message.setText("Your booking was successful. Your Booking ID is" + bookingID + "." + "\nWe look forward to your visit.");
        }
        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constant.FROM_BOOKING = true;
                dialog.dismiss();
                Intent intent = new Intent(PaymentActivity.this, BookingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // Show the dialog
        dialog.show();
    }

    // 0 for no card
    // 1 for no default card
    //2 payment failed
    private void noDefaultCard(int from) {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.booking_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Find the buttons from the custom layout
        RelativeLayout rlOk = dialog.findViewById(R.id.rl_ok);
        TextView title = dialog.findViewById(R.id.tv_title);
        TextView btnText = dialog.findViewById(R.id.tv_btnText);
        TextView message = dialog.findViewById(R.id.tv_message);
        ImageView imageView = dialog.findViewById(R.id.img_icon);
        btnText.setText("Ok");
        imageView.setVisibility(View.GONE);
        if (from == 1) {
            title.setText("Card");
            message.setText("No default card found for this customer.");
        } else if (from == 0) {
            title.setText("Payment Required");
            message.setText("To proceed with your booking payment,\nplease select or add a card");
        } else if (from == 2) {
            title.setText("Payment Due");
            message.setText("Your payment was unsuccessful, and your room booking has been canceled. Please check your payment method and try again.");
        } else {
            title.setText("Payment Failed");
            message.setText("Your payment was unsuccessful, and your room booking has been canceled. Please check your payment method and try again.");
        }
        rlOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                Intent intent = new Intent(PaymentActivity.this, AddCardForBookingActivity.class);
//                startActivity(intent);
            }
        });
        // Show the dialog
        dialog.show();
    }

    void cancelBookingSlot() {
        CancelBookingRequest request = new CancelBookingRequest(bookingID);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    showLoader(PaymentActivity.this);
                    apiServiceProvider.cancelBookingSlot(request, new RetrofitListener<CancelBookingResponse>() {
                        @Override
                        public void onResponseSuccess(CancelBookingResponse sucessRespnse, String apiFlag) {
                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                Toast.makeText(PaymentActivity.this, sucessRespnse.getStatus(), Toast.LENGTH_LONG).show();
                                Log.e("CancelBooking", sucessRespnse.getStatus());
                            } else {
                                Toast.makeText(PaymentActivity.this, sucessRespnse.getStatus(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }


    void getBookedSlotDetail() {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.getBookingSlotDetail(String.valueOf(bookingID), new RetrofitListener<BookingSlotDetailResponse>() {
                        @Override
                        public void onResponseSuccess(BookingSlotDetailResponse sucessRespnse, String apiFlag) {
                            hideLoader();
                            String bookingStatus = getBookingStatusDescription(sucessRespnse.getData().get(0).getBookingStatus());
//                            if (bookingStatus)
                            if (sucessRespnse.getData().get(0).getBookingStatus().equalsIgnoreCase("1")) {
                                confirmBooking(sucessRespnse.getData().get(0).getPaymentID(), sucessRespnse.getData().get(0).getDepositPaymentID());
                            } else if (sucessRespnse.getData().get(0).getBookingStatus().equalsIgnoreCase("0")) {
                                noDefaultCard(2);
                            } else if (sucessRespnse.getData().get(0).getBookingStatus().equalsIgnoreCase("3")) {
                                noDefaultCard(3);
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            hideLoader();
                            try {
                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });

    }

}

//    void createPaymentWithOutDeposit() {
//        if (defaultCardId != null && !defaultCardId.isEmpty()) {
//            CreatePaymentStripeRequestWithoutDepost request = new CreatePaymentStripeRequestWithoutDepost(SharePreference.getInstance(PaymentActivity.this).getString(STRIPE_CUSTOMER_ID), outStandingAmt, defaultCardId, roomName, bookingID, 0);
//            Util.checkInternet(this, new Util.NetworkCheckCallback() {
//                @Override
//                public void onNetworkCheckComplete(boolean isAvailable) {
//                    if (isAvailable) {
//                        apiServiceProvider.createPaymentStripeWithOutDepost(request, new RetrofitListener<PaymentResponseModel>() {
//                            @Override
//                            public void onResponseSuccess(PaymentResponseModel sucessRespnse, String apiFlag) {
//                                if (sucessRespnse.getStatusCode() == 200) {
//                                    confirmBookingWithOutDepost(sucessRespnse.getAmountIntent().getId());
//
//                                } else {
//                                    hideLoader();
//                                    Toast.makeText(PaymentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
//                                    cancelBookingSlot();
////                                   confirmBooking(sucessRespnse);
//                                }
//                            }
//
//                            @Override
//                            public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                                hideLoader();
//                                try {
//                                    Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
//                                } catch (Exception e) {
//                                    Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//                    } else {
//                        hideLoader();
//                    }
//                }
//            });
//        } else {
//            noDefaultCard(0);
//        }
//
//    }
//private void payNowWithOutDeposit() {
//    PayNowWithoutDepositRequest request = new PayNowWithoutDepositRequest(SharePreference.getInstance(PaymentActivity.this).getString(STRIPE_CUSTOMER_ID), outStandingAmt, roomName, bookingID);
//    Util.checkInternet(this, new Util.NetworkCheckCallback() {
//        @Override
//        public void onNetworkCheckComplete(boolean isAvailable) {
//            if (isAvailable) {
//                apiServiceProvider.payNowWithOutDeposit(request, new RetrofitListener<PayNowResponseModel>() {
//                    @Override
//                    public void onResponseSuccess(PayNowResponseModel sucessRespnse, String apiFlag) {
//                        if (sucessRespnse.getStatusCode() == 200) {
//                            Intent intent = new Intent(PaymentActivity.this, PaymentWebView.class);
//                            intent.putExtra("url", sucessRespnse.getPaymentIntent().getNext_action().getPaynowDisplayQrCode().getHostedInstructionsUrl());
//                            addWebActivityResultLauncher.launch(intent);
////                                sucessRespnse.getPaymentIntent().getNext_action().getPaynowDisplayQrCode().getHostedInstructionsUrl();
////                                confirmPayment(sucessRespnse.getPaymentIntent().getClient_secret());
////                                confirmBooking(sucessRespnse);
//
//                        } else {
//                            hideLoader();
//                            Toast.makeText(PaymentActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
//                            cancelBookingSlot();
////                                   confirmBooking(sucessRespnse);
//                        }
//                    }
//
//                    @Override
//                    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                        hideLoader();
//                        try {
//                            Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//            } else {
//                hideLoader();
//            }
//        }
//    });
//}

//    void confirmBookingWithOutDepost(String id) {
//        Log.e("Naimish", "BookingID" + bookingID);
//        ConfirmBookingRequestWithoutDeposit request = new ConfirmBookingRequestWithoutDeposit(id, bookingID);
//        Util.checkInternet(this, new Util.NetworkCheckCallback() {
//            @Override
//            public void onNetworkCheckComplete(boolean isAvailable) {
//                if (isAvailable) {
//                    apiServiceProvider.confirmBookingWithout(request, new RetrofitListener<ConfirmBookingResponse>() {
//                        @Override
//                        public void onResponseSuccess(ConfirmBookingResponse sucessRespnse, String apiFlag) {
//                            hideLoader();
//                            if (sucessRespnse.getStatusCode() == 200) {
//                                bookingSuccessDialog();
//                            }
//                        }
//
//                        @Override
//                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                            hideLoader();
//                            try {
//                                Toast.makeText(PaymentActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
//                            } catch (Exception e) {
//                                Toast.makeText(PaymentActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    });
//                } else {
//                    hideLoader();
//                }
//            }
//        });
//
//    }