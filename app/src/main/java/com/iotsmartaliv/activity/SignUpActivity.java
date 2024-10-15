package com.iotsmartaliv.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doormaster.vphone.config.DMErrorReturn;
import com.doormaster.vphone.exception.DMException;
import com.doormaster.vphone.inter.DMModelCallBack.DMCallback;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.constants.Request;
import com.iotsmartaliv.fragments.TermsOfFragment;
import com.iotsmartaliv.services.DeviceLogSyncService;
import com.iotsmartaliv.utils.NetworkAvailability;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

//import io.fabric.sdk.android.services.concurrency.AsyncTask;

import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.isValidEmail;
import static com.iotsmartaliv.constants.Constant.isValidPassword;
import static com.iotsmartaliv.constants.Constant.showLoader;

/**
 * This activity class is used for sign-up.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, RetrofitListener<SuccessResponse> {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public String communityCodeFromSocail = "";
    LoginButton btnFacebook;
    ApiServiceProvider apiServiceProvider;
    RelativeLayout rel_term_of_use;
    private TextView tvSignUp, tvLogin, have_code_tv;
    private RelativeLayout rlFacebookSignUp, rlGoogleSignUp;
    private ImageView myFaceBookButton, myGooglePlusButton, arrowImg, view_img, view_img_conf;
    private Spinner spinner;
    private CheckBox checkBox;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText etUserName, etFullName, etEmailId, etPassword, etConfirmPassword, intation_ed;
    private String strFName, strEmailId, strUserName, strPassword, strConfirmPassword, strSpinnerItem;
    private CallbackManager callbackManager;
    private String fbToken = "";
    private boolean isInFacebookMode = false;
    private List<String> categories = new ArrayList<String>();
    private ExpandableWeightLayout mExpandLayout;
    String token = "";
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        firebaseToken();
        initView();
        initListener();
        loginManager
                = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginSetup();
        googleSignInSetup();
    }

    /**
     * Initialize views.
     */
    private void initView() {
        rel_term_of_use = findViewById(R.id.rel_term_of_use);
        view_img = findViewById(R.id.view_img);
        view_img_conf = findViewById(R.id.view_img_conf);
        mExpandLayout = findViewById(R.id.expandableLayout);
        intation_ed = findViewById(R.id.intation_ed);
        have_code_tv = findViewById(R.id.have_code_tv);
        etUserName = findViewById(R.id.edt_user_name);
        etFullName = findViewById(R.id.edt_name);
        etEmailId = findViewById(R.id.edt_email_log_in);
        etPassword = findViewById(R.id.edt_password);
        etConfirmPassword = findViewById(R.id.edt_confirm_password);
        tvSignUp = findViewById(R.id.tv_sign_up);
        tvLogin = findViewById(R.id.tv_log_in);
        btnFacebook = findViewById(R.id.login_button);
        //tvGender = (TextView) findViewById(R.id.tv_gender);
        checkBox = findViewById(R.id.cb_terms_of_use);
        spinner = findViewById(R.id.spinner);
        myFaceBookButton = findViewById(R.id.my_login_button);
        rlFacebookSignUp = findViewById(R.id.rel_sign_up_facebook);
        rlGoogleSignUp = findViewById(R.id.rel_sign_up_google);
        myGooglePlusButton = findViewById(R.id.my_sign_in_button);
        arrowImg = findViewById(R.id.arrowImg);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        categories.add(getResources().getString(R.string.gender));
        categories.add(getResources().getString(R.string.male));
        categories.add(getResources().getString(R.string.female));
        categories.add(getResources().getString(R.string.other));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    /**
     * Initialize listener.  wFRVLiFPWLtQxDtfjh4UXNJYk6Y=
     **/
    private void initListener() {
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        //tvGender.setOnClickListener(this);
        myGooglePlusButton.setOnClickListener(this);
        myFaceBookButton.setOnClickListener(this);
        rlFacebookSignUp.setOnClickListener(this);
        rlGoogleSignUp.setOnClickListener(this);
        rel_term_of_use.setOnClickListener(this);
        mExpandLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onPreOpen() {
                arrowImg.setImageResource(R.drawable.ic_expand_close);
            }

            @Override
            public void onPreClose() {
                arrowImg.setImageResource(R.drawable.ic_expand_more);

            }

            @Override
            public void onOpened() {
                arrowImg.setImageResource(R.drawable.ic_expand_close);

            }

            @Override
            public void onClosed() {
                arrowImg.setImageResource(R.drawable.ic_expand_more);
                intation_ed.setText("");

            }
        });


        view_img.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_UP:
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setSelection(etPassword.getText().length());

                    break;

                case MotionEvent.ACTION_DOWN:
                    etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etPassword.setSelection(etPassword.getText().length());

                    break;

            }
            return true;
        });

        view_img_conf.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_UP:
                    etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etConfirmPassword.setSelection(etConfirmPassword.getText().length());

                    break;

                case MotionEvent.ACTION_DOWN:
                    etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    etConfirmPassword.setSelection(etConfirmPassword.getText().length());

                    break;

            }
            return true;
        });
    }

    /**
     * This method is used for set up the google plus sign in.
     */
    private void googleSignInSetup() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]
    }

    /**
     * This method is used for set up the facebook and sign in through the facebook.
     */
    private void facebookLoginSetup() {
//        callbackManager = CallbackManager.Factory.create();
//        List<String> permissionNeeds = Arrays.asList("email",
//                "public_profile");
//        btnFacebook.setReadPermissions(permissionNeeds);
//        btnFacebook.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                isInFacebookMode = true;
                // fbToken = loginResult.getAccessToken().getToken();
                // fbToken = loginResult.getAccessToken().getApplicationId();

                String token = loginResult.getAccessToken().getToken();
                String appId = loginResult.getAccessToken().getApplicationId();
                String id = loginResult.getAccessToken().getUserId();
                Log.e("IDS = ", token + " " + appId + " " + id);

                Set<String> deniedPermissions = loginResult.getRecentlyDeniedPermissions();

                if (deniedPermissions.size() > 0) {
                    Toast.makeText(SignUpActivity.this, "Facebook email permission error", Toast.LENGTH_SHORT).show();
                    logoutFromFacebook();
                    hideLoader();
                    return;
                }

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.e("Facebook", "onCompleted()");
                                try {
                                    String email = null;
                                    try {
                                        email = object.optString("email");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    String name = object.optString("name");
                                    Profile profile = Profile.getCurrentProfile();
                                    String id = object.optString("id");
                                    Log.e("profile id ", "" + id);

                                    if (email == null || name == null || email.isEmpty() || name.isEmpty()) {
                                        logoutFromFacebook();
                                        hideLoader();
                                        Toast.makeText(SignUpActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    String[] names = name.split(" ", 2);

                                    if (NetworkAvailability.getInstance(SignUpActivity.this).checkNetworkStatus()) {
                                        logoutFromFacebook();
                                        signUpApiMethod(name, name, email, "", "", "facebook", id, communityCodeFromSocail);
                                    } else {
                                        Toast.makeText(SignUpActivity.this, R.string.no_internet_available, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    hideLoader();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.e("onCancel()", "Cancel");
                hideLoader();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("error: ", error.toString());
                hideLoader();
            }
        });
    }

    public void logoutFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            // already logged out
            return;
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, graphResponse -> LoginManager.getInstance().logOut()).executeAsync();
    }

    public void showPopForCommunityCode(boolean isForFacebook) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_community, null);

        final EditText editText = dialogView.findViewById(R.id.edt_comment);
        Button button1 = dialogView.findViewById(R.id.buttonSubmit);
        Button button2 = dialogView.findViewById(R.id.buttonCancel);

        button2.setOnClickListener(view -> {
            communityCodeFromSocail = "";
            dialogBuilder.dismiss();
            if (isForFacebook) {
                loginManager.logInWithReadPermissions(SignUpActivity.this, Arrays.asList("public_profile", "email"));
            } else {
                signIn();
            }
        });
        button1.setOnClickListener(view -> {
            if (editText.getText().toString().trim().length() > 0) {
                communityCodeFromSocail = editText.getText().toString();
                if (isForFacebook) {
                    loginManager.logInWithReadPermissions(SignUpActivity.this, Arrays.asList("public_profile", "email"));

                } else {
                    signIn();
                }
                dialogBuilder.dismiss();
            } else {
                editText.setError("Enter invitation code Here");
            }
        });


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sign_up:
                strFName = etFullName.getText().toString().trim();
                strEmailId = etEmailId.getText().toString().trim();
                strUserName = etUserName.getText().toString().trim();
                strPassword = etPassword.getText().toString().trim();
                strConfirmPassword = etConfirmPassword.getText().toString().trim();
                strSpinnerItem = spinner.getSelectedItem().toString();
                if (isValid()) {
                    signUpApiMethod(strFName, strUserName, strEmailId, strPassword, strConfirmPassword, "", "", intation_ed.getText().toString());
                    /*Thread register_th = new Thread(new Runnable() {
                        public void run() {
                            emailRegister();
                        }
                    });
                    register_th.start();*/
                }
                break;
            case R.id.tv_log_in:
             /*   Intent intentLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intentLogin);
                finish();*/
                onBackPressed();
                break;

           /* case R.id.sign_in_button:
                if (!checkBox.isChecked()) {
                    Toast.makeText(this, "Please check terms and use", Toast.LENGTH_SHORT).show();
                } else {
                    signIn();
                }
                break;*/

          /*  case R.id.my_login_button:
                if (!checkBox.isChecked()) {
                    Toast.makeText(this, "Please check terms and use", Toast.LENGTH_SHORT).show();
                } else {
                    btnFacebook.performClick();
                }
                break;*/

           /* case R.id.my_sign_in_button:
                if (!checkBox.isChecked()) {
                    Toast.makeText(this, "Please check terms and use", Toast.LENGTH_SHORT).show();
                } else {
                    signIn();
                }
                break;*/

            case R.id.rel_term_of_use:
                Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rel_sign_up_facebook:
                if (!checkBox.isChecked()) {
                    Toast.makeText(this, "Please check terms of use", Toast.LENGTH_SHORT).show();
                } else {
                    showPopForCommunityCode(true);
                }
                break;
            case R.id.rel_sign_up_google:
                if (!checkBox.isChecked()) {
                    Toast.makeText(this, "Please check terms of use", Toast.LENGTH_SHORT).show();
                } else {
                    showPopForCommunityCode(false);
                }
                break;
        }
    }


    /**
     * Validation for signup.
     *
     * @return boolean value after validation.
     */
    private boolean isValid() {
        int size = strPassword.length();
        if (strFName.equalsIgnoreCase("")) {
            // Toast.makeText(this, R.string.empty_user_name, Toast.LENGTH_SHORT).show();
            etFullName.setError("Please Enter Name");
            etFullName.requestFocus();
            return false;
        } else if (strUserName.equalsIgnoreCase("")) {
            // Toast.makeText(this, R.string.empty_user_name, Toast.LENGTH_SHORT).show();
            etUserName.setError(getResources().getString(R.string.empty_user_name));
            etUserName.requestFocus();
            return false;
        } else if (strEmailId.equalsIgnoreCase("")) {
            //Toast.makeText(this, R.string.empty_email_id, Toast.LENGTH_SHORT).show();
            etEmailId.setError(getResources().getString(R.string.empty_email_id));
            etEmailId.requestFocus();

            return false;
        } else if (!isValidEmail(strEmailId)) {
            //Toast.makeText(this, R.string.valid_email_id, Toast.LENGTH_SHORT).show();
            etEmailId.setError(getResources().getString(R.string.valid_email_id));
            etEmailId.requestFocus();

            return false;
        } else if (strPassword.equalsIgnoreCase("")) {
            //  Toast.makeText(this, R.string.empty_password, Toast.LENGTH_SHORT).show();
            etPassword.setError(getResources().getString(R.string.empty_password));
            etPassword.requestFocus();

            return false;
        } else if (size < 8) {
            //  Toast.makeText(this, R.string.valid_password, Toast.LENGTH_SHORT).show();
            etPassword.setError(getResources().getString(R.string.valid_password));
            etPassword.requestFocus();
            return false;
        } else if (!isValidPassword(strPassword)) {
            //  Toast.makeText(this, R.string.valid_password, Toast.LENGTH_SHORT).show();
            etPassword.setError(getResources().getString(R.string.error_msg));
            etPassword.requestFocus();
            return false;
        } else if (strConfirmPassword.equalsIgnoreCase("")) {
            //    Toast.makeText(this, R.string.empty_confirm_password, Toast.LENGTH_SHORT).show();
            etConfirmPassword.setError(getResources().getString(R.string.empty_confirm_password));
            etConfirmPassword.requestFocus();
            return false;
        } else if (!strPassword.equalsIgnoreCase(strConfirmPassword)) {
            //  Toast.makeText(this, R.string.password_and_confirm_password_doesnt_match, Toast.LENGTH_SHORT).show();
            etConfirmPassword.setError(getResources().getString(R.string.password_and_confirm_password_doesnt_match));
            etConfirmPassword.requestFocus();

            return false;
        } /*else if (strSpinnerItem.equalsIgnoreCase(getResources().getString(R.string.gender))) {
            Toast.makeText(this, R.string.select_gender, Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (!checkBox.isChecked()) {
            Toast.makeText(this, "Please check terms of use", Toast.LENGTH_SHORT).show();
/*
            SpringAnimationClass.startSpringAnimation(checkBox);
*/
            return false;
        }
        return true;
    }

    /**
     * Web-api for signup (door master).
     */
    private void emailRegister() {
        try {
            JSONObject signup_ret = Request.signUp("6be6675031a8f141f82a3cfL6286b3a142ceccc1620a65a927b2c819", strUserName, strEmailId, strPassword, "");

            if (!signup_ret.isNull("ret")) {
                int ret = 0;
                try {
                    ret = signup_ret.getInt("ret");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ret == 0) {
                    showToast("Verification link has been sent to your account successfully.");
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
                    finish();
                } else if (ret == Constant.VERIFY_NUM_WRONG) {
                    showToast(getResources().getString(R.string.verify_num_wrong));
                } else if (ret == Constant.FAILED_CRERATE_COUNT) {
                    showToast(getResources().getString(R.string.failed_create_account));
                } else if (ret == Constant.ACCOUNT_REGISETERED) {
                    showToast(getResources().getString(R.string.account_had_registed));
                } else if (ret == Constant.NETWORD_SHUTDOWN) {
                    showToast(getResources().getString(R.string.check_network));
                } else {
                    showToast(getResources().getString(R.string.failed_register_unknow));
                }
            }
        } catch (Exception e) {
            Log.e("doormaster ", e.getMessage());
        }
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is used for sign up users.
     */
    public void signUpApiMethod(String fullName, String userName, String userEmailID, String password, String confirmPassword
            , String authProvider, String authUid, String intate_code) {
//        showLoader(this);
//        if (Util.checkInternet(this)) {
//            FirebaseMessaging.getInstance().getToken()
//                    .addOnCompleteListener(task -> {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "getInstanceId failed", task.getException());
//                            Toast.makeText(SignUpActivity.this, "Try Again" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            return;
//                        }

//                        Constant.showLoader(SignUpActivity.this);
        Util.checkInternet(this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    apiServiceProvider.performSignUp(fullName, userName, userEmailID, password, confirmPassword, authProvider, authUid, intate_code, token, SignUpActivity.this);

                }
            }
        });
//                        if (Util.checkInternet(this)) {
    }
//                    });

//        }else {
//           hideLoader();
//        }
//    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            String email = account.getEmail();
            String name = account.getGivenName();
            String id = account.getId();
            String displayName = account.getDisplayName();
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Util.checkInternet(SignUpActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                } else {

                }

            }
        });
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            String email = account.getEmail();
            String name = account.getGivenName();
            String id = account.getId();
            String displayName = account.getDisplayName();
            revokeAccess();
            signUpApiMethod(name, name, email, "", "", "google", id, communityCodeFromSocail);

        }
    }

    public void codeExpande(View view) {
        mExpandLayout.toggle();
    }


    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.SIGN_UP_API:
                hideLoader();
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

//                    Constant.showLoader(this);
//                    if (Util.checkInternet(this)) {

                    Util.checkInternet(this, new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                DMVPhoneModel.loginVPhoneServer(sucessRespnse.getData().getUserEmail(), sucessRespnse.getData().getAccountTokenPwd(), 1, SignUpActivity.this, new DMCallback() {
                                    //DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
                                    @Override
                                    public void setResult(int errorCode, DMException e) {
                                        Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
                                        if (e == null) {
                                            Toast.makeText(SignUpActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                            SharePreference.getInstance(SignUpActivity.this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                                            Constant.LOGIN_DETAIL = sucessRespnse.getData();
                                            SharePreference.getInstance(SignUpActivity.this).putBoolean(Constant.IS_LOGIN, true);
                                            Intent service = new Intent(SignUpActivity.this, DeviceLogSyncService.class);
//                                startService(service);
                                            Intent intent1 = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent1);
                                            finishAffinity();
                                        } else {
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
                                            //   Toast.makeText(SignUpActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
                                            Toast.makeText(SignUpActivity.this, "SignUp Successfully. But Something went Wrong on Login. Login Again.", Toast.LENGTH_SHORT).show();
                                            onBackPressed();

                                        }
                                        if (e == null) {
                                            Log.e(TAG, getResources().getString(R.string.status_connected));
                                        } else if (errorCode == DMErrorReturn.ERROR_RegistrationProgress) {
                                            Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_in_progress));
                                        } else if (errorCode == DMErrorReturn.ERROR_RegistrationFailed) {
                                            Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_error));
                                        } else {
                                            Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_not_connected));
                                        }
                                    }
                                });

                            } else {
                                hideLoader();
                                Toast.makeText(SignUpActivity.this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
//                        DMVPhoneModel.loginVPhoneServer(sucessRespnse.getData().getUserEmail(), sucessRespnse.getData().getAccountTokenPwd(), 1, this, new DMCallback() {
//                            //DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
//                            @Override
//                            public void setResult(int errorCode, DMException e) {
//                                Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);
//                                if (e == null) {
//                                    Toast.makeText(SignUpActivity.this, "login successful", Toast.LENGTH_SHORT).show();
//                                    SharePreference.getInstance(SignUpActivity.this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
//                                    Constant.LOGIN_DETAIL = sucessRespnse.getData();
//                                    SharePreference.getInstance(SignUpActivity.this).putBoolean(Constant.IS_LOGIN, true);
//                                    Intent service = new Intent(SignUpActivity.this, DeviceLogSyncService.class);
////                                startService(service);
//                                    Intent intent1 = new Intent(SignUpActivity.this, MainActivity.class);
//                                    startActivity(intent1);
//                                    finishAffinity();
//                                } else {
//                                    new AsyncTask<Void, Void, Void>() {
//                                        @Override
//                                        protected Void doInBackground(Void... params) {
//                                            try {
//                                                FirebaseMessaging.getInstance().deleteToken();
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                                Log.d("FCMTOKEN", "doInBackground: " + e.getLocalizedMessage());
//                                            }
//                                            return null;
//                                        }
//
//                                        @Override
//                                        protected void onPostExecute(Void result) {
//                                            Log.d("FCMTOKEN", "doInBackground: Done");
//                                        }
//                                    }.execute();
//                                    //   Toast.makeText(SignUpActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(SignUpActivity.this, "SignUp Successfully. But Something went Wrong on Login. Login Again.", Toast.LENGTH_SHORT).show();
//                                    onBackPressed();
//
//                                }
//                                if (e == null) {
//                                    Log.e(TAG, getResources().getString(R.string.status_connected));
//                                } else if (errorCode == DMErrorReturn.ERROR_RegistrationProgress) {
//                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_in_progress));
//                                } else if (errorCode == DMErrorReturn.ERROR_RegistrationFailed) {
//                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_error));
//                                } else {
//                                    Log.e(TAG, "statusCallback main" + getResources().getString(R.string.status_not_connected));
//                                }
//                            }
//                        });
                } else {
                    if (sucessRespnse.getMsg().equalsIgnoreCase("Email already Exist")){
                        hideLoader();
                        Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }else{
                        hideLoader();
                        Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        hideLoader();
        switch (apiFlag) {
            case Constant.UrlPath.SIGN_UP_API:
                Util.firebaseEvent(Constant.APIERROR, SignUpActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, LOGIN_DETAIL.getUsername(), LOGIN_DETAIL.getAppuserID(), errorObject.getStatus());

                try {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    public void termsCheck(View view) {
        if (!checkBox.isChecked()) {
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new TermsOfFragment()).addToBackStack("tag").commit();
        } else {
            checkBox.setChecked(false);
        }
    }

    public void userAceeptTerm() {
        checkBox.setChecked(true);
    }

    void firebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
//                                    Toast.makeText(LoginActivity.this, "Connectivity Issue. Please try again", Toast.LENGTH_SHORT).show();
                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        hideLoader();
                        return;
                    }
                    token = task.getResult();
                    Log.e(TAG, "FCMTOKEN: " + task.getResult());
                });
    }

}