package com.iotsmartaliv.activity.booking;

import static com.iotsmartaliv.constants.Constant.STRIPE_CUSTOMER_ID;
import static com.iotsmartaliv.constants.Constant.STRIPE_TEST_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.databinding.ActivityAddCardForBookingBinding;
import com.iotsmartaliv.model.booking.AttacheCardOnStripeRequest;
import com.iotsmartaliv.model.booking.AttachedCardResponseModel;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;

import java.util.Calendar;

public class AddCardForBookingActivity extends AppCompatActivity {

    ActivityAddCardForBookingBinding binding;
    private String strCardno = "", strCvv = "", strName = "", strExp = "";
    private ApiServiceProvider apiServiceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardForBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiServiceProvider = ApiServiceProvider.getInstance(this, true);
        initViews();
        initListeners();
    }

    private void initViews() {
        strCardno = binding.cardNumber.getText().toString().trim();
        strCvv = binding.edtCvc.getText().toString().trim();
        strName = binding.holderName.getText().toString().trim();
        strExp = binding.edtExp.getText().toString();
        binding.llHeader.tvHeader.setText("Card");
    }

    /**
     * Initialize listeners.
     */
    private void initListeners() {
        binding.rlAddCard.setOnClickListener(v -> {
            // Get values from input fields
            strCardno = binding.cardNumber.getText().toString().trim();
            strCvv = binding.edtCvc.getText().toString().trim();
            strName = binding.holderName.getText().toString().trim();
            strExp = binding.edtExp.getText().toString().trim();

            // Perform validations
            if (strCardno.isEmpty()) {
                binding.cardNumber.setError("Card number is required");
            } else if (strCardno.length() < 16) {
                binding.cardNumber.setError("Invalid card number");
            } else if (strName.isEmpty()) {
                binding.holderName.setError("Cardholder name is required");
            } else if (strExp.isEmpty()) {
                binding.edtExp.setError("Expiry date is required");
            } else if (!isExpiryDateValid(strExp)) {
                binding.edtExp.setError("Invalid expiry date");
            } else if (strCvv.isEmpty()) {
                binding.edtCvc.setError("CVC is required");
            } else if (strCvv.length() < 3) {
                binding.edtCvc.setError("Invalid CVC");
            } else {
                // If validation passes, create a payment method
                createPaymentMethod();
            }
        });

        // Add other listeners (Back button, text formatting, etc.)
        binding.llHeader.imgBack.setOnClickListener(v -> finish());
        binding.cardNumber.addTextChangedListener(new TextWatcher() {
            private int previousLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.cardNumber.removeTextChangedListener(this);

                String input = s.toString();
                int cursorPosition =  binding.cardNumber.getSelectionStart();

                // Remove spaces
                String digitsOnly = input.replaceAll("\\s", "");

                // Format the input
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digitsOnly.length(); i++) {
                    formatted.append(digitsOnly.charAt(i));
                    if ((i + 1) % 4 == 0 && (i + 1) != digitsOnly.length()) {
                        formatted.append(" ");
                    }
                }

                // Adjust cursor position
                int spacesUpToCursor = 0;
                for (int i = 0; i < cursorPosition; i++) {
                    if (input.charAt(i) == ' ') {
                        spacesUpToCursor++;
                    }
                }
                cursorPosition -= spacesUpToCursor;

                cursorPosition += (cursorPosition / 4);
                if (formatted.length() > 0 && formatted.charAt(formatted.length() - 1) == ' ' &&
                        digitsOnly.length() == previousLength - 1) {
                    formatted.deleteCharAt(formatted.length() - 1);
                }

                binding.cardNumber.setText(formatted.toString());
                binding.cardNumber.setSelection(Math.min(cursorPosition, formatted.length()));

                binding.cardNumber.addTextChangedListener(this);
            }
        });

//        binding.cardNumber.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        binding.edtExp.addTextChangedListener(new ExpiryFormatting());
    }

    /**
     * Create a payment method using the card details entered.
     */
    public void createPaymentMethod() {
        // Get the card details from input fields
        String cardNumber = binding.cardNumber.getText().toString().trim();
        String expiryDate = binding.edtExp.getText().toString().trim();
        String cvc = binding.edtCvc.getText().toString().trim();
        String postalCode = "";  // Add a postal code if needed

        if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvc.isEmpty()) {
            // Show error to the user for missing fields
            return;
        }

        // Split the expiry date into month and year
        String[] expiryComponents = expiryDate.split("/");
        if (expiryComponents.length != 2) {
            binding.edtExp.setError("Invalid expiry date format");
            return;
        }

        // Extract month and year from expiryComponents
        int expMonth = Integer.parseInt(expiryComponents[0]);
        int expYear = Integer.parseInt(expiryComponents[1]);

        // Create a PaymentMethodCreateParams.Card with the card details
        PaymentMethodCreateParams.Card card = new PaymentMethodCreateParams.Card.Builder()
                .setNumber(cardNumber)
                .setCvc(cvc)
                .setExpiryMonth(expMonth)
                .setExpiryYear(expYear)
                .build();

        // Create the PaymentMethodCreateParams
        PaymentMethodCreateParams paymentMethodParams = PaymentMethodCreateParams.create(card, null);

        // Call the Stripe API to create the PaymentMethod
        Stripe stripe = new Stripe(getApplicationContext(), STRIPE_TEST_KEY);

        stripe.createPaymentMethod(
                paymentMethodParams,
                new ApiResultCallback<PaymentMethod>() {
                    @Override
                    public void onSuccess(PaymentMethod paymentMethod) {
                        // PaymentMethod was created successfully
                        String paymentMethodId = paymentMethod.id;
                        Log.e("paymentId", paymentMethodId);
//                        Toast.makeText(AddCardForBookingActivity.this,paymentMethodId,Toast.LENGTH_LONG).show();
//                        savePaymentMethodToBackend(paymentMethodId);
                        addCardToServer(paymentMethodId);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(AddCardForBookingActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    // Check if expiry date is valid
    private boolean isExpiryDateValid(String expiryDate) {
        String[] parts = expiryDate.split("/");
        if (parts.length != 2) {
            return false;
        }
        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);
            return month >= 1 && month <= 12 && year >= Calendar.getInstance().get(Calendar.YEAR) % 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * This class is used for checking credit card number format.
     */
    private class CreditCardNumberFormattingTextWatcher implements TextWatcher {
        private boolean lock;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String source = editable.toString();
            int length = source.length();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source);

            int counter = 0;
            for (int i = 0; i < source.length(); i++) {
                if (source.charAt(i) == ' ') {
                    counter++;
                }
            }
            if (length > 0 && length % 5 == 0) {
                if (lock) {
                    stringBuilder.deleteCharAt(length - 1);
                } else {
                    stringBuilder.insert(length - 1, " ");
                }
                binding.cardNumber.setText(stringBuilder);
                binding.cardNumber.setSelection(binding.cardNumber.getText().length());
            }
        }
    }

    /**
     * This class is for credit card expiry date format.
     */
    private class ExpiryFormatting implements TextWatcher {
        private boolean lock;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String source = editable.toString();
            int length = source.length();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(source);

            int counter = 0;
            for (int i = 0; i < source.length(); i++) {
                if (source.charAt(i) == '/') {
                    counter++;
                }
            }
            if (length > 0 && length % 3 == 0) {
                lock = length == 3 && source.contains("/") && counter == 1;
                if (lock) {
                    stringBuilder.deleteCharAt(length - 1);
                } else {
                    stringBuilder.insert(length - 1, "/");
                }
                binding.edtExp.setText(stringBuilder);
                binding.edtExp.setSelection(binding.edtExp.getText().length());
            }
        }
    }

    void addCardToServer(String paymentMethodId) {
        Util.checkInternet(this, new Util.NetworkCheckCallback() {

            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    AttacheCardOnStripeRequest requestBody = new AttacheCardOnStripeRequest(paymentMethodId, SharePreference.getInstance(AddCardForBookingActivity.this).getString(STRIPE_CUSTOMER_ID));
                    apiServiceProvider.attachPaymentMethodStripe(requestBody, new RetrofitListener<AttachedCardResponseModel>() {
                        @Override
                        public void onResponseSuccess(AttachedCardResponseModel sucessRespnse, String apiFlag) {
//                            hideLoader();
                            if (sucessRespnse.getStatusCode() == 200) {
                                if (sucessRespnse.getPaymentMethod() != null) {
                                    Toast.makeText(AddCardForBookingActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                                    Intent resultIntent = new Intent();
                                    setResult(RESULT_OK, resultIntent);  // Set result as successful
                                    finish();  // Close the activity and return to the previous one
                                }
                            }else {
                                Toast.makeText(AddCardForBookingActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
                            try {
                                Toast.makeText(AddCardForBookingActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(AddCardForBookingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
//                    hideLoader();
                }
            }
        });
    }
}
