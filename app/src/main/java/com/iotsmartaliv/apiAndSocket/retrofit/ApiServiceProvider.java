package com.iotsmartaliv.apiAndSocket.retrofit;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.iotsmartaliv.apiAndSocket.listeners.CallBackWithProgress;
import com.iotsmartaliv.apiAndSocket.listeners.RetrofitListener;
import com.iotsmartaliv.apiAndSocket.models.AutomationScheduleResponse;
import com.iotsmartaliv.apiAndSocket.models.BroadcastModel;
import com.iotsmartaliv.apiAndSocket.models.CountryDataResponse;
import com.iotsmartaliv.apiAndSocket.models.FcmUpdateModel;
import com.iotsmartaliv.apiAndSocket.models.GroupData;
import com.iotsmartaliv.apiAndSocket.models.GroupResponse;
import com.iotsmartaliv.apiAndSocket.models.InstructorInfoResponse;
import com.iotsmartaliv.apiAndSocket.models.SearchBookingData;
import com.iotsmartaliv.apiAndSocket.models.SearchBookingResponse;
import com.iotsmartaliv.apiAndSocket.models.SuccessArrayResponse;
import com.iotsmartaliv.apiAndSocket.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiAndSocket.models.SuccessResponse;
import com.iotsmartaliv.apiAndSocket.models.TimeSlot;
import com.iotsmartaliv.apiAndSocket.models.VideoDeviceListModel;
import com.iotsmartaliv.apiAndSocket.utils.HttpUtil;
import com.iotsmartaliv.constants.Constant;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.model.BookRoomsResponse;
import com.iotsmartaliv.model.BookingResponse;
import com.iotsmartaliv.model.CheckBookingRequest;
import com.iotsmartaliv.model.DeleteUserRequest;
import com.iotsmartaliv.model.InstructorInductionDataResponse;
import com.iotsmartaliv.model.InstructorListResponse;
import com.iotsmartaliv.model.OpenVideoDeviceRelayRequest;
import com.iotsmartaliv.model.SuccessResponseModel;
import com.iotsmartaliv.model.VisitorData;
import com.iotsmartaliv.model.VisitorsListDataResponse;
import com.iotsmartaliv.model.VoIpModel;
import com.iotsmartaliv.model.feedback.AddFeedbackRequest;
import com.iotsmartaliv.model.feedback.AddFeedbackResponse;
import com.iotsmartaliv.model.feedback.FeedBackCategoryModel;
import com.iotsmartaliv.model.feedback.FeedbackDetails;
import com.iotsmartaliv.model.feedback.FeedbackModel;
import com.iotsmartaliv.model.feedback.MessageHistory;
import com.iotsmartaliv.model.feedback.MessageStatusResponse;
import com.iotsmartaliv.model.feedback.MessageStatusUpdateRequest;
import com.iotsmartaliv.model.feedback.UploadFeedbackDocumentResponse;
import com.iotsmartaliv.modules.cardManager.CardUserListModel;
import com.iotsmartaliv.roomDB.AccessLogModel;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_EVENT;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_INSTRUCTOR;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_VISITOR_EVENT;
import static com.iotsmartaliv.constants.Constant.UrlPath.Broadcast_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.FCM_TOKEN_UPDATE_SERVICE_ENDPOINT;
import static com.iotsmartaliv.constants.Constant.UrlPath.FCM_TOKEN_UPDATE_SERVICE_URL;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_CARD_USERLIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_CURRENT_TIME;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_INSTRUCTOR_INFO;
import static com.iotsmartaliv.constants.Constant.UrlPath.INSTRUCTOR_INDUCTION_HR_RESPONSE;
import static com.iotsmartaliv.constants.Constant.UrlPath.INSTRUCTOR_LIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_BROADCAST_READ_STATUS_API;


/**
 * This class is used for the all Retrofit basic operation for api calling.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 27/3/19 :March : 2019 on 14 : 59.
 */
public class ApiServiceProvider<context> extends RetrofitBase {
    private ApiServices apiServices;

    private ApiServiceProvider(Context context) {
        super(context, true);
        apiServices = retrofit.create(ApiServices.class);
    }

    public static ApiServiceProvider getInstance(Context context) {
        ApiServiceProvider apiServiceProvider = new ApiServiceProvider(context);
        return apiServiceProvider;
    }

    public static ApiServiceProvider getInstance1(Activity activity) {
        ApiServiceProvider apiServiceProvider = new ApiServiceProvider(activity);
        return apiServiceProvider;
    }

    public ApiServices getApiService() {
        return apiServices;
    }

    /**
     * This method is use for login operation. we can perform login with ID and password, and we can use this method for social login also.
     * when we are calling api for normal login we have to pass only {@code username} and {@code password}.
     * when we are calling api for Social login we have to pass only {@code username} , {@code oauthProvider} and {@code oauthUid} we can skip password .
     *
     * @param username      user name is unique name for user.
     * @param password      password for authentication .
     * @param oauthProvider is's for social login provide (Like :Facebook ,Google).
     * @param oauthUid      it's for social login Unique ID (Facebook ,Google).
     */

    public void perfromLogin(String username, String password, String packageName, String oauthProvider, String oauthUid, String fcm_token, final RetrofitListener<SuccessResponse> retrofitListener) {

        HashMap<String, String> parameterHashMap = new HashMap<>();
        parameterHashMap.put("username", username);
        parameterHashMap.put("password", password);
        parameterHashMap.put("packageName", packageName);
        parameterHashMap.put("oauth_provider", oauthProvider);
        parameterHashMap.put("oauth_uid", oauthUid);
        parameterHashMap.put("fcm_token", fcm_token);
        parameterHashMap.put("platform", "0");
        Log.e("FCM TOKEN", fcm_token);
        Call<SuccessResponse> call = apiServices.performLogin(parameterHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {

                validateResponse(response, retrofitListener, Constant.UrlPath.LOGIN_API);
                super.onResponse(call, response);
                Log.e("RESPONSE LOGIN", response.raw().toString());
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);

//                Util.firebaseEvent(Constant.APIERROR, context,String.valueOf(call.request().url()),username);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.LOGIN_API);
            }
        });
    }


    /**
     * This method is use for signup operation. we can perform signup normal and social also .
     * when we are calling api for normal signup we have to skip {@code oauthProvider} and {@code oauthUid}.
     * when we are calling api for Social signup we must to be include {@code oauthProvider} and {@code oauthUid}. we can skip password .
     *
     * @param username      user name is unique name for user.
     * @param email_ID      user name is unique email of user.
     * @param password      password for authentication .
     * @param cpassword     confirm password for authentication.
     * @param oauthProvider is's for social signup provide (Like :Facebook ,Google).
     * @param oauthUid      it's for social signup Unique ID (Facebook ,Google).
     * @param invite_code   it's optional that provide by admin.
     *                      <p>
     *                      "termofuse"     it's mandatory. confirmation for term of is accepted "1" mean accepted "0" mean not accepted.
     *                      we validate on UI it should be "1".
     */

    public void performSignUp(String user_full_name, String username, String email_ID, String password, String cpassword,
                              String oauthProvider, String oauthUid, String invite_code, String fcm_token, final RetrofitListener<SuccessResponse> retrofitListener) {

        HashMap<String, String> parameterHashMap = new HashMap<>();
        parameterHashMap.put("user_full_name", user_full_name);
        parameterHashMap.put("username", username);
        parameterHashMap.put("email_ID", email_ID);
        parameterHashMap.put("password", password);
        parameterHashMap.put("cpassword", cpassword);
        parameterHashMap.put("oauth_provider", oauthProvider);
        parameterHashMap.put("oauth_uid", oauthUid);
        parameterHashMap.put("termofuse", "1");
        parameterHashMap.put("invite_code", invite_code);
        parameterHashMap.put("fcm_token", fcm_token);
        parameterHashMap.put("platform", "0");


        Call<SuccessResponse> call = apiServices.performSignUp(parameterHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.SIGN_UP_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SIGN_UP_API);
            }
        });
    }


    /**
     * This method is use for update user profile.
     * In this API we can skip param that we are not updating
     *
     * @param appuser_ID     this is user ID.
     * @param username       password for authentication .
     * @param user_full_name is full name of user
     * @param password       password for authentication .
     */

    public void updateUserProile(String appuser_ID, String username, String user_full_name, String password, final RetrofitListener<SuccessResponse> retrofitListener) {
        HashMap<String, String> parameterHashMap = new HashMap<>();
        parameterHashMap.put("appuser_ID", appuser_ID);
        parameterHashMap.put("username", username);
        parameterHashMap.put("user_full_name", user_full_name);
        parameterHashMap.put("password", password);

        Call<SuccessResponse> call = apiServices.performUpdateProfile(parameterHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                validateResponse(response, retrofitListener, Constant.UrlPath.UPDATE_PROFILE_API);
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.UPDATE_PROFILE_API);
            }
        });
    }


    /**
     * This method use to call api for forgot password. we send the mail to user with temporary password.
     *
     * @param username         this is user name.
     * @param retrofitListener this is call back listener for activity.
     */

    public void callForForgotPassword(String username, final RetrofitListener<SuccessResponse> retrofitListener) {

        Call<SuccessResponse> call = apiServices.callForgotPassword(username);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.FORGOT_PASSWORD_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.FORGOT_PASSWORD_API);
            }
        });
    }


    /**
     * This method use to call api for change password. it will call when user login with temporary password.
     *
     * @param appuser_ID       this is user ID.
     * @param password         this is password field
     * @param cpassword        his is confirm password field
     * @param retrofitListener this is call back listener for activity.
     */
    public void callForChangePassword(String appuser_ID, String password, String cpassword, final RetrofitListener<SuccessResponse> retrofitListener) {
        HashMap<String, String> paramHashMap = new HashMap<>();
        paramHashMap.put("appuser_ID", appuser_ID);
        paramHashMap.put("password", password);
        paramHashMap.put("cpassword", cpassword);

        Call<SuccessResponse> call = apiServices.callChangePassword(paramHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.CHANGE_PASSWORD_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.CHANGE_PASSWORD_API);
            }
        });
    }

    /**
     * This method use to call api for all Device List.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForDeviceList(String appuser_ID,String appVersion, final RetrofitListener<SuccessDeviceListResponse> retrofitArrayListener) {

        Call<SuccessDeviceListResponse> call = apiServices.getAllDeviceList(appuser_ID,appVersion);
        call.enqueue(new CallBackWithProgress<SuccessDeviceListResponse>(context) {
            @Override
            public void onResponse(Call<SuccessDeviceListResponse> call, Response<SuccessDeviceListResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_LIST_API);

            }

            @Override
            public void onFailure(Call<SuccessDeviceListResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_LIST_API);
            }
        });
    }

    /**
     * This method use to call api for all Device List.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForRefreshDeviceList(String appuser_ID,String appVersion, final RetrofitListener<SuccessDeviceListResponse> retrofitArrayListener) {
        Call<SuccessDeviceListResponse> call = apiServices.getAllDeviceList(appuser_ID,appVersion);
        call.enqueue(new Callback<SuccessDeviceListResponse>() {
            @Override
            public void onResponse(Call<SuccessDeviceListResponse> call, Response<SuccessDeviceListResponse> response) {
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_LIST_API);

            }

            @Override
            public void onFailure(Call<SuccessDeviceListResponse> call, Throwable t) {
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_LIST_API);

            }
        });
    }

    /**
     * This method use to call api for communityss Device List.
     *
     * @param appuser_ID   this is app user id field.
     * @param community_ID community id.
     */
    public void callForCommunityDeviceList(String appuser_ID, String community_ID, final RetrofitListener<SuccessResponse> retrofitArrayListener) {
        Call<SuccessResponse> call = apiServices.getCommunityDeviceList(appuser_ID, community_ID);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.COMMUNITY_DEVICE_LIST_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.COMMUNITY_DEVICE_LIST_API);
            }
        });
    }

    /**
     * This method use to call api for all video Device List.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForVideoDeviceList(String appuser_ID, final RetrofitListener<VideoDeviceListModel> retrofitArrayListener) {
        Call<VideoDeviceListModel> call = apiServices.getVideoDeviceList(appuser_ID);
        call.enqueue(new CallBackWithProgress<VideoDeviceListModel>(context) {
            @Override
            public void onResponse(Call<VideoDeviceListModel> call, Response<VideoDeviceListModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_VIDEO_LIST_API);
            }

            @Override
            public void onFailure(Call<VideoDeviceListModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_VIDEO_LIST_API);
            }
        });
    }

    public void callForVideoDeviceWithoutProgres(String appuser_ID, final RetrofitListener<VideoDeviceListModel> retrofitArrayListener){

        Call<VideoDeviceListModel> call = apiServices.getVideoDeviceList(appuser_ID);
        call.enqueue(new Callback<VideoDeviceListModel>() {
            @Override
            public void onResponse(Call<VideoDeviceListModel> call, Response<VideoDeviceListModel> response) {
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_VIDEO_LIST_API);
            }

            @Override
                public void onFailure(Call<VideoDeviceListModel> call, Throwable throwable) {
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), throwable, Constant.UrlPath.DEVICE_VIDEO_LIST_API);
            }
        });
    }


    /**
     * This method use to call api for join community.
     *
     * @param scan_code  this is scan_code it community code.
     * @param appuser_ID this is app user id field.
     */
    public void callForJoinCommunity(String appuser_ID, String scan_code, final RetrofitListener<SuccessResponse> retrofitListener) {
        HashMap<String, String> paramHashMap = new HashMap<>();
        paramHashMap.put("appuser_ID", appuser_ID);
        paramHashMap.put("scan_code", scan_code);

        Call<SuccessResponse> call = apiServices.callForJoinCommunity(paramHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.JOIN_COMMUNITY_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.JOIN_COMMUNITY_API);
            }
        });
    }

    /**
     * This method use to call api for all community.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForListOfCommunity(String appuser_ID, final RetrofitListener<SuccessArrayResponse> retrofitArrayListener) {
        Call<SuccessArrayResponse> call = apiServices.getAllCommunityList(appuser_ID);
        call.enqueue(new CallBackWithProgress<SuccessArrayResponse>(context) {
            @Override
            public void onResponse(Call<SuccessArrayResponse> call, Response<SuccessArrayResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.COMMUNITY_LIST_API);
            }

            @Override
            public void onFailure(Call<SuccessArrayResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.COMMUNITY_LIST_API);
            }
        });
    }

    /**
     * This method use to call api for all sub community.
     *
     * @param community_ID this is community_ID field.
     */
    public void callForListOfSubCommunity(String community_ID, final RetrofitListener<SuccessArrayResponse> retrofitArrayListener) {
        Call<SuccessArrayResponse> call = apiServices.getAllSubCommunityList(community_ID);
        call.enqueue(new CallBackWithProgress<SuccessArrayResponse>(context) {
            @Override
            public void onResponse(Call<SuccessArrayResponse> call, Response<SuccessArrayResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.SUB_COMMUNITY_LIST_API);
            }

            @Override
            public void onFailure(Call<SuccessArrayResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SUB_COMMUNITY_LIST_API);
            }
        });
    }


    /**
     * This method use to call api for server time for non network device time synchronization
     */
    public void callForGetCurrentServerTime(final RetrofitListener<String> retrofitArrayListener) {
        Call<String> call = apiServices.getCurrentTimeForSync();
        call.enqueue(new CallBackWithProgress<String>(context) {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.SERVERTIMESYNC);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SERVERTIMESYNC);
            }
        });
    }

    public void callVoIpCall(final RetrofitListener<VoIpModel> retrofitArrayListener) {
        Call<VoIpModel> call = apiServices.getVoip();
        call.enqueue(new CallBackWithProgress<VoIpModel>(context) {
            @Override
            public void onResponse(Call<VoIpModel> call, Response<VoIpModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.SERVERTIMESYNC);
            }

            @Override
            public void onFailure(Call<VoIpModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SERVERTIMESYNC);
            }
        });
    }

    /**
     * This method use for end call on twilio API
     */
    public void callApiForEndCall(String user_id, final RetrofitListener<SuccessResponse> retrofitArrayListener) {
        Call<SuccessResponse> call = apiServices.callAPIForENDCall(user_id);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.END_CALL_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.END_CALL_API);
            }
        });
    }


    /**
     * This method use to call api for post access log on server.
     *
     * @param appuser_ID       This is user ID.
     * @param accessLogModel   This is AccessLogModel
     * @param retrofitListener this is call back listener for activity.
     */
    public void postAccessLog(String appuser_ID, AccessLogModel accessLogModel, final RetrofitListener<AccessLogModel> retrofitListener) {
        HashMap<String, String> paramHashMap = new HashMap<>();
        paramHashMap.put("appuser_ID", appuser_ID);
        paramHashMap.put("device_SN", accessLogModel.getDevice_SN());
        paramHashMap.put("number", accessLogModel.getNumber());
        paramHashMap.put("description", accessLogModel.getDescription());
        paramHashMap.put("event_time", accessLogModel.getEvent_time());

        Call<SuccessResponse> call = apiServices.callForPostAccessLog(paramHashMap);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                //     validateResponse(response, retrofitListener, Constant.UrlPath.POST_ACCESS_LOG);
                retrofitListener.onResponseSuccess(accessLogModel, Constant.UrlPath.POST_ACCESS_LOG);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.POST_ACCESS_LOG);
            }
        });
    }


    public void taskOpenDoorRemotely(HashMap<String, String> hashMap,  final RetrofitListener<SuccessResponse> retrofitListener) {
        Call<SuccessResponse> call = apiServices.callForOpenDoor(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.POST_ACCESS_LOG);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.POST_ACCESS_LOG);
            }
        });
    }

    /**
     * This method use to call api for submit the basic visitor event.
     *
     * @param hashMap This is map of collection param.
     *                appuser_ID:31
     *                visitors[0][name]:test
     *                visitors[0][country_code]:91
     *                visitors[0][contact]:9907936962
     *                purpose:testing
     *                event_start_date:20/11/2019 01:43:10
     *                event_end_date:20/09/2019 01:43:10
     *                device_SNs[0]:132
     *                pincode_usage_limit:1
     *                community_ID:dsdsd
     */
    public void submitVisitorEvent(HashMap<String, String> hashMap, final RetrofitListener<SuccessResponse> retrofitListener) {
        Call<SuccessResponse> call = apiServices.callForAddVisitorEvent(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, ADD_VISITOR_EVENT);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, ADD_VISITOR_EVENT);
            }
        });
    }

    /**
     * This method use to call api for submit the enhance visitor event.
     *
     * @param hashMap This is map of collection param.
     *                appuser_ID:31
     *                visitors[0][name]:test
     *                visitors[0][country_code]:91
     *                visitors[0][contact]:9907936962
     *                purpose:testing
     *                event_start_date:20/11/2019 01:43:10
     *                event_end_date:20/09/2019 01:43:10
     *                device_SNs[0]:132
     *                pincode_usage_limit:1
     *                community_ID:dsdsd
     */
    public void submitVisitorEnhanceEvent(HashMap<String, String> hashMap, final RetrofitListener<SuccessResponse> retrofitListener) {
        Call<SuccessResponse> call = apiServices.callForAddEnchanceVisitorEvent(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, ADD_EVENT);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, ADD_EVENT);
            }
        });
    }


    /**
     * This method use to call api for Add Group.
     *
     * @param appuser_ID            this is app user id field.
     * @param groupName             this is the group name.
     * @param retrofitArrayListener this is response call back listener.
     */
    public void callForAddGroup(String appuser_ID, String groupName, final RetrofitListener<SuccessResponse> retrofitArrayListener) {
        HashMap<String, String> paramHashMap = new HashMap<>();
        paramHashMap.put("appuser_ID", appuser_ID);
        paramHashMap.put("group_name", groupName);
        Call<SuccessResponse> call = apiServices.callForAddGroup(paramHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.ADD_GROUP);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADD_GROUP);
            }
        });
    }

    /**
     * This method use to call api for all group list.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForListOfGroup(String appuser_ID, final RetrofitListener<GroupResponse> retrofitArrayListener) {
        Call<GroupResponse> call = apiServices.getAllGroupList(appuser_ID);
        call.enqueue(new CallBackWithProgress<GroupResponse>(context) {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.GET_GROUP_LIST);
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_GROUP_LIST);
            }
        });
    }

    /**
     * This method use to call api for update group .
     *
     * @param operation  this is operation type put(For Update) or delete(For delete).
     * @param vgroup_ID  this is group ID.
     * @param group_name this is updated group name.
     */
    public void callForGroupUpdateAndDelete(String operation, String vgroup_ID, String group_name, final RetrofitListener<GroupResponse> retrofitArrayListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("operation", operation);
        stringStringHashMap.put("vgroup_ID", vgroup_ID);
        stringStringHashMap.put("group_name", group_name);

        Call<GroupResponse> call = apiServices.callForGroupUpdateOrDelete(stringStringHashMap);
        call.enqueue(new CallBackWithProgress<GroupResponse>(context) {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.UPDATE_GROUP);
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.UPDATE_GROUP);
            }
        });
    }

    public void submitAddInstructor(HashMap<String, String> hashMap, final RetrofitListener<SuccessResponse> retrofitListener) {
        Call<SuccessResponse> call = apiServices.callForAddInstructor(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, ADD_INSTRUCTOR);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, ADD_INSTRUCTOR);
            }
        });
    }

    /**
     * This method use to call api for all Instructor Induction List.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForInstructorInductionList(String appuser_ID, final RetrofitListener<InstructorInductionDataResponse> retrofitArrayListener) {
        Call<InstructorInductionDataResponse> call = apiServices.getInstructorInductionList(appuser_ID);
        call.enqueue(new CallBackWithProgress<InstructorInductionDataResponse>(context) {
            @Override
            public void onResponse(Call<InstructorInductionDataResponse> call, Response<InstructorInductionDataResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.INSTRUCTOR_INDUCTION);
            }

            @Override
            public void onFailure(Call<InstructorInductionDataResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.INSTRUCTOR_INDUCTION);
            }
        });
    }

    /**
     * @param hashMap
     * @param retrofitListener
     */
    public void inductionHrResponse(HashMap<String, String> hashMap, final RetrofitListener<SuccessResponse> retrofitListener) {
        Call<SuccessResponse> call = apiServices.callForInductionHrResponse(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, INSTRUCTOR_INDUCTION_HR_RESPONSE);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, INSTRUCTOR_INDUCTION_HR_RESPONSE);
            }
        });
    }

    /***
     * api cal for get the instructor list
     * @param appuser_ID
     * @param retrofitArrayListener
     */
    public void callForInstructorList(String appuser_ID, final RetrofitListener<InstructorListResponse> retrofitArrayListener) {
        Call<InstructorListResponse> call = apiServices.getInstructorList(appuser_ID);
        call.enqueue(new CallBackWithProgress<InstructorListResponse>(context) {
            @Override
            public void onResponse(Call<InstructorListResponse> listResponseCall, Response<InstructorListResponse> response) {
                super.onResponse(listResponseCall, response);
                validateResponse(response, retrofitArrayListener, INSTRUCTOR_LIST);
            }

            @Override
            public void onFailure(Call<InstructorListResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, INSTRUCTOR_LIST);
            }
        });
    }

    public void callAPIForInstructorInfo(String contact_number, String community_ID, final RetrofitListener<InstructorInfoResponse> retrofitArrayListener) {
        Call<InstructorInfoResponse> call = apiServices.getInstructorInfo(contact_number, community_ID);
        call.enqueue(new CallBackWithProgress<InstructorInfoResponse>(context) {
            @Override
            public void onResponse(Call<InstructorInfoResponse> listResponseCall, Response<InstructorInfoResponse> response) {
                super.onResponse(listResponseCall, response);
                validateResponse(response, retrofitArrayListener, GET_INSTRUCTOR_INFO);
            }

            @Override
            public void onFailure(Call<InstructorInfoResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, GET_INSTRUCTOR_INFO);
            }
        });
    }

    public void callForAddVisitor(HashMap<String, String> hashMap, final RetrofitListener<SuccessResponse> retrofitArrayListener) {
        Call<SuccessResponse> call = apiServices.callForAddVisitor(hashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.ADD_VISITOR);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADD_VISITOR);
            }
        });
    }

    /**
     * This method use to call api for all visitor list.
     *
     * @param appuser_ID this is app user id field.
     */
    public void callForListOfVisitor(String appuser_ID, final RetrofitListener<VisitorsListDataResponse> retrofitArrayListener) {
        Call<VisitorsListDataResponse> call = apiServices.getAllVisitorList(appuser_ID);
        call.enqueue(new CallBackWithProgress<VisitorsListDataResponse>(context) {
            @Override
            public void onResponse(Call<VisitorsListDataResponse> call, Response<VisitorsListDataResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.GET_USER_VISITORS);
            }

            @Override
            public void onFailure(Call<VisitorsListDataResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_USER_VISITORS);
            }
        });
    }


    public void visitorUpdateAndDelete(String operation, String visit_ID, String appuser_ID, String uvisitor_name, final RetrofitListener<VisitorsListDataResponse> retrofitArrayListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("operation", operation);
        stringStringHashMap.put("visit_ID", visit_ID);
        stringStringHashMap.put("appuser_ID", appuser_ID);
        //stringStringHashMap.put("group_ID",group_ID);
        stringStringHashMap.put("uvisitor_name", uvisitor_name);
        Call<VisitorsListDataResponse> call = apiServices.callForVisitorUpdateOrDelete(stringStringHashMap);
        call.enqueue(new CallBackWithProgress<VisitorsListDataResponse>(context) {
            @Override
            public void onResponse(Call<VisitorsListDataResponse> call, Response<VisitorsListDataResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.UPDATE_VISITOR);
            }

            @Override
            public void onFailure(Call<VisitorsListDataResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.UPDATE_VISITOR);
            }
        });
    }


    public void callForCountryCodes(String timeZone, RetrofitListener<CountryDataResponse> retrofitListener) {
        Call<CountryDataResponse> dataResponseCall = apiServices.getListOfCountryCodes(timeZone);
        dataResponseCall.enqueue(new CallBackWithProgress<CountryDataResponse>(context) {
            @Override
            public void onResponse(Call<CountryDataResponse> call, Response<CountryDataResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GET_COUNTRY_CODES);
            }

            @Override
            public void onFailure(Call<CountryDataResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_COUNTRY_CODES);
            }
        });
    }

    public void getAssignVisitorsInGroup(String appuser_ID, String group_ID, RetrofitListener<VisitorsListDataResponse> retrofitListener) {
        Call<VisitorsListDataResponse> dataResponseCall = apiServices.getVisitorsInGroup(appuser_ID, group_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<VisitorsListDataResponse>(context) {
            @Override
            public void onResponse(Call<VisitorsListDataResponse> call, Response<VisitorsListDataResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GETVISITORSINGROUP);
            }

            @Override
            public void onFailure(Call<VisitorsListDataResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GETVISITORSINGROUP);
            }
        });
    }

    public void getAssignGroupsOfVisitor(String appuser_ID, String visit_ID, RetrofitListener<GroupResponse> retrofitListener) {
        Call<GroupResponse> dataResponseCall = apiServices.getVisitorsGroup(appuser_ID, visit_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<GroupResponse>(context) {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GET_COUNTRY_CODES);
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_COUNTRY_CODES);
            }
        });
    }

    /**
     * this api use for the Assign Or Unassigned Visitors To Groups / Group to visitors.
     *
     * @param appuser_ID
     * @param oprationType
     * @param visitorList
     * @param groupsList
     * @param responseRetrofitListener
     */

    public void callForAssignOrUnssignVisitorsToGroups(String appuser_ID, String oprationType, ArrayList<VisitorData> visitorList, ArrayList<GroupData> groupsList, final RetrofitListener<SuccessResponse> responseRetrofitListener) {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("appuser_ID", appuser_ID);
        stringStringHashMap.put("operation", oprationType);
        for (int i = 0; i < visitorList.size(); i++) {
            stringStringHashMap.put("visitors[" + i + "]", visitorList.get(i).getVisitorID());
        }
        for (int i = 0; i < groupsList.size(); i++) {
            stringStringHashMap.put("groups[" + i + "]", groupsList.get(i).getVgroupID());
        }

        Call<SuccessResponse> call = apiServices.callForAssignVisitorAndGroup(stringStringHashMap);
        call.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, responseRetrofitListener, Constant.UrlPath.VISITORSTOGROUPS);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                responseRetrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.VISITORSTOGROUPS);
            }
        });
    }

    /**
     * this api using for the Search room
     *
     * @param community_ID
     * @param appuser_ID
     * @param start_date
     * @param end_date
     * @param start_time
     * @param end_time
     * @param retrofitListener
     */

    public void searchBookings(String community_ID, String appuser_ID, String start_date, String end_date, String start_time, String end_time, RetrofitListener<SearchBookingResponse> retrofitListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("community_ID", community_ID);
        stringStringHashMap.put("appuser_ID", appuser_ID);
        stringStringHashMap.put("start_date", start_date);
        stringStringHashMap.put("end_date", end_date);
        stringStringHashMap.put("start_time", start_time);
        stringStringHashMap.put("end_time", end_time);
        Call<SearchBookingResponse> dataResponseCall = apiServices.searchBooking(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<SearchBookingResponse>(context) {
            @Override
            public void onResponse(Call<SearchBookingResponse> call, Response<SearchBookingResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.SEARCH_BOOKING);
            }

            @Override
            public void onFailure(Call<SearchBookingResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SEARCH_BOOKING);
            }
        });
    }

    /***
     * this api is call book the room.
     * @param search_criteria
     * @param bookedRoom
     * @param retrofitListener
     */
    public void callAPIForRoomBooking(String search_criteria, ArrayList<SearchBookingData> bookedRoom, RetrofitListener<SearchBookingResponse> retrofitListener) {
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("search_criteria", search_criteria);

        for (int i = 0; i < bookedRoom.size(); i++) {
            SearchBookingData searchBookingData = bookedRoom.get(i);
            stringStringHashMap.put("rooms[:" + i + "][room_ID]", searchBookingData.getRoomDetails().getRoomID());
            stringStringHashMap.put("rooms[:" + i + "][purpose]", searchBookingData.getPurpuse());
            int j = 0;
            for (TimeSlot timeSlot : searchBookingData.getTimeSlots()) {
                if (timeSlot.isSelected()) {
                    stringStringHashMap.put("rooms[:" + i + "][slots_ID][" + j + "]", timeSlot.getSlotsID());
                    j++;
                }
            }
        }
        Call<SearchBookingResponse> dataResponseCall = apiServices.callForRoomBooking(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<SearchBookingResponse>(context) {
            @Override
            public void onResponse(Call<SearchBookingResponse> call, Response<SearchBookingResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.BOOK_ROOM);
            }

            @Override
            public void onFailure(Call<SearchBookingResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.BOOK_ROOM);
            }
        });
    }

    /**
     * this api use for the get all booking.
     *
     * @param appuser_ID
     * @param retrofitListener
     */
    public void getAllBookings(String appuser_ID, RetrofitListener<BookingResponse> retrofitListener) {
        Call<BookingResponse> dataResponseCall = apiServices.getBookedRoom(appuser_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<BookingResponse>(context) {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GET_BOOKINGS);
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_BOOKINGS);
            }
        });
    }

    /**
     * this api use for the get all booking rooms.
     *
     * @param retrofitListener
     */
    public void getAllBookingRooms(String booking_ID, RetrofitListener<BookRoomsResponse> retrofitListener) {
        Call<BookRoomsResponse> dataResponseCall = apiServices.getBookedRooms(booking_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<BookRoomsResponse>(context) {
            @Override
            public void onResponse(Call<BookRoomsResponse> call, Response<BookRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GET_BOOKED_ROOMS);
            }

            @Override
            public void onFailure(Call<BookRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_BOOKED_ROOMS);
            }
        });
    }

    /**
     * this is the api call the room and booking.
     *
     * @param appuser_ID
     * @param booking_ID
     * @param room_IDs
     * @param retrofitListener
     */
    public void cancelRoomOrBooking(String appuser_ID, String booking_ID, String room_IDs, RetrofitListener<BookingResponse> retrofitListener) {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("appuser_ID", appuser_ID);
        stringStringHashMap.put("booking_ID", booking_ID);
        if (room_IDs != null) {
            stringStringHashMap.put("room_IDs[0]", room_IDs);
        }

        Call<BookingResponse> dataResponseCall = apiServices.cancelBookingOrRoom(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<BookingResponse>(context) {
            @Override
            public void onResponse(Call<BookingResponse> call, Response<BookingResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ROOM_CANCELLATION);
            }

            @Override
            public void onFailure(Call<BookingResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ROOM_CANCELLATION);
            }
        });
    }

    /**
     * this api use for the get all automation rooms.
     *
     * @param retrofitListener
     */
    public void getAutomationRoomList(String appuser_ID, RetrofitListener<AutomationRoomsResponse> retrofitListener) {
        Call<AutomationRoomsResponse> dataResponseCall = apiServices.getAutomationRoomList(appuser_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationRoomsResponse>(context) {
            @Override
            public void onResponse(Call<AutomationRoomsResponse> call, Response<AutomationRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.AUTOMATION_ROOM_LIST);
            }

            @Override
            public void onFailure(Call<AutomationRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.AUTOMATION_ROOM_LIST);
            }
        });
    }

    /**
     * this api use for the get all automation rooms.
     *
     * @param retrofitListener
     */
    public void getAutomationSchedule(String schedule_ID, RetrofitListener<AutomationScheduleResponse> retrofitListener) {
        Call<AutomationScheduleResponse> dataResponseCall = apiServices.getAutomationSchedule(schedule_ID);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationScheduleResponse>(context) {
            @Override
            public void onResponse(Call<AutomationScheduleResponse> call, Response<AutomationScheduleResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.GET_AUTOMATION_SCHEDULE);
            }

            @Override
            public void onFailure(Call<AutomationScheduleResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.GET_AUTOMATION_SCHEDULE);
            }
        });
    }

    /**
     * This API is use for the Add Automation Schedule.
     *
     * @param stringStringHashMap
     * @param retrofitListener
     */
    public void addAutomationTimeSchedule(HashMap<String, String> stringStringHashMap, RetrofitListener<AutomationRoomsResponse> retrofitListener) {
        Call<AutomationRoomsResponse> dataResponseCall = apiServices.callForAddAutomationSchedule(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationRoomsResponse>(context) {
            @Override
            public void onResponse(Call<AutomationRoomsResponse> call, Response<AutomationRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADD_AUTOMATION_SCHEDULE);
            }

            @Override
            public void onFailure(Call<AutomationRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADD_AUTOMATION_SCHEDULE);
            }
        });
    }

    /**
     * This API is use for the Add Automation Schedule.
     *
     * @param stringStringHashMap
     * @param retrofitListener
     */
    public void updateAutomationTimeSchedule(HashMap<String, String> stringStringHashMap, RetrofitListener<AutomationRoomsResponse> retrofitListener) {
        Call<AutomationRoomsResponse> dataResponseCall = apiServices.callForUpdateAutomationSchedule(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationRoomsResponse>(context) {
            @Override
            public void onResponse(Call<AutomationRoomsResponse> call, Response<AutomationRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.UPDATE_AUTOMATION_SCHEDULE);
            }

            @Override
            public void onFailure(Call<AutomationRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.UPDATE_AUTOMATION_SCHEDULE);
            }
        });
    }

    /**
     * This API is use for the Add Automation Schedule.
     *
     * @param stringStringHashMap
     * @param retrofitListener
     */
    public void deleteAutomationTimeSchedule(HashMap<String, String> stringStringHashMap, RetrofitListener<AutomationRoomsResponse> retrofitListener) {
        Call<AutomationRoomsResponse> dataResponseCall = apiServices.callForDeleteSchedule(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationRoomsResponse>(context) {
            @Override
            public void onResponse(Call<AutomationRoomsResponse> call, Response<AutomationRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.DELETE_SCHEDULE);
            }

            @Override
            public void onFailure(Call<AutomationRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DELETE_SCHEDULE);
            }
        });
    }

    /**
     * This API is use for the Add Automation Schedule.
     *
     * @param stringStringHashMap
     * @param retrofitListener
     */

    public void chageStatusOfAutomationDevice(HashMap<String, String> stringStringHashMap, RetrofitListener<AutomationRoomsResponse> retrofitListener) {
        Call<AutomationRoomsResponse> dataResponseCall = apiServices.callForUpdateDeviceStatus(stringStringHashMap);
        dataResponseCall.enqueue(new CallBackWithProgress<AutomationRoomsResponse>(context) {
            @Override
            public void onResponse(Call<AutomationRoomsResponse> call, Response<AutomationRoomsResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADHOC);
            }

            @Override
            public void onFailure(Call<AutomationRoomsResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADHOC);
            }
        });
    }

    /**
     * This API is use for the upload image for face enroll.
     *
     * @param appuser_ID
     * @param base64String
     * @param retrofitListener
     */
    public void callAPIForUploadFaceEnrollImg(String appuser_ID, String base64String, RetrofitListener<SuccessResponse> retrofitListener) {


        Call<SuccessResponse> dataResponseCall = apiServices.callForUploadFaceEnrollImg(appuser_ID, base64String);
        dataResponseCall.enqueue(new CallBackWithProgress<SuccessResponse>(context) {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADDFACEENROLLIMAGE);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADDFACEENROLLIMAGE);
            }
        });
    }

    /**
     * This API is use for the upload fcm token video intercom server.
     * * url: http://43.229.85.122:8099/doormaster/vphone/login/info
     * * method: POST
     * * json data:
     * * {
     * * "account":"app account",
     * * "token":"fcm token",
     * * "brands":"google",
     * * "phone":"android",
     * * "app_type":"1"
     * * }
     * *
     */
    public void callAPIForUpdateFcmTokenONIntercomServer(FcmUpdateModel FcmUpdateModel, RetrofitListener<FcmUpdateModel> retrofitListener) {
        Call<FcmUpdateModel> fcmUpdateModelCall = apiServices.updateFCMTokenForIntercom(FCM_TOKEN_UPDATE_SERVICE_URL + FCM_TOKEN_UPDATE_SERVICE_ENDPOINT, FcmUpdateModel);
        fcmUpdateModelCall.enqueue(new CallBackWithProgress<FcmUpdateModel>(context) {
            @Override
            public void onResponse(Call<FcmUpdateModel> call, Response<FcmUpdateModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.FCM_TOKEN_UPDATE_SERVICE_ENDPOINT);
            }

            @Override
            public void onFailure(Call<FcmUpdateModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.FCM_TOKEN_UPDATE_SERVICE_ENDPOINT);
            }
        });
    }


    /***
     * api cal for get the instructor list
     * @param appuser_ID
     * @param retrofitArrayListener
     */
    public void callForCardUserList(String community_ID, String device_ID, String appuser_ID, final RetrofitListener<CardUserListModel> retrofitArrayListener) {
        Call<CardUserListModel> call = apiServices.getCardUserList(community_ID, device_ID, appuser_ID);
        call.enqueue(new CallBackWithProgress<CardUserListModel>(context) {
            @Override
            public void onResponse(Call<CardUserListModel> listResponseCall, Response<CardUserListModel> response) {
                super.onResponse(listResponseCall, response);
                validateResponse(response, retrofitArrayListener, GET_CARD_USERLIST);
            }

            @Override
            public void onFailure(Call<CardUserListModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, GET_CARD_USERLIST);
            }
        });
    }


    /***
     * api cal for get the instructor list
     * @param appuser_ID
     * @param retrofitArrayListener
     */
    public void callForCardList(String community_ID, String device_ID, String appuser_ID, final RetrofitListener<CardUserListModel> retrofitArrayListener) {
        Call<CardUserListModel> call = apiServices.getCardList(community_ID, device_ID, appuser_ID);
        call.enqueue(new CallBackWithProgress<CardUserListModel>(context) {
            @Override
            public void onResponse(Call<CardUserListModel> listResponseCall, Response<CardUserListModel> response) {
                super.onResponse(listResponseCall, response);
                validateResponse(response, retrofitArrayListener, GET_CARD_USERLIST);
            }

            @Override
            public void onFailure(Call<CardUserListModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, GET_CARD_USERLIST);
            }
        });
    }


    /***
     * api cal for get the instructor list
     * @param stringStringHashMap all Data
     * @param retrofitArrayListener
     */
    public void callForUpdateSyncUser(HashMap<String, String> stringStringHashMap, final RetrofitListener<CardUserListModel> retrofitArrayListener) {
        Call<CardUserListModel> call = apiServices.updateCardListOnsync(stringStringHashMap);
        call.enqueue(new CallBackWithProgress<CardUserListModel>(context) {
            @Override
            public void onResponse(Call<CardUserListModel> listResponseCall, Response<CardUserListModel> response) {
                super.onResponse(listResponseCall, response);
                validateResponse(response, retrofitArrayListener, GET_CARD_USERLIST);
            }

            @Override
            public void onFailure(Call<CardUserListModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, GET_CARD_USERLIST);
            }
        });
    }

    /***
     * api cal for get the instructor list
     //     * @param stringStringHashMap all Data
     * @param retrofitArrayListener
     */
    public void callGetServerCurrentTime(final RetrofitListener<ResponseBody> retrofitArrayListener) {
        Call<ResponseBody> call = apiServices.getCurrentTime();
        call.enqueue(new CallBackWithProgress<ResponseBody>(context) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, GET_CURRENT_TIME);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, GET_CURRENT_TIME);
            }
        });
    }



    public void callGetUserBroadcast(final RetrofitListener<BroadcastModel> retrofitArrayListener, String appUserId) {

        Call<BroadcastModel> call = apiServices.getUserBroadcast(/*"49"*/appUserId);
        call.enqueue(new CallBackWithProgress<BroadcastModel>(context) {
            @Override
            public void onResponse(Call<BroadcastModel> call, Response<BroadcastModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Broadcast_API);
            }

            @Override
            public void onFailure(Call<BroadcastModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Broadcast_API);
            }
        });
    }

    public void callUpdateBroadcastReadStatus(String buID,final RetrofitListener<SuccessResponse> retrofitListener) {

        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("bu_ID", buID);

        Call<SuccessResponse> call = apiServices.updateBroadcastReadStatus(stringStringHashMap);
        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
//                super.onResponse(call, response);
                validateResponse(response, retrofitListener, UPDATE_BROADCAST_READ_STATUS_API);
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
//                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, UPDATE_BROADCAST_READ_STATUS_API);
            }
        });
    }

    public void checkDeviceBooking(CheckBookingRequest checkBookingRequest, RetrofitListener<SuccessDeviceListResponse> retrofitListener) {
        Call<SuccessDeviceListResponse> dataResponseCall = apiServices.checkDeviceBookings(checkBookingRequest);
        dataResponseCall.enqueue(new CallBackWithProgress<SuccessDeviceListResponse>(context) {
            @Override
            public void onResponse(Call<SuccessDeviceListResponse> call, Response<SuccessDeviceListResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADHOC);
            }

            @Override
            public void onFailure(Call<SuccessDeviceListResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADHOC);
            }
        });
    }

    public void openVideoDeviceRelay(OpenVideoDeviceRelayRequest relayRequest, RetrofitListener<SuccessResponseModel> retrofitListener) {
        Call<SuccessResponseModel> dataResponseCall = apiServices.openRelay(relayRequest);
        dataResponseCall.enqueue(new CallBackWithProgress<SuccessResponseModel>(context) {
            @Override
            public void onResponse(Call<SuccessResponseModel> call, Response<SuccessResponseModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADHOC);
            }

            @Override
            public void onFailure(Call<SuccessResponseModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADHOC);
            }
        });
    }

    public void DeleteUser(DeleteUserRequest deleteUserRequest, RetrofitListener<SuccessResponseModel> retrofitListener) {
        Call<SuccessResponseModel> dataResponseCall = apiServices.deleteUser(deleteUserRequest);
        dataResponseCall.enqueue(new CallBackWithProgress<SuccessResponseModel>(context) {
            @Override
            public void onResponse(Call<SuccessResponseModel> call, Response<SuccessResponseModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADHOC);
            }

            @Override
            public void onFailure(Call<SuccessResponseModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADHOC);
            }
        });
    }

    public void FeedbackList(String appuser_ID,String status,String pageLimit,String page, final RetrofitListener<FeedbackModel> retrofitArrayListener) {

        Call<FeedbackModel> call = apiServices.getFeedList(appuser_ID,status,pageLimit,page);
        call.enqueue(new CallBackWithProgress<FeedbackModel>(context) {
            @Override
            public void onResponse(Call<FeedbackModel> call, Response<FeedbackModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_LIST_API);
            }

            @Override
            public void onFailure(Call<FeedbackModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_LIST_API);
            }
        });
    }


    public void getFeedbackCategory(final RetrofitListener<FeedBackCategoryModel> retrofitArrayListener) {
        Call<FeedBackCategoryModel> call = apiServices.getFeedbackCatergory();
        call.enqueue(new CallBackWithProgress<FeedBackCategoryModel>(context) {
            @Override
            public void onResponse(Call<FeedBackCategoryModel> call, Response<FeedBackCategoryModel> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.SERVERTIMESYNC);
            }

            @Override
            public void onFailure(Call<FeedBackCategoryModel> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.SERVERTIMESYNC);
            }
        });
    }

    public void addFeedback(AddFeedbackRequest request, final RetrofitListener<AddFeedbackResponse> retrofitListener) {
        Call<AddFeedbackResponse> call = apiServices.addFeedback(request);
        call.enqueue(new CallBackWithProgress<AddFeedbackResponse>(context) {
            @Override
            public void onResponse(Call<AddFeedbackResponse> call, Response<AddFeedbackResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADD_FEEDBACK);
            }

            @Override
            public void onFailure(Call<AddFeedbackResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADD_FEEDBACK);
            }
        });
    }

    public void uploadFeedbackDocument(String fileName, File file, final RetrofitListener<UploadFeedbackDocumentResponse> retrofitListener) {
//        File file = new File(Util.getPathFromUri(fileUri,context)); // Get the file path from URI

        // Create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("feedback_document", file.getName(), requestFile);

        // Add feedback document name
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), fileName);

        // Execute the request
        Call<UploadFeedbackDocumentResponse> call = apiServices.uploadFeedbackDocument(name, body);
        call.enqueue(new CallBackWithProgress<UploadFeedbackDocumentResponse>(context) {
            @Override
            public void onResponse(Call<UploadFeedbackDocumentResponse> call, Response<UploadFeedbackDocumentResponse> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitListener, Constant.UrlPath.ADD_FEEDBACK_DOCUMENT);
            }

            @Override
            public void onFailure(Call<UploadFeedbackDocumentResponse> call, Throwable t) {
                super.onFailure(call, t);
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.ADD_FEEDBACK_DOCUMENT);
            }
        });
    }

    public void getFeedbackDetails(String feedback_ID, final RetrofitListener<FeedbackDetails> retrofitArrayListener) {

        Call<FeedbackDetails> call = apiServices.getFeedbackDetails(feedback_ID);
        call.enqueue(new CallBackWithProgress<FeedbackDetails>(context) {
            @Override
            public void onResponse(Call<FeedbackDetails> call, Response<FeedbackDetails> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_LIST_API);
            }

            @Override
            public void onFailure(Call<FeedbackDetails> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_LIST_API);
            }
        });
    }

    public void getFeedbackMessages(String feedback_ID,String appuser_ID,String page,String limit, final RetrofitListener<MessageHistory> retrofitArrayListener) {
        Call<MessageHistory> call = apiServices.getFeedBackMessages(feedback_ID,appuser_ID,page,limit);
        call.enqueue(new CallBackWithProgress<MessageHistory>(context) {
            @Override
            public void onResponse(Call<MessageHistory> call, Response<MessageHistory> response) {
                super.onResponse(call, response);
                validateResponse(response, retrofitArrayListener, Constant.UrlPath.DEVICE_LIST_API);
            }
            @Override
            public void onFailure(Call<MessageHistory> call, Throwable t) {
                super.onFailure(call, t);
                retrofitArrayListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, Constant.UrlPath.DEVICE_LIST_API);
            }
        });
    }

//    public void callFeedbackMessageStatus(MessageStatusUpdateRequest messageStatusUpdateRequest, final RetrofitListener<MessageStatusResponse> retrofitListener) {
//
//
//        Call<MessageStatusResponse> call = apiServices.updateFeedbackMessageStatus(messageStatusUpdateRequest);
//        call.enqueue(new Callback<MessageStatusResponse>() {
//            @Override
//            public void onResponse(Call<MessageStatusResponse> call, Response<MessageStatusResponse> response) {
////                super.onResponse(call, response);
//                validateResponse(response, retrofitListener, UPDATE_BROADCAST_READ_STATUS_API);
//            }
//
//            @Override
//            public void onFailure(Call<MessageStatusResponse> call, Throwable t) {
////                super.onFailure(call, t);
//                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, UPDATE_BROADCAST_READ_STATUS_API);
//            }
//        });
//    }

    public void callFeedbackMessageStatus(MessageStatusUpdateRequest messageStatusUpdateRequest, final RetrofitListener<MessageStatusResponse> retrofitListener) {

        Call<MessageStatusResponse> call = apiServices.updateFeedbackMessageStatus(messageStatusUpdateRequest);
        call.enqueue(new Callback<MessageStatusResponse>() {
            @Override
            public void onResponse(Call<MessageStatusResponse> call, Response<MessageStatusResponse> response) {
                // Handle success response
                validateResponse(response, retrofitListener, UPDATE_BROADCAST_READ_STATUS_API);
            }

            @Override
            public void onFailure(Call<MessageStatusResponse> call, Throwable t) {
                // Handle error
                retrofitListener.onResponseError(HttpUtil.getServerErrorPojo(context), t, UPDATE_BROADCAST_READ_STATUS_API);
            }
        });
    }
}