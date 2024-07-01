package com.iotsmartaliv.constants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.iotsmartaliv.R;
import com.iotsmartaliv.apiCalling.models.DeviceObject;
import com.iotsmartaliv.apiCalling.models.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class hold the constants.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-23
 */
public class Constant {

    public static final int VERIFY_NUM_WRONG = 1022;
    public static final int FAILED_CRERATE_COUNT = 1025;
    public static final int ACCOUNT_REGISETERED = 1024;
    public static final int NETWORD_SHUTDOWN = -1;
    //working base url
//    public static final String BASE_URL = "https://aliv.com.sg/alivapp/";
    public static final String LOGIN_API = "login";
    public static final String FORGOT_PASSWORD_API = "forgotPassword";
    public static final String USER_NAME = "username";
    public static final String EMAIL_ID = "email_ID";
    public static final String PASSWORD = "password";
    public static final String CONFIRM_PASSWORD = "cpassword";
    public static final String OAUTH_PROVIDER = "oauth_provider";
    public static final String OAUTH_UID = "oauth_uid";
    public static final String TERM_OF_USE = "termofuse";
    public static final String IS_LOGIN = "is_login";
    public static final String IS_INTERCOM_LOGIN = "is_intercom_login";

    public static final String HAS_ON_BOARDING_SHOWN = "should_show_on_boarding";
    public static final String SKIP_PROTECTIONAPPCHECK = "skipProtectedAppCheck";
    public static final String LOGIN_PREFRENCE = "LOGIN_PREFRENCE";
    public static final String SHAKE_ENABLE = "SHAKE_ENABLE";
    public static final String BACKGROUND_SHAKE_ENABLE = "BACKGROUND_SHAKE_ENABLE";
    public static final String SHAKE_DISTANCE = "SHAKE_DISTANCE";
    public static final int SHAKE_MAX_PROGRESS = 70;
    public static final int SHAKE_DEFAUT_PROGRESS = 60;

    public static final String DEVICE_ID = "device_Id";
    public static final String COMMUNITY_ID = "community_Id";
    public static final String ONBOARDING_PAGE_TITLE = "onboarding_page_title";
    public static final String ONBOARDING_PAGE_DESC = "onboarding_page_desc";
    public static final String ONBOARDING_PAGE_IMG = "onboarding_page_img";
    public static final String PERMISSION_ARRAY = "permission_array";
    public static final String FROM_DRAWER = "from_drawer";
    public static final String VO_IP = "VoIp";
    public static final String VO_PORT = "VoPort";
    /**
     * deviceList is a list of device list of particular user.
     */
    public static List<DeviceObject> deviceLIST = new ArrayList<>();
    /**
     * this is a user detail object. that is initialize on SplashActivity and user detail update.
     */
    public static ResponseData LOGIN_DETAIL;
    private static ProgressDialog dialog;

    public static String URL = "url";
        public static String USERNAME = "userName";
        public static String USERID = "userID";
        public static String ERRORCODE = "errorCode";
    public static String APITYPE = "apiType";
    public static String PARAMETER = "parameter";
    public static String APIERROR = "Api_Error";
    public static String DEVICEAPIERRO = "iOT_SeverLogin_Error";
    public static String LOGIN = "login";

    /**
     * This method is used for showing message in snack bar.
     *
     * @param coordinatorLayout
     * @param message
     */
    public final static void showMessageInSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    /**
     * This method is used for hide keyboard.
     *
     * @param mContext
     */
    public final static void hideKeyBoard(Activity mContext) {
        // Check if no view has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * This method is used for hide keyboard.
     *
     * @param mContext
     */
    public final static void showKeyBoard(Activity mContext) {
        // Check if no view has focus:
        View view = mContext.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    /**
     * Method to show progress dialog on API calling
     *
     * @param context
     */
    public static void showLoader(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setMessage(context.getResources().getString(R.string.please_wait));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * Method to hide the progress dialog.
     */
    public static void hideLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * Email Validation
     *
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(target);

        return matcher.matches();
    }


    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static String getDeviceToken(Activity activity) {
        String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("device_token", "device_" + androidId);
        return androidId;
    }

    public interface TimeOut {
        int IMAGE_UPLOAD_CONNECTION_TIMEOUT = 120;
        int IMAGE_UPLOAD_SOCKET_TIMEOUT = 120;
        int SOCKET_TIME_OUT = 60;
        int CONNECTION_TIME_OUT = 60;
    }

    public interface UrlPath {

        String BASEURL = "https://aliv.com.sg/"; // Live server.
//      String BASEURL = "https://live.aliv.com.sg/"; // Alpha server
        String PATH = "alivapp/";                  // Here is the path path of API.
        String SERVER_URL = BASEURL + PATH;        // Here is API Calling Complete URL.

        /**
         * This are the End Point list of User Operation API.
         */
        String SIGN_UP_API = "signup";
        String LOGIN_API = "login";
        String FORGOT_PASSWORD_API = "forgot_password";
        String CHANGE_PASSWORD_API = "change_password";
        String UPDATE_PROFILE_API = "updateProfile";

        String Broadcast_API = "getUserBroadcast";
        String UPDATE_BROADCAST_READ_STATUS_API = "updateBroadcastReadStatus";

        /**
         * This are the End Point list of Device API.
         */
        String  DEVICE_LIST_API = "userDevices";

        /**
         * This is the End Point list of Community Device API.
         */
        String COMMUNITY_DEVICE_LIST_API = "commDevices";

        /**
         * This are the End Point list of video Device API.
         */
        String DEVICE_VIDEO_LIST_API = "userVideoDevices";

        /**
         * This are the End Point list of video Device API.
         */
        String DEVICE_OPEN_DOOR_REMOTELY = "openDoorRemote";


        /**
         * This is the End Point of Access Log Device API.
         */
        String POST_ACCESS_LOG = "postAccessLog";


        /**
         * This are the End Point list of Community API.
         */
        String JOIN_COMMUNITY_API = "joinCommunity";
        String COMMUNITY_LIST_API = "userCommunities";
        String SUB_COMMUNITY_LIST_API = "subCommunities";
        String SERVERTIMESYNC = "serverTimeSync";
        String VOIP = "getVoipData";

        /**
         * this for end the call of twilio API
         */
        String END_CALL_API = "endCall";


        /**
         * this for end the call of visitor management API
         */
        String ADD_VISITOR_EVENT = "addVisitorEvent";
        String ADD_EVENT = "addEvent";
        /**
         * this for end the call of add Instructor API
         */
        String ADD_INSTRUCTOR = "addInstructor";
        String INSTRUCTOR_INDUCTION = "instructorInductionList";
        String INSTRUCTOR_INDUCTION_HR_RESPONSE = "inductionHRResponse";
        String INSTRUCTOR_LIST = "instructorList";
        String GET_INSTRUCTOR_INFO = "getInstructorInfo";

        /**
         * this for end the call for all Visitor management API
         */
        String ADD_GROUP = "addVisitorGroup";
        String GET_GROUP_LIST = "getVisitorGroup";
        String UPDATE_GROUP = "updateVisitorGroup";
        String ADD_VISITOR = "addVisitor";
        String GET_USER_VISITORS = "getUserVisitors";
        String UPDATE_VISITOR = "updateVisitor";
        String GET_COUNTRY_CODES = "getCountryCodes";
        String VISITORSTOGROUPS = "addVisitorsToGroups";
        String GETVISITORSINGROUP = "getVisitorsInGroup";
        String GETVISITORSGROUP = "getVisitorsGroups";


        /**
         * end point for the search Booking.
         */
        String SEARCH_BOOKING = "searchBooking";
        String BOOK_ROOM = "roomBooking";
        String GET_BOOKINGS = "getBookings";
        String ROOM_CANCELLATION = "roomCancellation";
        String GET_BOOKED_ROOMS = "getBookedRooms";

        /**
         * Room automation API
         */

        String AUTOMATION_ROOM_LIST = "getAutomationRoomList";
        String ADD_AUTOMATION_SCHEDULE = "addAutomationSchedule";
        String UPDATE_AUTOMATION_SCHEDULE = "updateAutomationSchedule";
        String DELETE_SCHEDULE = "deleteSchedule";
        String GET_AUTOMATION_SCHEDULE = "getAuomationSchedule";
        String ADHOC = "adhoc";

        /**
         * Face enroll
         */
        String ADDFACEENROLLIMAGE = "addFacialImage";


        /**
         * this API for the Card Manager.
         * it is use for get user list that are assign in the group.
         */

        String GET_CARD_SYNC_LIST = "getCardSyncList";

        String GET_CARD_USERLIST = "getCardUserList";

        String UPDATE_CARD_SYNC_DATA = "updateCardSyncData";
        String GET_CURRENT_TIME = "getCurrentTime";
        String CHECK_DEVICE_BOOKINGS = "checkDeviceBookings";


        /**
         * API call for video intercom server to update the firebase token video intercom calling.
         * <p>
         * <p>
         * url: http://43.229.85.122:8099/doormaster/vphone/login/info
         * method: POST
         * json data:
         * {
         * "account":"app account",
         * "token":"fcm token",
         * "brands":"google",
         * "phone":"android",
         * "app_type":"1"
         * }
         */
        String FCM_TOKEN_UPDATE_SERVICE_URL = "http://43.229.85.122:8099/doormaster/vphone/login/";
        String FCM_TOKEN_UPDATE_SERVICE_ENDPOINT = "info";



    }

    /**
     * This Interface is contains all error response code.
     */
    public interface ErrorClass {
        String CODE = "code";
        String STATUS = "status";
        String MESSAGE = "message";
        String DEVELOPER_MESSAGE = "developerMessage";
    }
}
