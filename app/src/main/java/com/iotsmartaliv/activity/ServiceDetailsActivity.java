package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.iotsmartaliv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity class is used for service details  .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class ServiceDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textViewIdLabel)
    TextView textViewIdLabel;
    @BindView(R.id.serviceTv)
    TextView serviceTv;
    @BindView(R.id.textViewSchLabel)
    TextView textViewSchLabel;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.addressEditText)
    TextView addressEditText;
    @BindView(R.id.textViewStaLabel)
    TextView textViewStaLabel;
    @BindView(R.id.buttonStatus)
    Button buttonStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
