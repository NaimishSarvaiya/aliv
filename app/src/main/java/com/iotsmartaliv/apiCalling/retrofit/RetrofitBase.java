package com.iotsmartaliv.apiCalling.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iotsmartaliv.BuildConfig;
import com.iotsmartaliv.apiCalling.listeners.RetrofitListener;
import com.iotsmartaliv.apiCalling.models.ErrorObject;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.utils.AppUtil;
import com.iotsmartaliv.apiCalling.utils.HttpUtil;
import com.iotsmartaliv.apiCalling.utils.Logger;
import com.iotsmartaliv.apiCalling.utils.RequestInterceptor;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.utils.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is used for the all Retrofit common  operation like: Header, Timeout,Response Error Handle.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 27/3/19 :March : 2019 on 14 : 59.
 */

public class RetrofitBase {
    protected Retrofit retrofit;
    protected Context context;
    private Logger logger;

    public RetrofitBase(Context context, boolean addTimeout) {
        this.context = context;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder().addInterceptor(interceptor);
        httpClientBuilder.addNetworkInterceptor(new StethoInterceptor());
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        httpClientBuilder.addInterceptor(new RequestInterceptor(cm));

        if (addTimeout) {
            httpClientBuilder.readTimeout(Constant.TimeOut.SOCKET_TIME_OUT, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(Constant.TimeOut.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
        } else {
            httpClientBuilder.readTimeout(Constant.TimeOut.IMAGE_UPLOAD_SOCKET_TIMEOUT, TimeUnit.SECONDS);
            httpClientBuilder.connectTimeout(Constant.TimeOut.IMAGE_UPLOAD_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        }
        addVersioningHeaders(httpClientBuilder, context);
        OkHttpClient httpClient = httpClientBuilder.build();

        logger = new Logger(RetrofitBase.class.getSimpleName());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constant.UrlPath.SERVER_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }

    private void addVersioningHeaders(OkHttpClient.Builder builder, Context context) {
        final String appVersion = "v.1.0.1";
        final int version = AppUtil.getApplicationVersionCode(context);
        final String appName = "RetroKit";
        final String name = "RetroKit";
        builder.interceptors().add(chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader(appVersion, String.valueOf(version))
                    .addHeader(appName, name)
                    .build();
            return chain.proceed(request);
        });
    }

    void validateResponse(Response response, RetrofitListener retrofitListener, String apiFlag) {
        Util.logSentryEvent(apiFlag, response.raw().request(), response, null);
        if (response.code() == 200) {
            retrofitListener.onResponseSuccess(response.body(), apiFlag);
        } else {
            error(response, retrofitListener, apiFlag);
        }
    }

    private void error(Response<SuccessResponse> response, RetrofitListener retrofitListener, String apiFlag) {
        Gson gson = new Gson();
        ErrorObject errorPojo = null;
        try {
            if (response.errorBody() != null) {
                errorPojo = gson.fromJson((response.errorBody()).string(), ErrorObject.class);
            }
            if (errorPojo == null) {
                errorPojo = HttpUtil.getServerErrorPojo(context);
            }
            // Log error details in Sentry
            Util.logSentryEvent(apiFlag, response.raw().request(), response, null);
            retrofitListener.onResponseError(errorPojo, null, apiFlag);
        } catch (Exception e) {
            // Log exception in Sentry
            Util.logSentryEvent("API Error Handling Exception", response.raw().request(), null, e);
            retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), null, apiFlag);
        }
    }
}
