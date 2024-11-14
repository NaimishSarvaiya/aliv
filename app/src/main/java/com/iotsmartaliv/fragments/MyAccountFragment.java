package com.iotsmartaliv.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;

import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.activity.EnrollmentActivity;
import com.iotsmartaliv.activity.LoginActivity;
import com.iotsmartaliv.activity.MainActivity;
import com.iotsmartaliv.activity.NewPasswordActivity;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.databinding.MyAccountFragmentBinding;
import com.iotsmartaliv.model.DeleteUserRequest;
import com.iotsmartaliv.model.SuccessResponseModel;
import com.iotsmartaliv.model.booking.PaymentMethodModel;
import com.iotsmartaliv.services.ShakeOpenService;
import com.iotsmartaliv.utils.CommanUtils;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


//import butterknife.OnClick;
//import butterknife.Unbinder;

import static com.iotsmartaliv.activity.MainActivity.drawerLayout;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;
import static com.iotsmartaliv.constants.Constant.hideLoader;

/**
 * This fragment class is used for my account fragment.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class MyAccountFragment extends AppCompatActivity implements RetrofitListener<SuccessResponse> {
    ApiServiceProvider apiServiceProvider;
    MyAccountFragmentBinding binding;
    ArrayList<String> appFeture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyAccountFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        view = inflater.inflate(R.layout.my_account_fragment, container, false);
        appFeture = (ArrayList<String>) SharePreference.getInstance(this).getFeatureForApp();
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        if (appFeture.contains(Constant.USER_APP_NAME)){
            binding.imgChangeName.setVisibility(View.VISIBLE);
        }else {
            binding.imgChangeName.setVisibility(View.GONE);
        }
        setDataUI();
        binding.imgChangeName.setOnClickListener(this::onViewClicked);
        binding.imgChangeUname.setOnClickListener(this::onViewClicked);
        binding.edtFaceRegistration.setOnClickListener(this::onViewClicked);
        binding.tvPaymentHelp.setOnClickListener(this::onViewClicked);
        binding.relPassword.setOnClickListener(this::onViewClicked);
        binding.relRemoveAccount.setOnClickListener(this::onViewClicked);
        binding.llToolbar.imgBack.setOnClickListener(v -> onBackPressed());
        binding.llToolbar.tvHeader.setText("My Account");
//        return binding.getRoot();

    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view;
//        binding = MyAccountFragmentBinding.inflate(inflater,container,false);
////        view = inflater.inflate(R.layout.my_account_fragment, container, false);
//        appFeture = (ArrayList<String>) SharePreference.getInstance(getActivity()).getFeatureForApp();
//        apiServiceProvider = ApiServiceProvider.getInstance(getContext(),false);
//        if (appFeture.contains(Constant.USER_APP_NAME)){
//            binding.imgChangeName.setVisibility(View.VISIBLE);
//        }else {
//            binding.imgChangeName.setVisibility(View.GONE);
//        }
//        setDataUI();
//        binding.imgChangeName.setOnClickListener(this::onViewClicked);
//        binding.imgChangeUname.setOnClickListener(this::onViewClicked);
//        binding.edtFaceRegistration.setOnClickListener(this::onViewClicked);
//        binding.tvPaymentHelp.setOnClickListener(this::onViewClicked);
//        binding.relPassword.setOnClickListener(this::onViewClicked);
//        binding.relRemoveAccount.setOnClickListener(this::onViewClicked);
//        return binding.getRoot();
//    }

    public void setDataUI() {

        binding.tvName.setText(LOGIN_DETAIL.getUserFullName());
        binding.edtUserName.setText(LOGIN_DETAIL.getUsername());
        binding.edtEmailLogIn.setText(LOGIN_DETAIL.getUserEmail());
    }

    /**
     * This method is used for load the fragments.
     *
//     * @param fragment
     */
//    private void loadFragments(Fragment fragment) {
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, fragment);
//        fragmentTransaction.commit();
//    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_change_name:
                showPopForTakeInput("Enter your name.", binding.tvName.getText().toString(), 1);
                break;

            case R.id.img_change_uname:
                showPopForTakeInput("Enter your username.", binding.edtUserName.getText().toString(), 2);
                break;
            case R.id.edt_face_registration:
                Intent intentEnrollment = new Intent(MyAccountFragment.this, EnrollmentActivity.class);
                startActivity(intentEnrollment);
                break;
            case R.id.rel_password:
                showPopForTakeInput("Enter your current password.", "Current password.", 3);
                break;
            case R.id.tv_payment_help:
                /*    Intent intentPayment = new Intent(getActivity(), PaymentSetupActivity.class);
//                startActivity(intentPayment);*/
//                drawerLayout.closeDrawer(GravityCompat.START);
//                Fragment fragmentPayment = new PaymentFragment();
//                ((MainActivity) getActivity()).tvHeader.setText(getResources().getString(R.string.payment));
//                ((MainActivity) getActivity()).imgDraweHeader.setVisibility(View.GONE);
//                loadFragments(fragmentPayment);
                break;
            case R.id.rel_removeAccount:
                showDeleteDialog();
                break;
        }
    }

    /**
     * @param title
     * @param hint
     * @param isDialogType this param is use for which type of dialog you want to show
     *                     1= change name dialog.
     *                     2= change username dialog.
     *                     3= change password dialog for confirm old password.
     */
    public void showPopForTakeInput(String title, String hint, int isDialogType) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(MyAccountFragment.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_community, null);
        final EditText editText = dialogView.findViewById(R.id.edt_comment);
        final TextView tvTitle = dialogView.findViewById(R.id.textView);
        Button buttonSubmit = dialogView.findViewById(R.id.buttonSubmit);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        buttonSubmit.setText("Update");
        buttonCancel.setText("Cancel");
        tvTitle.setText(title);
        editText.setText(hint);
        if (isDialogType == 3) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            buttonSubmit.setText("Verify");

        }

        buttonCancel.setOnClickListener(view -> {
            dialogBuilder.dismiss();
        });
        buttonSubmit.setOnClickListener(view -> {
            if (editText.getText().toString().trim().length() > 0) {
                String value = editText.getText().toString();
                if (isDialogType != 3) {
                    dialogBuilder.dismiss();
                    apiServiceProvider.updateUserProile(LOGIN_DETAIL.getAppuserID(), (isDialogType == 2 ? value : ""), (isDialogType == 1 ? value : ""), "", this);
                } else {

                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("SHA-256");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    byte[] hashInBytes = new byte[0];
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        hashInBytes = md.digest(value.getBytes(StandardCharsets.UTF_8));
                    }
                    // bytes to hex
                    StringBuilder sb = new StringBuilder();
                    for (byte b : hashInBytes) {
                        sb.append(String.format("%02x", b));
                    }

                    if (LOGIN_DETAIL.getPassword().equalsIgnoreCase(sb.toString())) {
                        dialogBuilder.dismiss();
                        Intent intentEnrollment = new Intent(MyAccountFragment.this, NewPasswordActivity.class);
                        intentEnrollment.putExtra("isFromAccount", true);
                        startActivity(intentEnrollment);
                    } else {
                        editText.setError("Password Not Match.");
                    }
                }
            } else {
                if (isDialogType != 3) {
                    editText.setError(isDialogType == 1 ? "Enter Name" : "Enter Username");
                } else {
                    editText.setError("Enter Your Current Password");
                }
                editText.requestFocus();
            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }


    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.UPDATE_PROFILE_API:
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {
                    SharePreference.getInstance(MyAccountFragment.this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                    Constant.LOGIN_DETAIL = sucessRespnse.getData();
                    CommanUtils.showSucessDialog(MyAccountFragment.this, "Profile Updated Successfully", "");
                    setDataUI();
                } else {
                    Toast.makeText(MyAccountFragment.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.UPDATE_PROFILE_API:
                try {
                    Toast.makeText(MyAccountFragment.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(MyAccountFragment.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    public void deleteAccount(){
        Util.checkInternet(MyAccountFragment.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {

                if (isAvailable) {
                    apiServiceProvider = ApiServiceProvider.getInstance(MyAccountFragment.this,false);
                    DeleteUserRequest deleteUserRequest = new DeleteUserRequest(LOGIN_DETAIL.getAppuserID());
                    apiServiceProvider.DeleteUser(deleteUserRequest, new RetrofitListener<SuccessResponseModel>() {
                        @Override
                        public void onResponseSuccess(SuccessResponseModel sucessRespnse, String apiFlag) {
//                            if (dialog!=null) {
//                                dialog.dismiss();
//                            }

                            LOGIN_DETAIL = null;
                            DMVPhoneModel.exit();
                            new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... params) {
                                    try {
                                        FirebaseMessaging.getInstance().deleteToken();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Log.d("FCMTOKEN", "doInBackground: " + e.getLocalizedMessage());
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void result) {
                                    Log.d("FCMTOKEN", "doInBackground: Done");
                                }
                            }.execute();
                            MyAccountFragment.this.stopService(new Intent(MyAccountFragment.this, ShakeOpenService.class));
                            SharePreference.getInstance(MyAccountFragment.this).clearPref();
                            SharePreference.getInstance(MyAccountFragment.this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);
                            // startActivity(new Intent(getContext(), SplashActivity.class));
                            hideLoader();
                            // login in issue-----------
                            Intent intent = new Intent(MyAccountFragment.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                            Toast.makeText(MyAccountFragment.this, sucessRespnse.getMsg(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
//                            if (dialog!=null) {
//                                dialog.dismiss();
//                            }
                            hideLoader();
                            Toast.makeText(MyAccountFragment.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                chagefailSatus(isOnline);
                        }
                    });
                } else {
                    hideLoader();
                }
            }
        });
    }
    private void showDeleteDialog() {
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
        TextView message = dialog.findViewById(R.id.message);
        ImageView imageIcon = dialog.findViewById(R.id.img_icon);

        title.setText("Delete!");
        message.setText("Are you sure? \n Do you want to delete account? This action is irreversible and will permanently remove all your data, from our system.");
       imageIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_delete));
       imageIcon.setVisibility(View.VISIBLE);
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
                dialog.dismiss();
                Constant.showLoader(MyAccountFragment.this);
                deleteAccount();
            }
        });

        // Show the dialog
        dialog.show();
    }

}