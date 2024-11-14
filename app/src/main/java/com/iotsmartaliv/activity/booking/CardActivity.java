package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.STRIPE_CUSTOMER_ID;
import static com.iotsmartaliv.constants.Constant.hideLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.NewMainActivity;
import com.iotsmartaliv.adapter.booking.CardAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityCardBinding;
import com.iotsmartaliv.model.booking.CreateCustomerOnStripRequest;
import com.iotsmartaliv.model.booking.CreateCustomerResponse;
import com.iotsmartaliv.model.booking.CustomerCardRequestBody;
import com.iotsmartaliv.model.booking.CustomerCardsResponse;
import com.iotsmartaliv.model.booking.DeleteCardModel;
import com.iotsmartaliv.model.booking.DeleteCardRequestModel;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.model.booking.SetDefaultCardResponseModel;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity implements CardAdapter.OnItemClickListener, CardAdapter.OnDeleteClickListener {
    ActivityCardBinding binding;
    CardAdapter adapter;
    private ApiServiceProvider apiServiceProvider;
    private List<PaymentMethodModel> cardList = new ArrayList<>();
    private ActivityResultLauncher<Intent> addCardActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this, true);
        init();
    }
    void init() {
//        binding.layoutNoData.tvTitle.setText(R.string.we_couldn_t_find_any_result);
//        binding.layoutNoData.tvDetail.setText(R.string.we_couldn_t_locate_any_relevant_results_in_your_communities_right_now_please_try_again_later);
//       binding.layoutNoData.llNoFeedback.setVisibility(View.VISIBLE);
//       binding.rvBooking.setVisibility(View.GONE);
        binding.rvCard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardAdapter(this,cardList,this,this);
        binding.rvCard.setAdapter(adapter);
        binding.llToolbar.tvHeader.setText("Card");
        binding.fabAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(CardActivity.this, AddCardForBookingActivity.class);
            addCardActivityResultLauncher.launch(intent);
        });
        binding.llToolbar.imgBack.setOnClickListener(v -> {
           onBackPressed();
        });

        getCard();
        // Initialize the ActivityResultLauncher for adding a card
        addCardActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // The card was successfully created, refresh the card list
                        getCard();
                    }
                }
        );
    }
    void getCard(){
        if (SharePreference.getInstance(CardActivity.this).getString(STRIPE_CUSTOMER_ID) == null || SharePreference.getInstance(CardActivity.this).getString(STRIPE_CUSTOMER_ID).equalsIgnoreCase("")) {

            createCustomerOnStripe();
        } else {
            getCardList();
        }

    }

    void getCardList(){
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    CustomerCardRequestBody requestBody = new CustomerCardRequestBody(SharePreference.getInstance(CardActivity.this).getString(STRIPE_CUSTOMER_ID));
                    apiServiceProvider.getCustomerCardStripe(requestBody, new RetrofitListener<CustomerCardsResponse>() {
                        @Override
                        public void onResponseSuccess(CustomerCardsResponse sucessRespnse, String apiFlag) {
//                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getCards() != null && sucessRespnse.getCards().size()!=0) {
                                    binding.tvNoCard.setVisibility(View.GONE);
                                    List<PaymentMethodModel> newCardList = sucessRespnse.getCards();
                                    adapter.updateData(newCardList);
                                    Log.e("ClientId", String.valueOf(sucessRespnse.getCards().size()));
//                                    SharePreference.getInstance(CardActivity.this).putString(STRIPE_CUSTOMER_ID, sucessRespnse.getCustomerID());
//                                    getDefaultCard(sucessRespnse.getCustomerID());
                                }else {
                                    binding.tvNoCard.setVisibility(View.VISIBLE);
                                }
                            }else {
                                Toast.makeText(CardActivity.this,sucessRespnse.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(CardActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position, PaymentMethodModel card) {
        setAdDefaultDialog(card);

    }

    @Override
    public void onDeleteClick(int position, PaymentMethodModel card) {
        showDeleteCardDialog(position,card);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10){
            getCard();
        }
    }

    void deleteCard(int position,PaymentMethodModel card){
        Util.checkInternet(this, new Util.NetworkCheckCallback() {

            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    DeleteCardRequestModel requestBody = new DeleteCardRequestModel(SharePreference.getInstance(CardActivity.this).getString(STRIPE_CUSTOMER_ID),card.getId());
                    apiServiceProvider.deleteCard(requestBody, new RetrofitListener<DeleteCardModel>() {
                        @Override
                        public void onResponseSuccess(DeleteCardModel sucessRespnse, String apiFlag) {
//                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                adapter.deleteItem(position);
                                getCard();
                            }else {
                                Toast.makeText(CardActivity.this,sucessRespnse.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(CardActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }
    void setDefaultCard(PaymentMethodModel card){
        Util.checkInternet(this, new Util.NetworkCheckCallback() {

            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    DeleteCardRequestModel requestBody = new DeleteCardRequestModel(SharePreference.getInstance(CardActivity.this).getString(STRIPE_CUSTOMER_ID),card.getId());
                    apiServiceProvider.setDefaultCard(requestBody, new RetrofitListener<SetDefaultCardResponseModel>() {
                        @Override
                        public void onResponseSuccess(SetDefaultCardResponseModel sucessRespnse, String apiFlag) {
//                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                Toast.makeText(CardActivity.this, sucessRespnse.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(CardActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }

    private void showDeleteCardDialog(int position, PaymentMethodModel card) {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog_for_booking);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Get the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        // Find the buttons from the custom layout
        TextView btnNotNow = dialog.findViewById(R.id.btn_not_now);
        TextView btnConfirm = dialog.findViewById(R.id.btn_confirm);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Delete!");
        // Set click listeners for the buttons
        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCard(position,card);
                dialog.dismiss();
                // Handle the confirmation action here
               // Dismiss the dialog after confirming
            }
        });

        // Show the dialog
        dialog.show();
    }

    private void setAdDefaultDialog(PaymentMethodModel card) {
        // Create a new dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirmation_dialog_for_booking);

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        // Get the screen width
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        // Set dialog width to 80% of screen width
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = (int) (screenWidth * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        // Find the buttons from the custom layout
        TextView btnNotNow = dialog.findViewById(R.id.btn_not_now);
        TextView btnConfirm = dialog.findViewById(R.id.btn_confirm);
        TextView message = dialog.findViewById(R.id.message);
        TextView title = dialog.findViewById(R.id.title);
        title.setText("Default Card!");
        message.setText("Do you want to set this card as your\n default card?");
        btnNotNow.setText("No");
        btnConfirm.setText("Yes");

        // Set click listeners for the buttons
        btnNotNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Dismiss the dialog when "Not Now" is clicked
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultCard(card);
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }

    void createCustomerOnStripe() {
//        showLoader(PaymentActivity.this);
        Util.checkInternet(CardActivity.this, new Util.NetworkCheckCallback() {
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
                                    SharePreference.getInstance(CardActivity.this).putString(STRIPE_CUSTOMER_ID, sucessRespnse.getCustomerID());
                                    getCardList();
                                }

                            }else {
                                Toast.makeText(CardActivity.this,sucessRespnse.getMsg(),Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(CardActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(CardActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getStringExtra(Constant.PATH )!=null){
            startActivity(new Intent(CardActivity.this, NewMainActivity.class));
            finish();
        }else {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);  // Set result as successful
            finish();
        }
    }
}