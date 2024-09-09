package com.iotsmartaliv.apiCalling.retrofit;

import com.iotsmartaliv.apiCalling.models.AutomationScheduleResponse;
import com.iotsmartaliv.apiCalling.models.BroadcastModel;
import com.iotsmartaliv.apiCalling.models.CountryDataResponse;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.FcmUpdateModel;
import com.iotsmartaliv.apiCalling.models.GroupResponse;
import com.iotsmartaliv.apiCalling.models.InstructorInfoResponse;
import com.iotsmartaliv.apiCalling.models.SearchBookingResponse;
import com.iotsmartaliv.apiCalling.models.SuccessArrayResponse;
import com.iotsmartaliv.apiCalling.models.SuccessDeviceListResponse;
import com.iotsmartaliv.apiCalling.models.SuccessResponse;
import com.iotsmartaliv.apiCalling.models.VideoDeviceData;
import com.iotsmartaliv.apiCalling.models.VideoDeviceListModel;
import com.iotsmartaliv.model.AutomationRoomsResponse;
import com.iotsmartaliv.model.BookRoomsResponse;
import com.iotsmartaliv.model.BookingResponse;
import com.iotsmartaliv.model.CheckBookingRequest;
import com.iotsmartaliv.model.DeleteUserRequest;
import com.iotsmartaliv.model.InstructorInductionDataResponse;
import com.iotsmartaliv.model.InstructorListResponse;
import com.iotsmartaliv.model.OpenVideoDeviceRelayRequest;
import com.iotsmartaliv.model.SuccessResponseModel;
import com.iotsmartaliv.model.VisitorsListDataResponse;
import com.iotsmartaliv.model.VoIpModel;
import com.iotsmartaliv.model.feedback.AddFeedbackRequest;
import com.iotsmartaliv.model.feedback.AddFeedbackResponse;
import com.iotsmartaliv.model.feedback.FeedBackCategoryModel;
import com.iotsmartaliv.model.feedback.FeedbackModel;
import com.iotsmartaliv.model.feedback.UploadFeedbackDocumentResponse;
import com.iotsmartaliv.modules.cardManager.CardUserListModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import static com.iotsmartaliv.constants.Constant.UrlPath.ADDFACEENROLLIMAGE;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_AUTOMATION_SCHEDULE;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_EVENT;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_FEEDBACK;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_FEEDBACK_DOCUMENT;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_GROUP;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_INSTRUCTOR;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_VISITOR;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADD_VISITOR_EVENT;
import static com.iotsmartaliv.constants.Constant.UrlPath.ADHOC;
import static com.iotsmartaliv.constants.Constant.UrlPath.AUTOMATION_ROOM_LIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.BOOK_ROOM;
import static com.iotsmartaliv.constants.Constant.UrlPath.Broadcast_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.CHANGE_PASSWORD_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.CHECK_DEVICE_BOOKINGS;
import static com.iotsmartaliv.constants.Constant.UrlPath.COMMUNITY_DEVICE_LIST_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.COMMUNITY_LIST_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.DELETE_APPUSER;
import static com.iotsmartaliv.constants.Constant.UrlPath.DELETE_SCHEDULE;
import static com.iotsmartaliv.constants.Constant.UrlPath.DEVICE_LIST_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.DEVICE_OPEN_DOOR_REMOTELY;
import static com.iotsmartaliv.constants.Constant.UrlPath.DEVICE_VIDEO_LIST_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.END_CALL_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.FORGOT_PASSWORD_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.GETVISITORSGROUP;
import static com.iotsmartaliv.constants.Constant.UrlPath.GETVISITORSINGROUP;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_AUTOMATION_SCHEDULE;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_BOOKED_ROOMS;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_BOOKINGS;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_CARD_SYNC_LIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_CARD_USERLIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_COUNTRY_CODES;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_CURRENT_TIME;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_FEED;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_FEED_CATEGORY;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_GROUP_LIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_INSTRUCTOR_INFO;
import static com.iotsmartaliv.constants.Constant.UrlPath.GET_USER_VISITORS;
import static com.iotsmartaliv.constants.Constant.UrlPath.INSTRUCTOR_INDUCTION;
import static com.iotsmartaliv.constants.Constant.UrlPath.INSTRUCTOR_INDUCTION_HR_RESPONSE;
import static com.iotsmartaliv.constants.Constant.UrlPath.INSTRUCTOR_LIST;
import static com.iotsmartaliv.constants.Constant.UrlPath.JOIN_COMMUNITY_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.LOGIN_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.OPEN_VIDEO_DEVICE_RELAY;
import static com.iotsmartaliv.constants.Constant.UrlPath.POST_ACCESS_LOG;
import static com.iotsmartaliv.constants.Constant.UrlPath.ROOM_CANCELLATION;
import static com.iotsmartaliv.constants.Constant.UrlPath.SEARCH_BOOKING;
import static com.iotsmartaliv.constants.Constant.UrlPath.SERVERTIMESYNC;
import static com.iotsmartaliv.constants.Constant.UrlPath.SIGN_UP_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.SUB_COMMUNITY_LIST_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_AUTOMATION_SCHEDULE;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_BROADCAST_READ_STATUS_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_CARD_SYNC_DATA;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_GROUP;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_PROFILE_API;
import static com.iotsmartaliv.constants.Constant.UrlPath.UPDATE_VISITOR;
import static com.iotsmartaliv.constants.Constant.UrlPath.VISITORSTOGROUPS;
import static com.iotsmartaliv.constants.Constant.UrlPath.VOIP;

/**
 * This interface is used as ApiServices that is provide the all api endpoint request.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 27/3/19 :March : 2019 on 14 : 59.
 */

public interface ApiServices {
    @FormUrlEncoded
    @POST(LOGIN_API)
    Call<SuccessResponse> performLogin(@FieldMap HashMap<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(SIGN_UP_API)
    Call<SuccessResponse> performSignUp(@FieldMap HashMap<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(FORGOT_PASSWORD_API)
    Call<SuccessResponse> callForgotPassword(@Field("username") String username);

    @FormUrlEncoded
    @POST(CHANGE_PASSWORD_API)
    Call<SuccessResponse> callChangePassword(@FieldMap HashMap<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(UPDATE_PROFILE_API)
    Call<SuccessResponse> performUpdateProfile(@FieldMap HashMap<String, String> paramHashMap);

    @GET(DEVICE_LIST_API)
    Call<SuccessDeviceListResponse> getAllDeviceList(@Query("appuser_ID") String userId,@Query("app_version") String appVersion);

    @GET(COMMUNITY_DEVICE_LIST_API)
    Call<SuccessResponse> getCommunityDeviceList(@Query("appuser_ID") String userId, @Query("community_ID") String community_ID);

    @GET(DEVICE_VIDEO_LIST_API)
    Call<VideoDeviceListModel> getVideoDeviceList(@Query("appuser_ID") String userId);

    @FormUrlEncoded
    @POST(POST_ACCESS_LOG)
    Call<SuccessResponse> callForPostAccessLog(@FieldMap HashMap<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(JOIN_COMMUNITY_API)
    Call<SuccessResponse> callForJoinCommunity(@FieldMap HashMap<String, String> paramHashMap);

    @GET(COMMUNITY_LIST_API)
    Call<SuccessArrayResponse> getAllCommunityList(@Query("appuser_ID") String userId);

    @GET(SUB_COMMUNITY_LIST_API)
    Call<SuccessArrayResponse> getAllSubCommunityList(@Query("community_ID") String community_ID);

    @GET(SERVERTIMESYNC)
    Call<String> getCurrentTimeForSync();

    @GET(VOIP)
    Call<VoIpModel> getVoip();
    @FormUrlEncoded
    @POST(END_CALL_API)
    Call<SuccessResponse> callAPIForENDCall(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST(DEVICE_OPEN_DOOR_REMOTELY)
    Call<SuccessResponse> callForOpenDoor(@FieldMap HashMap<String, String> stringStringHashMap);

    @FormUrlEncoded
    @POST(ADD_VISITOR_EVENT)
    Call<SuccessResponse> callForAddVisitorEvent(@FieldMap HashMap<String, String> stringStringHashMap);

    @FormUrlEncoded
    @POST(ADD_EVENT)
    Call<SuccessResponse> callForAddEnchanceVisitorEvent(@FieldMap HashMap<String, String> stringStringHashMap);

    @FormUrlEncoded
    @POST(ADD_GROUP)
    Call<SuccessResponse> callForAddGroup(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(GET_GROUP_LIST)
    Call<GroupResponse> getAllGroupList(@Query("appuser_ID") String userId);

    @FormUrlEncoded
    @POST(UPDATE_GROUP)
    Call<GroupResponse> callForGroupUpdateOrDelete(@FieldMap HashMap<String, String> stringStringHashMap);

    @FormUrlEncoded
    @POST(ADD_INSTRUCTOR)
    Call<SuccessResponse> callForAddInstructor(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(INSTRUCTOR_INDUCTION)
    Call<InstructorInductionDataResponse> getInstructorInductionList(@Query("appuser_ID") String userId);

    @FormUrlEncoded
    @POST(INSTRUCTOR_INDUCTION_HR_RESPONSE)
    Call<SuccessResponse> callForInductionHrResponse(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(INSTRUCTOR_LIST)
    Call<InstructorListResponse> getInstructorList(@Query("appuser_ID") String userId);

    @GET(GET_INSTRUCTOR_INFO)
    Call<InstructorInfoResponse> getInstructorInfo(@Query("contact_number") String contact_number, @Query("community_ID") String community_ID);

    @FormUrlEncoded
    @POST(ADD_VISITOR)
    Call<SuccessResponse> callForAddVisitor(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(GET_USER_VISITORS)
    Call<VisitorsListDataResponse> getAllVisitorList(@Query("appuser_ID") String userId);

    @FormUrlEncoded
    @POST(UPDATE_VISITOR)
    Call<VisitorsListDataResponse> callForVisitorUpdateOrDelete(@FieldMap HashMap<String, String> stringStringHashMap);

    @FormUrlEncoded
    @POST(VISITORSTOGROUPS)
    Call<SuccessResponse> callForAssignVisitorAndGroup(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(GET_COUNTRY_CODES)
    Call<CountryDataResponse> getListOfCountryCodes(@Query("timezone") String timezone);

    @GET(GETVISITORSINGROUP)
    Call<VisitorsListDataResponse> getVisitorsInGroup(@Query("appuser_ID") String appuser_ID, @Query("group_ID") String group_ID);

    @GET(GETVISITORSGROUP)
    Call<GroupResponse> getVisitorsGroup(@Query("appuser_ID") String appuser_ID, @Query("visit_ID") String visit_ID);

    @GET(SEARCH_BOOKING)
    Call<SearchBookingResponse> searchBooking(@QueryMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(BOOK_ROOM)
    Call<SearchBookingResponse> callForRoomBooking(@FieldMap HashMap<String, String> hashMap);

    @GET(GET_BOOKINGS)
    Call<BookingResponse> getBookedRoom(@Query("appuser_ID") String appuser_ID);

    @FormUrlEncoded
    @POST(ROOM_CANCELLATION)
    Call<BookingResponse> cancelBookingOrRoom(@FieldMap HashMap<String, String> hashMap);

    @GET(GET_BOOKED_ROOMS)
    Call<BookRoomsResponse> getBookedRooms(@Query("booking_ID") String appuser_ID);

    @GET(AUTOMATION_ROOM_LIST)
    Call<AutomationRoomsResponse> getAutomationRoomList(@Query("appuser_ID") String appuser_ID);

    @GET(GET_AUTOMATION_SCHEDULE)
    Call<AutomationScheduleResponse> getAutomationSchedule(@Query("schedule_ID") String schedule_ID);

    @FormUrlEncoded
    @POST(ADD_AUTOMATION_SCHEDULE)
    Call<AutomationRoomsResponse> callForAddAutomationSchedule(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(UPDATE_AUTOMATION_SCHEDULE)
    Call<AutomationRoomsResponse> callForUpdateAutomationSchedule(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(DELETE_SCHEDULE)
    Call<AutomationRoomsResponse> callForDeleteSchedule(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ADHOC)
    Call<AutomationRoomsResponse> callForUpdateDeviceStatus(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ADDFACEENROLLIMAGE)
    Call<SuccessResponse> callForUploadFaceEnrollImg(@Field("appuser_ID") String appuser_ID, @Field("image") String image);


    //   @FormUrlEncoded
    @POST
    Call<FcmUpdateModel> updateFCMTokenForIntercom(@Url String urlStr, @Body FcmUpdateModel fcmUpdateModel);

    @GET(GET_CARD_USERLIST)
    Call<CardUserListModel> getCardUserList(@Query("community_ID") String community_ID, @Query("device_ID") String device_ID, @Query("appuser_ID") String appuser_ID);

    @GET(GET_CARD_SYNC_LIST)
    Call<CardUserListModel> getCardList(@Query("community_ID") String community_ID, @Query("device_ID") String device_ID, @Query("appuser_ID") String appuser_ID);

    @FormUrlEncoded
    @POST(UPDATE_CARD_SYNC_DATA)
    Call<CardUserListModel> updateCardListOnsync(@FieldMap HashMap<String, String> stringStringHashMap);

    @GET(GET_CURRENT_TIME)
    Call<ResponseBody> getCurrentTime();

    @GET(Broadcast_API)
    Call<BroadcastModel> getUserBroadcast(@Query("appuser_ID") String appuser_ID);

    @FormUrlEncoded
    @POST(UPDATE_BROADCAST_READ_STATUS_API)
    Call<SuccessResponse> updateBroadcastReadStatus(@FieldMap HashMap<String, String> stringStringHashMap);

    @POST(CHECK_DEVICE_BOOKINGS)
    Call<SuccessDeviceListResponse> checkDeviceBookings(@Body CheckBookingRequest bookingRequest);

    @POST(OPEN_VIDEO_DEVICE_RELAY)
    Call<SuccessResponseModel> openRelay(@Body OpenVideoDeviceRelayRequest openRelayRequest);

    @POST(DELETE_APPUSER)
    Call<SuccessResponseModel> deleteUser(@Body DeleteUserRequest deleteUserRequest);


    @GET(GET_FEED)
    Call<FeedbackModel> getFeedList(@Query("appuser_ID") String userId, @Query("status") String status, @Query("limit") String limit, @Query("page") String page);
    @GET(GET_FEED_CATEGORY)
    Call<FeedBackCategoryModel> getFeedbackCatergory();

    @POST(ADD_FEEDBACK)
    Call<AddFeedbackResponse> addFeedback(@Body AddFeedbackRequest feedbackRequest);

    // Add Multipart annotation for uploading a file
    @Multipart
    @POST(ADD_FEEDBACK_DOCUMENT)
    Call<UploadFeedbackDocumentResponse> uploadFeedbackDocument(
            @Part("feedback_doc_name") RequestBody feedbackDocName,
            @Part MultipartBody.Part feedbackDocument
    );
}


