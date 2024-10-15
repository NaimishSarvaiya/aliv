package com.iotsmartaliv.dialog_box;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.CountryCodeDialogAdapter;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.Country;
import com.iotsmartaliv.apiAndSocket.models.CountryArrayData;
import com.iotsmartaliv.apiAndSocket.models.DefaultCountryData;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 6/8/19 :August.
 */
public class AddVisitorDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Dialog dialog;
    EditText edtName, edContactNumber, edtLinsencePlate;
    TextView tvCountryCode;
    Button buttonAdd;
    CustomCountryCodeDialog customCountryCodeDialog;
    List<DefaultCountryData> defaultCountryDataList = new ArrayList<>();
    String countryId;
    Country country;
    CountryArrayData countryArrayData;
    VisitorAddedRefresh visitorAddedRefresh;

    ApiServiceProvider apiServiceProvider;

    String countryISO = "";

    public AddVisitorDialog(Activity activity, VisitorAddedRefresh visitorAddedRefresh, CountryArrayData countryArrayData) {
        super(activity);
        this.activity = activity;
        this.countryArrayData = countryArrayData;
        apiServiceProvider = ApiServiceProvider.getInstance(activity,false);
        this.visitorAddedRefresh = visitorAddedRefresh;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAdd:
                if (AllFieldsValidation()) {
                    TimeZone tz = TimeZone.getDefault();
                    System.out.println(" Timezon id :: " + tz.getID());
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("appuser_ID", LOGIN_DETAIL.getAppuserID());
                    hashMap.put("visitors[0][name]", edtName.getText().toString().trim());
                    hashMap.put("visitors[0][country_code]", countryId);
                    hashMap.put("visitors[0][contact]", tvCountryCode.getText().toString().trim() + edContactNumber.getText().toString().trim());
                    hashMap.put("visitors[0][license_plate]",edtLinsencePlate.getText().toString());
//                   if (edtLinsencePlate.getText().toString().isEmpty()){
//                       hashMap.put("visitors[0][license_plate]","");
//
//                   }else {
//                       hashMap.put("visitors[0][license_plate]",edtLinsencePlate.getText().toString());
//                   }

                    //hashMap.put("group_ID",);
                    apiServiceProvider.callForAddVisitor(hashMap, new RetrofitListener<SuccessResponse>() {
                        @Override
                        public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
                            if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                                Toast.makeText(activity, "Visitor Added Successfully.", Toast.LENGTH_SHORT).show();
                                visitorAddedRefresh.visitorAddedRefreshNotify();
                                dismiss();
                            } else {
                                Toast.makeText(activity, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {

                        }
                    });
                }
                break;
        }
    }

    private boolean AllFieldsValidation() {
        if (edtName.getText().toString().trim().isEmpty() || edtName.getText().toString().equalsIgnoreCase("") ){
            edtName.setError("Enter Visitor Name.");
            edtName.requestFocus();
            return false;
        }else if (edContactNumber.getText().toString().trim().isEmpty() || edContactNumber.getText().toString().equalsIgnoreCase("")){
            edContactNumber.setError("Enter Contact Number.");
            edContactNumber.requestFocus();
            return  false;
        }else if (!Util.isValidPhoneNumber(edContactNumber.getText().toString(), countryISO)) {
            edContactNumber.setError("Please enter valid Contact Number.");
            edContactNumber.requestFocus();
            return false;
        }else if (edtLinsencePlate.getText().toString().trim().isEmpty() || edtLinsencePlate.getText().toString().equalsIgnoreCase("")){
            edtLinsencePlate.setError("Enter Licence plat");
            edtLinsencePlate.requestFocus();
            return false;
        }else {
            return true;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_visitor);
        Window window = getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        edtName = findViewById(R.id.edt_name);
        tvCountryCode = findViewById(R.id.tv_country_code);
        edContactNumber = findViewById(R.id.edt_contact_number);
        edtLinsencePlate = findViewById(R.id.edt_linsence_plate);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);
        defaultCountryDataList = countryArrayData.getDefaultCountry();
        if (defaultCountryDataList.size() > 0) {
            tvCountryCode.setText(defaultCountryDataList.get(0).getPhonecode());
            countryId = defaultCountryDataList.get(0).getId();
            countryISO = defaultCountryDataList.get(0).getPhonecode();
        } else {
            tvCountryCode.setText(countryArrayData.getCountry().get(0).getPhonecode());
            countryId = countryArrayData.getCountry().get(0).getId();
            countryISO = countryArrayData.getCountry().get(0).getPhonecode();
        }
        tvCountryCode.setOnClickListener(v -> {
            CountryCodeDialogAdapter countryCodeDialogAdapter = new CountryCodeDialogAdapter(countryArrayData.getCountry(), data -> {
                tvCountryCode.setText(data.getPhonecode());
                country = data;
                countryISO = country.getPhonecode();
                countryId = country.getId();
                customCountryCodeDialog.dismiss();
            });
            customCountryCodeDialog = new CustomCountryCodeDialog(activity, countryCodeDialogAdapter, countryArrayData.getCountry());
            customCountryCodeDialog.setCanceledOnTouchOutside(false);
            customCountryCodeDialog.show();
        });


    }


    public interface VisitorAddedRefresh {
        void visitorAddedRefreshNotify();
    }
}
