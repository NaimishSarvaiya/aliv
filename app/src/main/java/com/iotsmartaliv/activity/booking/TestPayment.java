package com.iotsmartaliv.activity.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iotsmartaliv.R;
import com.iotsmartaliv.databinding.ActivityTestPaymentBinding;
import com.iotsmartaliv.model.booking.ComFeature;

import java.util.List;

public class TestPayment extends AppCompatActivity {
    ActivityTestPaymentBinding binding;
    String feesPaymentTpe;
    String depositPaymentTpe;
    boolean isCardPayFees = false;
    boolean isPaynowFees = false;
    boolean isCardPayDeposit = false;
    boolean isPaynowDeposit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.checkboxPaynowFees.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxCardFees.setChecked(false);
            }
        });

        binding.checkboxCardFees.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxPaynowFees.setChecked(false);
            }
        });

        // Checkbox listeners for deposit to ensure mutual exclusivity
        binding.checkboxPaynowDeposit.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxCardDepost.setChecked(false);
            }
        });

        binding.checkboxCardDepost.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.checkboxPaynowDeposit.setChecked(false);
            }
        });
        // Example initialization; set dynamically in your app
          // Set dynamically as needed
        binding.rlAdd.setOnClickListener(v -> {
            feesPaymentTpe = binding.etFees.getText().toString();  // Set dynamically as needed
            depositPaymentTpe = binding.etDeposit.getText().toString();
            setPaymentMethod();
            // Checkbox listeners for fees to ensure mutual exclusivity


//            boolean isFeesValid = false;
//            boolean isDepositValid = false;
//
//            // Check fees selection
//            if (feesPaymentTpe.equals("2")) {
//                isFeesValid = binding.checkboxPaynowFees.isChecked() || binding.checkboxCardFees.isChecked();
//                if (!isFeesValid) {
//                    Toast.makeText(this, "Please select a payment method for Fees.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            } else {
//                isFeesValid = true; // Auto-selection for cases with one option
//            }
//
//            // Check deposit selection
//            if (depositPaymentTpe.equals("2")) {
//                isDepositValid = binding.checkboxPaynowDeposit.isChecked() || binding.checkboxCardDepost.isChecked();
//                if (!isDepositValid) {
//                    Toast.makeText(this, "Please select a payment method for Deposit.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            } else {
//                isDepositValid = true; // Auto-selection for cases with one option
//            }
//
//            // Display selected payment methods
//            StringBuilder paymentMessage = new StringBuilder();
//            if (binding.checkboxCardFees.isChecked()) {
//                paymentMessage.append("Fees: Card\n");
//            } else if (binding.checkboxPaynowFees.isChecked()) {
//                paymentMessage.append("Fees: PayNow\n");
//            }
//
//            if (binding.checkboxCardDepost.isChecked()) {
//                paymentMessage.append("Deposit: Card");
//            } else if (binding.checkboxPaynowDeposit.isChecked()) {
//                paymentMessage.append("Deposit: PayNow");
//            }
//
//            Toast.makeText(this, paymentMessage.toString(), Toast.LENGTH_SHORT).show();
        });
        binding.rlConfirmBooking.setOnClickListener(v -> {
            boolean isFeesValid = false;
            boolean isDepositValid = false;

            // Validate fees selection
            if (feesPaymentTpe.equals("2")) {
                isFeesValid = binding.checkboxPaynowFees.isChecked() || binding.checkboxCardFees.isChecked();
                if (!isFeesValid) {
                    Toast.makeText(this, "Please select a payment method for Fees.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (feesPaymentTpe.equals("0")) {
                isFeesValid = true; // Card is auto-selected
            } else if (feesPaymentTpe.equals("1")) {
                isFeesValid = true; // PayNow is auto-selected
            }

            // Validate deposit selection
            if (depositPaymentTpe.equals("2")) {
                isDepositValid = binding.checkboxPaynowDeposit.isChecked() || binding.checkboxCardDepost.isChecked();
                if (!isDepositValid) {
                    Toast.makeText(this, "Please select a payment method for Deposit.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else if (depositPaymentTpe.equals("0")) {
                isDepositValid = true; // Card is auto-selected
            } else if (depositPaymentTpe.equals("1")) {
                isDepositValid = true; // PayNow is auto-selected
            }

            // Display selected payment methods
            StringBuilder paymentMessage = new StringBuilder();
            if (binding.checkboxCardFees.isChecked()) {
                paymentMessage.append("Fees: Card\n");
            } else if (binding.checkboxPaynowFees.isChecked()) {
                paymentMessage.append("Fees: PayNow\n");
            }

            if (binding.checkboxCardDepost.isChecked()) {
                paymentMessage.append("Deposit: Card");
            } else if (binding.checkboxPaynowDeposit.isChecked()) {
                paymentMessage.append("Deposit: PayNow");
            }

            // Show the payment method selection
            Toast.makeText(this, paymentMessage.toString(), Toast.LENGTH_SHORT).show();
        });
       // Call the function to set the payment method
    }

    void setPaymentMethod() {
        // Initially hide all layouts
        binding.llCardPayFees.setVisibility(View.GONE);
        binding.llPayNowFees.setVisibility(View.GONE);
        binding.llPayCarDepost.setVisibility(View.GONE);
        binding.llPayNowDepost.setVisibility(View.GONE);
        binding.llCbPayCardDeposit.setVisibility(View.GONE);
        binding.llCbPayNowfees.setVisibility(View.GONE);
        binding.llCbCardFees.setVisibility(View.GONE);
        binding.llCbPaynowDeposit.setVisibility(View.GONE);

        // Handle visibility based on feesPaymentTpe
        if (feesPaymentTpe.equals("0")) {
            // Show card for fees and hide PayNow
            binding.llCardPayFees.setVisibility(View.VISIBLE);
            binding.llCbCardFees.setVisibility(View.GONE);
            binding.llPayNowFees.setVisibility(View.GONE);
        } else if (feesPaymentTpe.equals("1")) {
            // Show PayNow for fees and hide card
            binding.llPayNowFees.setVisibility(View.VISIBLE);
            binding.llCbPaynowDeposit.setVisibility(View.GONE);
            binding.llCardPayFees.setVisibility(View.GONE);
        } else if (feesPaymentTpe.equals("2")) {
            // Show both card and PayNow for fees
            binding.llCardPayFees.setVisibility(View.VISIBLE);
            binding.llPayNowFees.setVisibility(View.VISIBLE);
            binding.llCbCardFees.setVisibility(View.VISIBLE);
            binding.llCbPayNowfees.setVisibility(View.VISIBLE);
        }

        // Handle visibility based on depositPaymentTpe
        if (depositPaymentTpe.equals("0")) {
            // Show card for deposit and hide PayNow
            binding.llPayCarDepost.setVisibility(View.VISIBLE);
            binding.llCbCardFees.setVisibility(View.GONE);
            binding.llPayNowDepost.setVisibility(View.GONE);
        } else if (depositPaymentTpe.equals("1")) {
            // Show PayNow for deposit and hide card
            binding.llPayNowDepost.setVisibility(View.VISIBLE);
            binding.llCbPaynowDeposit.setVisibility(View.GONE);
            binding.llPayCarDepost.setVisibility(View.GONE);
        } else if (depositPaymentTpe.equals("2")) {
            // Show both card and PayNow for deposit
            binding.llPayCarDepost.setVisibility(View.VISIBLE);
            binding.llPayNowDepost.setVisibility(View.VISIBLE);
            binding.llCbPaynowDeposit.setVisibility(View.VISIBLE);
            binding.llCbPayCardDeposit.setVisibility(View.VISIBLE);
        }

    }
}
