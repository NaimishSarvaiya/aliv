package com.iotsmartaliv.adapter.booking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.AddCardActivity;
import com.iotsmartaliv.databinding.ActivityAddCardForBookingBinding;

public class AddCardForBookingActivity extends AppCompatActivity {

    ActivityAddCardForBookingBinding binding;
    private String strCardno = "", strCvv = "", strName = "", strExp = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCardForBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
            strCardno = binding.cardNumber.getText().toString();
            strCvv = binding.edtCvc.getText().toString();
            strName = binding.holderName.getText().toString();
            strExp = binding.edtExp.getText().toString();

            if (strCardno.equalsIgnoreCase("") && strName.equalsIgnoreCase("") && strExp.equalsIgnoreCase("") && strCvv.equalsIgnoreCase("")) {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("cardNo", binding.cardNumber.getText().toString());
                intent.putExtra("expire_date", binding.edtExp.getText().toString().trim());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        binding.llHeader.imgBack.setOnClickListener(v -> {
            finish();
        });
        binding.cardNumber.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        binding.edtExp.addTextChangedListener(new ExpiryFormatting());
        if (binding.edtExp.length() >= 2) {
            binding.edtExp.append("/");
        }
    }

    /**
     * This class is used for check credit card number format.
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
                if (source.charAt(i) == '-') {
                    counter++;
                }
            }
            if (length > 0 && length % 5 == 0) {
                if (length == 5 && source.contains(" ") && counter == 1) {
                    lock = true;
                } else if (length == 10 && source.contains(" ") && counter == 2) {
                    lock = true;

                } else lock = length == 15 && source.contains(" ") && counter == 3;
                //lock=true;
                if (lock)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, " ");
                binding.cardNumber.setText(stringBuilder);
                binding.cardNumber.setSelection(binding.cardNumber.getText().length());
            }
        }
    }

    /**
     * This class is for credit cards expiry Format
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
                //lock=true;
                if (lock)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, "/");
                binding.edtExp.setText(stringBuilder);
                binding.edtExp.setSelection(binding.edtExp.getText().length());
            }
        }
    }
}