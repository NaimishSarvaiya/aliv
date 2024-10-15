package com.iotsmartaliv.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.R;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.ErrorObject;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.retrofit.ApiServiceProvider;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.DeviceBean;
import com.iotsmartaliv.utils.CheckPermissionUtils;
import com.iotsmartaliv.utils.SharePreference;
import com.iotsmartaliv.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.iotsmartaliv.constants.Constant.BACKGROUND_SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.LOGIN_DETAIL;
import static com.iotsmartaliv.constants.Constant.LOGIN_PREFRENCE;
import static com.iotsmartaliv.constants.Constant.SHAKE_ENABLE;
import static com.iotsmartaliv.constants.Constant.hideLoader;
import static com.iotsmartaliv.constants.Constant.showLoader;

/**
 * This activity class is used for login.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RetrofitListener<SuccessResponse> {
    private static final int REQUEST_CODE_TEST = 999;
    private static final int LOGIN_SUCCESS = 0x00;
    private static final int LOGIN_FAILED = 0x01;
    private static final int CLIENT_ID_NULL = 0x02;
    private static final int DATA_NULL = 0x03;
    private static final int LIST_REFRESH = 0x04;
    private static final int OPEN_AGAIN = 0x05;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    public static ArrayList<DeviceBean> devList = new ArrayList<DeviceBean>();
    private static String client_id = null;
    LoginButton btnFacebook;
    ApiServiceProvider apiServiceProvider;
    ImageView view_img;
    private ImageView myFaceBookButton, myGooglePlusButton;
    private Map<String, DeviceBean> tempDevDict = new HashMap<String, DeviceBean>();
    private TextView tvSignUp, tvLogin;
    private EditText username, password;
    private CallbackManager callbackManager;
    private String fbToken = "";
    private boolean isInFacebookMode = false;
    private GoogleSignInClient mGoogleSignInClient;
    private RelativeLayout rlFacebookSignUp, rlGoogleSignUp;
    String token = "";
    //    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            hideLoader();
//            switch (msg.what) {
//                case LOGIN_SUCCESS:
//                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                case CLIENT_ID_NULL:
//                    Toast.makeText(LoginActivity.this, "client_id is null",
//                            Toast.LENGTH_SHORT).show();
//                    break;
//                case LOGIN_FAILED:
//                    int ret = (Integer) msg.obj;
//                    Toast.makeText(LoginActivity.this, "Login Failure" + ret,
//                            Toast.LENGTH_SHORT).show();
//                    break;
//
//                   /* case OPEN_AGAIN:
//                    int opRet = LibDevModel.controlDevice(getApplicationContext(), 0x00, curClickDevice, null, callback);
//                    if (opRet == 0) {
//                        return;
//                    } else {
//                        Toast.makeText(getApplicationContext(), "RET：" + opRet, Toast.LENGTH_SHORT).show();
//                    }*/
//
//                case LIST_REFRESH:
//                    break;
//            }
//        }
//    };
    private boolean checkForMsg;
    private boolean showAlertForServices;
    private LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
        apiServiceProvider = ApiServiceProvider.getInstance(this,false);
        if (getIntent().getExtras() != null) {
            showAlertForServices = getIntent().getBooleanExtra("showAlert", false);
        }
        /*if (false) {
            Dialog dialog = new Dialog(this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.service_alert_dialog);
            TextView tvMsg = dialog.findViewById(R.id.tv_msg);
            TextView tvYes = dialog.findViewById(R.id.tv_yes);
            TextView tvNo = dialog.findViewById(R.id.tv_no);
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serviceIntent = new Intent(LoginActivity.this, ShakeOpenService.class);
                    startService(serviceIntent);
                    SharePreference.getInstance(LoginActivity.this).putBoolean(SHAKE_ENABLE, true);
                    dialog.dismiss();
                    initNextDialog();
                }
            });
            dialog.show();
        }*/

        Boolean hasOnBoardingShown = SharePreference.getInstance(LoginActivity.this).getBoolean(Constant.HAS_ON_BOARDING_SHOWN);

        Boolean isBackgroundShakeOn = SharePreference.getInstance(LoginActivity.this).getBoolean(BACKGROUND_SHAKE_ENABLE);

        SharePreference.getInstance(LoginActivity.this).putBoolean(Constant.HAS_ON_BOARDING_SHOWN, true);

        if (!hasOnBoardingShown && !isBackgroundShakeOn) {
            onBackgroundShakeDialog1();
        }

        firebaseToken();
        initViews();
        initListeners();
        requestPermissiontest();
        loginManager
                = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        facebookLoginSetup();
        googleSignInSetup();
    }

    private void onBackgroundShakeDialog1() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder.setMessage("Enjoy contactless and hassle free Access by shaking your phone! Do you want to “Shake” to open the door?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    SharePreference.getInstance(LoginActivity.this).putBoolean(SHAKE_ENABLE, true);

                    onBackgroundShakeDialog2();

                })
                .setNegativeButton("No", (dialog, id) -> {

                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void onBackgroundShakeDialog2() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setTitle("Alert!");
        alertDialogBuilder.setMessage("We recommend enabling “Shake” to Open in the background only if you frequently use it. Enabling “Shake” to Open in the background requires ALiv app to run in the background while you are not using it. Do you want to enable “Shake” to Open in the background?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {

                    SharePreference.getInstance(LoginActivity.this).putBoolean(BACKGROUND_SHAKE_ENABLE, true);

                })
                .setNegativeButton("No", (dialog, id) -> {
                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void initNextDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.next_shake_alert_dialog);
        TextView tvOk = dialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Initialize views.
     */
    private void initViews() {
        tvSignUp = findViewById(R.id.tv_sign_up);
        tvLogin = findViewById(R.id.tv_login);
        username = findViewById(R.id.edt_email_log_in);
        password = findViewById(R.id.edt_password);
        btnFacebook = findViewById(R.id.login_button);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        myFaceBookButton = findViewById(R.id.my_login_button);
        myGooglePlusButton = findViewById(R.id.my_sign_in_button);
        rlFacebookSignUp = findViewById(R.id.rel_sign_in_facebook);
        view_img = findViewById(R.id.view_img);
        rlGoogleSignUp = findViewById(R.id.rel_sign_in_google);

    }

    /**
     * Initialize listeners.
     */
    private void initListeners() {
        tvSignUp.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        myGooglePlusButton.setOnClickListener(this);
        myFaceBookButton.setOnClickListener(this);
        rlFacebookSignUp.setOnClickListener(this);
        rlGoogleSignUp.setOnClickListener(this);
        view_img.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {

                case MotionEvent.ACTION_UP:
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password.setSelection(password.getText().length());
                    break;

                case MotionEvent.ACTION_DOWN:
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    password.setSelection(password.getText().length());

                    break;
            }
            return true;
        });
    }

    /**
     * This method is used to setup and login with google plus
     */
    private void googleSignInSetup() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    }

    /**
     * This method is used for set up the facebook and login with facebook.
     */
    private void facebookLoginSetup() {


//        List<String> permissionNeeds = Arrays.asList("email",
//                "public_profile");
//        btnFacebook.setReadPermissions(permissionNeeds);
//        btnFacebook.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                isInFacebookMode = true;
                String token = loginResult.getAccessToken().getToken();
                String appId = loginResult.getAccessToken().getApplicationId();
                String id = loginResult.getAccessToken().getUserId();
                showLoader(LoginActivity.this);
                Set<String> deniedPermissions = loginResult.getRecentlyDeniedPermissions();
                if (deniedPermissions.size() > 0) {
                    Toast.makeText(LoginActivity.this, "Facebook email permission error", Toast.LENGTH_SHORT).show();
                    LoginManager.getInstance().logOut();
                    Constant.hideLoader();
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
                                        Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    String name = object.optString("name");
                                    Profile profile = Profile.getCurrentProfile();
                                    String id = object.optString("id");
                                    Log.e("profile id ", "" + id);

                                    if (email == null || name == null || email.isEmpty() || name.isEmpty()) {
                                        LoginManager.getInstance().logOut();
                                        Constant.hideLoader();
                                        Toast.makeText(LoginActivity.this, "Email not found", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    String[] names = name.split(" ", 2);

//                                    if (Util.checkInternet(LoginActivity.this)) {
//                                        LoginManager.getInstance().logOut();
                                        String finalEmail = email;
//                                        FirebaseMessaging.getInstance().getToken()
//                                                .addOnCompleteListener(task -> {
//                                                    if (!task.isSuccessful()) {
//                                                        Log.w(TAG, "getInstanceId failed", task.getException());
//                                                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//
////                                                        Toast.makeText(LoginActivity.this, "Connectivity issue. Please try again", Toast.LENGTH_SHORT).show();
//                                                        return;
//                                                    }
//                                                    Constant.hideLoader();
                                                    String packageName = BuildConfig.APPLICATION_ID;

                                                    Log.e("Package Name", packageName);
                                                    Util.checkInternet(LoginActivity.this, new Util.NetworkCheckCallback() {
                                                        @Override
                                                        public void onNetworkCheckComplete(boolean isAvailable) {
                                                           if (isAvailable){
                                                            apiServiceProvider.perfromLogin(finalEmail, "", packageName, "facebook", id,token, LoginActivity.this);
                                                        }else {
                                                            hideLoader();
                                                        }
                                                        }
                                                    }) ;

//                                                });
//                                    } else {
//                                        Constant.hideLoader();
//                                    }
                                } catch (Exception e) {
                                    Constant.hideLoader();
                                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

                Constant.hideLoader();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("error: ", error.toString());
                Toast.makeText(LoginActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                Constant.hideLoader();
            }
        });
    }

    void firebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
//                                    Toast.makeText(LoginActivity.this, "Connectivity Issue. Please try again", Toast.LENGTH_SHORT).show();
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        hideLoader();
                        return;
                    }
                    token = task.getResult();
                    Log.e(TAG, "FCMTOKEN: " + task.getResult());
                });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_up:
                Intent intentMain = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intentMain);
                username.setText("");
                password.setText("");
              /*  //vdk Fix for login issue
                finish();
                //vdk Fix for login issue*/
                break;
            case R.id.tv_login:
           /*     DeviceBean dev = new DeviceBean();
                dev.setDevSn("4112562222");
                dev.setDevMac("3f:49:f5:20:b8:2e");
                dev.setDevType(1);
                dev.seteKey("4d875a53c664633b5e71e3eddc0dec8d984480335780000000000031807402331000");
                LibDevModel libDev = DevicelistAdapter.getLibDev(dev);
                int ret = LibDevModel.openDoor(this, libDev, new LibInterface.ManagerCallback() {
                    @Override
                    public void setResult(int result, Bundle bundle) {
                        if (result == 0x00) {
                            Toast.makeText(LoginActivity.this, "Success",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (result == 48) {
                                Toast.makeText(LoginActivity.this, "Result Error Timer Out", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failure:" + result, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                if (ret == 0) {
                    return;
                } else {
                    Toast.makeText(this, "RET：" + ret, Toast.LENGTH_SHORT).show();
                }
*/
                final String username1 = username.getText().toString().trim();
                final String password1 = password.getText().toString();
                if (isValid(username1, password1)) {
//                    if (Util.checkInternet(this)) {
//                        showLoader(this);
//                        FirebaseMessaging.getInstance().getToken()
//                                .addOnCompleteListener(task -> {
////                                hideLoader();
//                                    if (!task.isSuccessful()) {
//                                        Log.w(TAG, "getInstanceId failed", task.getException());
////                                    Toast.makeText(LoginActivity.this, "Connectivity Issue. Please try again", Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                        hideLoader();
//                                        return;
//                                    }
//                                    Log.e(TAG, "FCMTOKEN: " + task.getResult());
//
                    String packageName = BuildConfig.APPLICATION_ID;
//
//                                    Log.e("Package Name", packageName);
//                    if (Util.checkInternet(LoginActivity.this)) {
//                        showLoader(this);
//                        apiServiceProvider.perfromLogin(username1, password1, packageName, "", "", token, this);
//                    } else {
//                        hideLoader();
//                    }

                    Util.checkInternet(LoginActivity.this, new Util.NetworkCheckCallback() {
                        @Override
                        public void onNetworkCheckComplete(boolean isAvailable) {
                            if (isAvailable) {
                                showLoader(LoginActivity.this);
                                // Proceed with API call
                                apiServiceProvider.perfromLogin(username1, password1, packageName, "", "", token, LoginActivity.this);
                            } else {
                                hideLoader();
                                // Handle the case when there is no internet connection available
                                // E.g., show a message to the user
                            }
                        }
                    });
//                    checkInternetExtra
//                                });

                  /*  //Todo Change Here For old Server Login.
                    showLoader(this);
                    Thread login_th = new Thread(new Runnable() {
                        public void run() {
                            login(username1, password1);
                        }
                    });
                    login_th.start();*/
//                    }else {
//                        Util.showToast(LoginActivity.this,"Please connect the device with proper Internet");
////                        Toast.makeText(LoginActivity.this,"Please connect the device with proper Internet", Toast.LENGTH_LONG).show();
//                    } else {
//                        hideLoader();
//                    }
                }
                break;
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.my_login_button:
            {
                loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));

            }
//                btnFacebook.performClick();
                break;

            case R.id.my_sign_in_button:
                signIn();
                break;
            case R.id.rel_sign_in_facebook:
            {
                loginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));

            }
//            btnFacebook.performClick();
                break;
            case R.id.rel_sign_in_google:
                signIn();
                break;
        }
    }

    /**
     * Validation for login.
     *
     * @param username1
     * @param password1
     * @return
     */
    private boolean isValid(String username1, String password1) {
        if (username1.equalsIgnoreCase("") && username1.isEmpty()) {
            // Toast.makeText(this, "Please enter the email address.", Toast.LENGTH_SHORT).show();
            username.setError("Please Enter Username.");
            username.requestFocus();
            return false;
        }/* else if (!isValidEmail(username1)) {
            Toast.makeText(this, "Please enter the valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }*/ else if (password1.equalsIgnoreCase("")) {
            //  Toast.makeText(this, "Please enter your password.", Toast.LENGTH_SHORT).show();
            password.setError("Please Enter Password.");
            password.requestFocus();

            return false;
        }
        return true;
    }

    /**
     * Web-api call for login.
     *
     * @param username1
     * @param password1
     */
/*    private void login(String username1, String password1) {
        try {
            JSONObject login_ret = Request.login(username1, password1);
            if (login_ret == null || login_ret.isNull("ret")) {
                Log.e("doormaster", "login_ret error");
                return;
            }
            Log.e("doomaster", login_ret.toString());
            int ret = login_ret.getInt("ret");
            Message msg = new Message();
            if (ret == 0) {
                if (login_ret.isNull("data")) {
                    mHandler.sendEmptyMessage(DATA_NULL);
                    return;
                }
                JSONObject data = login_ret.getJSONObject("data");
                if (!data.isNull("client_id")) {
                    client_id = data.getString("client_id");
                    SharePreference.getInstance(this).putString("CLIENTID", client_id);
                    devList = Request.reqDeviceList(client_id);
                    if (devList == null) {
                        devList = new ArrayList<DeviceBean>();
                        tempDevDict = new HashMap<String, DeviceBean>();
                    } else {
                        for (DeviceBean devBean : devList) {
                            tempDevDict.put(devBean.getDevSn(), devBean);
                        }
                    }
                    mHandler.sendEmptyMessage(LOGIN_SUCCESS);
                } else {
                    msg.what = CLIENT_ID_NULL;
                    mHandler.sendMessage(msg);
                }
            } else {
                msg.what = LOGIN_FAILED;
                msg.obj = ret;
                mHandler.sendMessage(msg);
            }
        } catch (JSONException e) {
            Log.e("doormaster ", e.getMessage());
        }
    }*/

    /**
     * Request for run-time permission for location.
     */
    public void requestPermissiontest() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // you needer permissions
            String[] permissions = {
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            };
            // check it is needed
            permissions = CheckPermissionUtils.getNeededPermission(LoginActivity.this, permissions);
            // requestPermissions
            if (permissions.length > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, REQUEST_CODE_TEST);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_TEST:
                if (grantResults.length > 0) {
                    return;
                }
                if (!CheckPermissionUtils.isNeedAddPermission(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

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
    private void  signIn() {
        Util.checkInternet(LoginActivity.this, new Util.NetworkCheckCallback() {
            @Override
            public void onNetworkCheckComplete(boolean isAvailable) {
                if (isAvailable){
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }else {

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
//
//            if (Util.checkInternet(LoginActivity.this)) {
//                showLoader(this);
//                FirebaseMessaging.getInstance().getToken()
//                        .addOnCompleteListener(task -> {
//                            if (!task.isSuccessful()) {
//                                Log.w(TAG, "getInstanceId failed", task.getException());
////                            Toast.makeText(LoginActivity.this, "Connectivity issue. Please try again", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//
//                                return;
//                            }
//                            hideLoader();
            String packageName = BuildConfig.APPLICATION_ID;
//
//                            dsfa
//                            Log.e("Package Name", packageName);

            Util.checkInternet(LoginActivity.this, new Util.NetworkCheckCallback() {
                @Override
                public void onNetworkCheckComplete(boolean isAvailable) {
                    if (isAvailable) {
                        showLoader(LoginActivity.this);
                        apiServiceProvider.perfromLogin(email, "", packageName, "google", id, token, LoginActivity.this);
                    } else {
                        hideLoader();
                    }
                }
            });

//                        });
//            } else {
//                hideLoader();
//            }
        }
    }

    public void forgotPswd(View view) {
        startActivity(new Intent(this, ForgotActivity.class));
    }


    @Override
    public void onResponseSuccess(SuccessResponse sucessRespnse, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.LOGIN_API:
                hideLoader();
                if (sucessRespnse.getStatus().equalsIgnoreCase("OK")) {

                    Log.d(TAG, "LoginRes: \n" + sucessRespnse.getData().getUserEmail() + " psw \n" + sucessRespnse.getData().getAccountTokenPwd());

                    Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                    SharePreference.getInstance(LoginActivity.this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                    Constant.LOGIN_DETAIL = sucessRespnse.getData();
                    SharePreference.getInstance(LoginActivity.this).putBoolean(Constant.IS_LOGIN, true);
//                    Intent service = new Intent(LoginActivity.this, DeviceLogSyncService.class);
//                    startService(service);
                    SharedPreferences sharePreferenceNew = getSharedPreferences("ALIV_NEW", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editShared = sharePreferenceNew.edit();
                    editShared.putString("APP_USER_ID", LOGIN_DETAIL.getAppuserID());
                    editShared.apply();
                    Intent intent1;
                    if (LOGIN_DETAIL.getLoginStatus().equalsIgnoreCase("1")) {
                        intent1 = new Intent(LoginActivity.this, MainActivity.class);
                    } else {
                        intent1 = new Intent(LoginActivity.this, NewPasswordActivity.class);
                    }
                    startActivity(intent1);
                    finishAffinity();

                    /*//  todo uncomment the following code when you want to login in video Intercom server
                    Constant.showLoader(this);
                    DMVPhoneModel.loginVPhoneServer(sucessRespnse.getData().getUserEmail(), sucessRespnse.getData().getAccountTokenPwd(), 1, this, new DMCallback() {
                        //DMVPhoneModel.loginVPhoneServer("ashishagrawal0108@gmail.com", "c5be8bcL88496f2bd778bfebeabc78208801efe3", 1, this, new DMModelCallBack.DMCallback() {
                        @Override
                        public void setResult(int errorCode, DMException e) {
                            Log.d(TAG, "DMCallback: " + "errorCode=" + errorCode);

                            Constant.hideLoader();
                            if (e == null) {
                                *//*Toast.makeText(LoginActivity.this, "login successful", Toast.LENGTH_SHORT).show();
                                SharePreference.getInstance(LoginActivity.this).putString(LOGIN_PREFRENCE, new Gson().toJson(sucessRespnse.getData()));
                                Constant.LOGIN_DETAIL = sucessRespnse.getData();
                                SharePreference.getInstance(LoginActivity.this).putBoolean(Constant.IS_LOGIN, true);
                                Intent service = new Intent(LoginActivity.this, DeviceLogSyncService.class);
                                startService(service);
                                Intent intent1;
                                if (LOGIN_DETAIL.getLoginStatus().equalsIgnoreCase("1")) {
                                    intent1 = new Intent(LoginActivity.this, MainActivity.class);
                                } else {
                                    intent1 = new Intent(LoginActivity.this, NewPasswordActivity.class);
                                }
                                startActivity(intent1);
                                finishAffinity();*//*
                                Toast.makeText(LoginActivity.this, "Logged successfully Intercom SDK", Toast.LENGTH_SHORT).show();


                            } else {
                                new AsyncTask<Void, Void, Void>() {
                                    @Override
                                    protected Void doInBackground(Void... params) {
                                        try {
                                            FirebaseInstanceId.getInstance().deleteInstanceId();
                                        } catch (IOException e) {
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
                                Toast.makeText(LoginActivity.this, "Login failed，errorCode=" + errorCode + ",e=" + e.toString(), Toast.LENGTH_SHORT).show();
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
                    });*/
                } else {
                    hideLoader();
                    Toast.makeText(this, sucessRespnse.getMsg(), Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void onResponseError(ErrorObject errorObject, Throwable throwable, String apiFlag) {
        switch (apiFlag) {
            case Constant.UrlPath.LOGIN_API:
                hideLoader();
                final String username1 = username.getText().toString();
                Util.firebaseEvent(Constant.APIERROR, LoginActivity.this, Constant.UrlPath.SERVER_URL + apiFlag, username1, "", errorObject.getStatus());
                try {
                    Log.e("ERR LOGIN", errorObject.getDeveloperMessage());

                    Toast.makeText(this, errorObject.getDeveloperMessage(), Toast.LENGTH_LONG).show();
                    hideLoader();
                } catch (Exception e) {
                    Log.e("LOGIN EXC", e.getLocalizedMessage());
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                    hideLoader();
                }
                break;

        }
    }
}