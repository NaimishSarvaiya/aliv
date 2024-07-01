package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iotsmartaliv.R;

/**
 * This activity class is used for adding the card.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class AddCardActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText card_no, holder_name, edt_exp, cvv;
    private ImageView imageView_back;
    private Button btnSubmit;
    private String strCardno = "", strCvv = "", strName = "", strExp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        initViews();
        initListeners();
    }

    /**
     * Initialize views
     */
    private void initViews() {
        btnSubmit = findViewById(R.id.btn_submit);
        card_no = findViewById(R.id.card_number);
        cvv = findViewById(R.id.edt_cvc);
        holder_name = findViewById(R.id.holder_name);
        edt_exp = findViewById(R.id.edt_exp);
        imageView_back = findViewById(R.id.img_back_add_card);
        strCardno = card_no.getText().toString().trim();
        strCvv = cvv.getText().toString().trim();
        strName = holder_name.getText().toString().trim();
        strExp = edt_exp.getText().toString();
    }

    /**
     * Initialize listeners.
     */
    private void initListeners() {
        btnSubmit.setOnClickListener(this);
        imageView_back.setOnClickListener(this);
        card_no.addTextChangedListener(new CreditCardNumberFormattingTextWatcher());
        edt_exp.addTextChangedListener(new ExpiryFormatting());
        if (edt_exp.length() >= 2) {
            edt_exp.append("/");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                strCardno = card_no.getText().toString();
                strCvv = cvv.getText().toString();
                strName = holder_name.getText().toString();
                strExp = edt_exp.getText().toString();

                if (strCardno.equalsIgnoreCase("") && strName.equalsIgnoreCase("") && strExp.equalsIgnoreCase("") && strCvv.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please enter all the details", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("cardNo", card_no.getText().toString());
                    intent.putExtra("expire_date", edt_exp.getText().toString().trim());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.img_back_add_card:
                finish();
                break;
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
                if (length == 5 && source.contains("-") && counter == 1) {
                    lock = true;
                } else if (length == 10 && source.contains("-") && counter == 2) {
                    lock = true;

                } else lock = length == 15 && source.contains("-") && counter == 3;
                //lock=true;
                if (lock)
                    stringBuilder.deleteCharAt(length - 1);
                else
                    stringBuilder.insert(length - 1, "-");
                card_no.setText(stringBuilder);
                card_no.setSelection(card_no.getText().length());
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
                edt_exp.setText(stringBuilder);
                edt_exp.setSelection(edt_exp.getText().length());
            }
        }
    }
}