package com.iotsmartaliv.dmvphonedemotest;
import static com.iotsmartaliv.constants.Constant.VO_IP;
import static com.iotsmartaliv.constants.Constant.VO_PORT;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.bugfender.sdk.Bugfender;
import com.doormaster.vphone.inter.DMVPhoneModel;
import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.activity.EnrollmentActivity;
import com.iotsmartaliv.apiCalling.retrofit.ApiServices;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.VoIpModel;
import com.iotsmartaliv.utils.ConnectivityHelper;
import com.iotsmartaliv.utils.SharePreference;
import com.thinmoo.utils.ChangeServerUtil;
import com.thinmoo.utils.ServerContainer;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * * For developer startup DMVPhone SDK
 * 一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 * Created by Liukebing on 2017/2/15.
 */



public class MVDPApplication extends Application {

    private static final String TAG = "MVDPApplication";
    String ip = "";
    String port = "";

    @Override
    public void onCreate() {
        Log.d(TAG, "[DMVPApplication] onCreate");
        super.onCreate();
        getVoip();
       

        // DMVPhoneModel.addPushIntentService(DemoIntentService.class);//添加自定义推送透传监听
        // ChangeServerUtil.getInstance().setAppServer(ServerContainer.THINMOO_HUAWEI_APP_SERVER);


    }
    @Override
    public void onTerminate() {
        super.onTerminate();

        // Release ConnectivityHelper when the application terminates
        ConnectivityHelper.release(this);
    }

    public void configure(String ip,String port){
        SharePreference.getInstance(this).putString(VO_IP, ip);
        SharePreference.getInstance(this).putString(VO_PORT, port);
        ConnectivityHelper.initialize(this);
        FirebaseApp.initializeApp(this);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        Bugfender.init(this, "BRxwnD0DhR8uUUhBlsfBtVPRpnMEZWsI", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableUIEventLogging(this);
        Bugfender.enableLogcatLogging();

        Stetho.initializeWithDefaults(this);
        //初始化sdk
        //  DMVPhoneModel.initDMVPhoneSDK(this);
//        DMVPhoneModel.initDMVPhoneSDK(this,"DmDemo");
        //context,appTitle,hasSur,autoAccept


//        ChangeServerUtil.getInstance().initConfig(this);
        ServerContainer serverContainer2 = new ServerContainer("43.229.85.122", "8099", "自定义应用服务器");
        ChangeServerUtil.getInstance().setAppServer(serverContainer2);
//        ServerContainer sipContainer = new ServerContainer("113.197.36.196", "5061", "CustomVideoServer");
        ServerContainer sipContainer = new ServerContainer(ip, port, "CustomVideoServer");
        ChangeServerUtil.getInstance().setVideoServer(sipContainer);
        DMVPhoneModel.initConfig(this);
        DMVPhoneModel.initDMVPhoneSDK(this, "DDemo", false, false);
        DMVPhoneModel.setCameraId(1, this);
        DMVPhoneModel.enableCallPreview(true, this);//打开预览消息界面显示
        DMVPhoneModel.setActivityToLaunchOnIncomingReceived(DmCallIncomingActivity.class);
        DMVPhoneModel.receivePushNotification("Incoming call from unidentified");

        DMVPhoneModel.setLogSwitch(true);
    }
    public void getVoip() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.UrlPath.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an ApiService instance from the Retrofit instance.
        ApiServices service = retrofit.create(ApiServices.class);

        // Call the getJokes() method of the ApiService
        // to make an API request.
        Call<VoIpModel> call = service.getVoip();

        // Use the enqueue() method of the Call object to
        // make an asynchronous API request.
        call.enqueue(new Callback<VoIpModel>() {
            // This is an anonymous inner class that implements the Callback interface.
            @Override
            public void onResponse(Call<VoIpModel> call, retrofit2.Response<VoIpModel> response) {

                VoIpModel voIpModel = response.body();
                 ip = voIpModel.getData().getIp();
                 port = voIpModel.getData().getPort();
                 configure(ip,port);


            }

            @Override
            public void onFailure(Call<VoIpModel> call, Throwable t) {
                // This method is called when the API request fails.
//                Toast.makeText(this, "Request Fail", Toast.LENGTH_SHORT).show();
                Log.e("","");
            }
        });
    }
}
