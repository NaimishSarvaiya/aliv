package com.iotsmartaliv.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iotsmartaliv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This activity class is used for show fault devices.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class FaultActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.textViewDate)
    TextView textViewDate;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.textViewTime)
    TextView textViewTime;
    @BindView(R.id.textViewAddress)
    TextView textViewAddress;
    @BindView(R.id.addressEditText)
    EditText addressEditText;
    @BindView(R.id.book_service)
    Button bookService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault);
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