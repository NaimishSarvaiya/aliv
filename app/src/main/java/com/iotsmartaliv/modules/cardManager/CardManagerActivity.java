package com.iotsmartaliv.modules.cardManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligoo.sdk.ConstantsUtils;
import com.intelligoo.sdk.LibDevModel;
import com.intelligoo.sdk.LibInterface;
import com.iotsmartaliv.R;
import com.iotsmartaliv.adapter.TabAdapter;
import com.iotsmartaliv.apiAndSocket.models.DeviceObject;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.ActivityCardManagerBinding;
import com.iotsmartaliv.utils.ErrorMsgDoorMasterSDK;
//


import static com.iotsmartaliv.adapter.DevicelistAdapter.selectDevice;

/**
 * This activity class is used to manage the card.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2019-01-16
 */
public class CardManagerActivity extends AppCompatActivity {
    TabAdapter adapter;
    ProgressDialog progress;
    String communityId, deviceId;
    LibInterface.ManagerCallback callBack = (result, bundle) -> {
        /*
ConstantsUtils.OPEN_DELAY （open time）
ConstantsUtils.CONTROL device control mode（(Electric control lock: 0x00, Electrical control: 0x01)）
ConstantsUtils.REG_CARDS_NUMS (card rosters)
ConstantsUtils.DEV_SYSTEM_VERSION (The firmware version number)
ConstantsUtils.MAX_CONTAINER (maximum user capacity)
*/
//Use the above Key to obtain corresponding data in the bundle
        if (result == 0x00) {
            int openDelay = bundle.getInt(ConstantsUtils.OPEN_DELAY);
            int controlWay = bundle.getInt(ConstantsUtils.CONTROL);
            int regCardNums = bundle.getInt(ConstantsUtils.REG_CARDS_NUMS);
            int systemVersion = bundle.getInt(ConstantsUtils.DEV_SYSTEM_VERSION);
            int maxContainer = bundle.getInt(ConstantsUtils.MAX_CONTAINER);

            Toast.makeText(CardManagerActivity.this, "openDelay:" + openDelay + "    " + "controlMode:" + controlWay + "    " + "regCardNums:" + regCardNums + "    " + "systemVersion:" + systemVersion + "    " + "maxContainer:" + maxContainer + "    ",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(CardManagerActivity.this, "Failure:" + result,
                    Toast.LENGTH_SHORT).show();
        }
    };
    ActivityCardManagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        ButterKnife.bind(this);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        communityId = getIntent().getStringExtra(Constant.COMMUNITY_ID);
        deviceId = getIntent().getStringExtra(Constant.DEVICE_ID);

        adapter = new TabAdapter(getSupportFragmentManager());

        Bundle bundleData = new Bundle();
        bundleData.putString(Constant.COMMUNITY_ID, communityId);
        bundleData.putString(Constant.DEVICE_ID, deviceId);
        CardListFragment cardListFragment = new CardListFragment();
        cardListFragment.setArguments(bundleData);
        UserListFragment userListFragment = new UserListFragment();
        userListFragment.setArguments(bundleData);
        /*adapter.addFragment(new CardListFragment(), "Card List");
        adapter.addFragment(new UserListFragment(), "User List");*/
        adapter.addFragment(cardListFragment, "Card List");
        adapter.addFragment(userListFragment, "User List");
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.viewPager.setOffscreenPageLimit(2);
        progress = new ProgressDialog(this);
        progress.setMessage("Processing.....");
        progress.setCancelable(false);

        binding.navigation.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.add_card:
                            progress.show();
                            LibInterface.ManagerCallback callback = (result, bundle) -> {
                                progress.dismiss();
                                if (result == 0) {
                                    Toast.makeText(this, "Swipe Add Mode Enable Successfully", Toast.LENGTH_SHORT).show();
                                    showDialog("Swipe Add", "Scan your card to add.");
                                } else {
                                    Toast.makeText(this, "Failed:ErrorCode:-" + result, Toast.LENGTH_SHORT).show();

                                }
                            };
                            int ret3 = LibDevModel.swipeAddCardModel(this, DeviceObject.getLibDev(selectDevice), callback);
                            if (ret3 != 0) {
                                Toast.makeText(CardManagerActivity.this, "Error on Add card " + ret3, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                            return true;
                        case R.id.delete_card:
                            progress.show();
                            int ret = LibDevModel.swipeCardDeleteModel(this, DeviceObject.getLibDev(selectDevice), new LibInterface.ManagerCallback() {
                                @Override
                                public void setResult(int result, Bundle bundle) {
                                    progress.dismiss();
                                    if (result == 0) {
                                        showDialog("Swipe Delete", "Scan your card to delete.");
                                        Toast.makeText(CardManagerActivity.this, "Swipe Delete Mode Enable Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CardManagerActivity.this, "Failed:ErrorCode:-" + result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            if (ret != 0) {
                                Toast.makeText(CardManagerActivity.this, "Error on Add card " + ret, Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }

                            return true;
                        case R.id.delete_all_card:
                            showClearCardDialog();
                            return true;
                    }
                    return false;
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void showDialog(String msg, String titleMsg) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_swipe_mode);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView title = dialog.findViewById(R.id.title);
        title.setText(msg);

        TextView tv_message = dialog.findViewById(R.id.tv_message);
        tv_message.setText(titleMsg);

        Button dialogButton = dialog.findViewById(R.id.exitToSwipeMode);
        dialogButton.setOnClickListener(v -> {
            if (msg.equalsIgnoreCase("Swipe Add")) {
                int ret = LibDevModel.existSwipeCardAddModel(CardManagerActivity.this, DeviceObject.getLibDev(selectDevice), (result, bundle) -> {
                    if (result == 0) {
                        Toast.makeText(CardManagerActivity.this, "Swipe Add Card Mode  Exit Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } else {
                        Toast.makeText(CardManagerActivity.this, "Failed:ErrorCode:-" + result, Toast.LENGTH_SHORT).show();
                    }
                });
                if (ret != 0) {
                    Toast.makeText(CardManagerActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                }
            } else if (msg.equalsIgnoreCase("Swipe Delete")) {
                int ret = LibDevModel.existSwipeCardDeleteModel(CardManagerActivity.this, DeviceObject.getLibDev(selectDevice), (result, bundle) -> {
                    if (result == 0) {
                        Toast.makeText(CardManagerActivity.this, "Swipe Delete Card Mode Exit Successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    } else {
                        Toast.makeText(CardManagerActivity.this, "Failed:ErrorCode:-" + result, Toast.LENGTH_SHORT).show();
                    }
                });
                if (ret != 0) {
                    Toast.makeText(CardManagerActivity.this, ErrorMsgDoorMasterSDK.getErrorMsg(ret), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

    }

    public void showClearCardDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_clear_all_card);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button buttonYes = dialog.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(v -> {
            int cleanCardRet = LibDevModel.cleanCard(CardManagerActivity.this, DeviceObject.getLibDev(selectDevice), new LibInterface.ManagerCallback() {
                @Override
                public void setResult(int result, Bundle bundle) {
                    if (result == 0x00) {
                        Toast.makeText(CardManagerActivity.this, "Card Clean Successfully.",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        if (result == 48) {
                            Toast.makeText(getApplicationContext(), "Result Error Timer Out", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CardManagerActivity.this, "Failure:" + result,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });       //Clean Card Test
            if (cleanCardRet != 0) {
                Toast.makeText(CardManagerActivity.this, "CleanCardFailure", Toast.LENGTH_SHORT).show();
            }

        });

        Button buttonNo = dialog.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();

    }

    public void getCardDetail(View view) {
        int ret = LibDevModel.getDeviceConfig(this, DeviceObject.getLibDev(selectDevice), callBack);
    }
}