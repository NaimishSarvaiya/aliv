package com.iotsmartaliv.apiCalling.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is used as model that is a constants all the object value of user detail.
 * using as user detail object.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 28/3/19 :March : 2019 on 20 : 04.
 */
public class ResponseData {
    @SerializedName("appuser_ID")
    @Expose
    private String appuserID;
    @SerializedName("oauth_provider")
    @Expose
    private String oauthProvider;
    @SerializedName("oauth_uid")
    @Expose
    private String oauthUid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_full_name")
    @Expose
    private String userFullName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("uid_number")
    @Expose
    private String uidNumber;
    @SerializedName("user_profile_pic")
    @Expose
    private String userProfilePic;
    @SerializedName("user_country_code")
    @Expose
    private String userCountryCode;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("appuser")
    @Expose
    private String appuser;
    @SerializedName("invite_code")
    @Expose
    private String inviteCode;
    @SerializedName("termofuse")
    @Expose
    private String termofuse;
    @SerializedName("user_created_by")
    @Expose
    private String userCreatedBy;
    @SerializedName("failed_attempt")
    @Expose
    private String failedAttempt;
    @SerializedName("old_password_1")
    @Expose
    private String oldPassword1;
    @SerializedName("old_password_2")
    @Expose
    private String oldPassword2;
    @SerializedName("old_password_3")
    @Expose
    private String oldPassword3;
    @SerializedName("login_status")
    @Expose
    private String loginStatus;
    @SerializedName("password_last_change")
    @Expose
    private String passwordLastChange;
    @SerializedName("user_token")
    @Expose
    private String userToken;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("smartcity_userID")
    @Expose
    private String smartcityUserID;
    @SerializedName("account_token_pwd")
    @Expose
    private String accountTokenPwd;
    @SerializedName("appuser_type")
    @Expose
    private String appuserType;
    @SerializedName("admin_community")
    @Expose
    private String adminCommunity;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("devices")
    @Expose
    private List<DeviceObject> devices = null;
    @SerializedName("country")
    @Expose
    private List<Country> country = null;
    @SerializedName("default_country")
    @Expose
    private String defaultCountry;
    @SerializedName("facial_image")
    @Expose
    private String facialImage;
    @SerializedName("role_IDs")
    @Expose
    private List<String> roleIDs = null;

    public String getAppuserID() {
        return appuserID;
    }

    public void setAppuserID(String appuserID) {
        this.appuserID = appuserID;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getOauthUid() {
        return oauthUid;
    }

    public void setOauthUid(String oauthUid) {
        this.oauthUid = oauthUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUidNumber() {
        return uidNumber;
    }

    public void setUidNumber(String uidNumber) {
        this.uidNumber = uidNumber;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserCountryCode() {
        return userCountryCode;
    }

    public void setUserCountryCode(String userCountryCode) {
        this.userCountryCode = userCountryCode;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAppuser() {
        return appuser;
    }

    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }

    public Object getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getTermofuse() {
        return termofuse;
    }

    public void setTermofuse(String termofuse) {
        this.termofuse = termofuse;
    }

    public String getUserCreatedBy() {
        return userCreatedBy;
    }

    public void setUserCreatedBy(String userCreatedBy) {
        this.userCreatedBy = userCreatedBy;
    }

    public String getFailedAttempt() {
        return failedAttempt;
    }

    public void setFailedAttempt(String failedAttempt) {
        this.failedAttempt = failedAttempt;
    }

    public String getOldPassword1() {
        return oldPassword1;
    }

    public void setOldPassword1(String oldPassword1) {
        this.oldPassword1 = oldPassword1;
    }

    public String getOldPassword2() {
        return oldPassword2;
    }

    public void setOldPassword2(String oldPassword2) {
        this.oldPassword2 = oldPassword2;
    }

    public String getOldPassword3() {
        return oldPassword3;
    }

    public void setOldPassword3(String oldPassword3) {
        this.oldPassword3 = oldPassword3;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getPasswordLastChange() {
        return passwordLastChange;
    }

    public void setPasswordLastChange(String passwordLastChange) {
        this.passwordLastChange = passwordLastChange;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSmartcityUserID() {
        return smartcityUserID;
    }

    public void setSmartcityUserID(String smartcityUserID) {
        this.smartcityUserID = smartcityUserID;
    }

    public String getAccountTokenPwd() {
        return accountTokenPwd;
    }

    public void setAccountTokenPwd(String accountTokenPwd) {
        this.accountTokenPwd = accountTokenPwd;
    }

    public String getAppuserType() {
        return appuserType;
    }

    public void setAppuserType(String appuserType) {
        this.appuserType = appuserType;
    }

    public String getAdminCommunity() {
        return adminCommunity;
    }

    public void setAdminCommunity(String adminCommunity) {
        this.adminCommunity = adminCommunity;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<DeviceObject> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceObject> devices) {
        this.devices = devices;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    public List<String> getRoleIDs() {
        return roleIDs;
    }

    public void setRoleIDs(List<String> roleIDs) {
        this.roleIDs = roleIDs;
    }

    public String getFacialImage() {
        return facialImage;
    }

    public void setFacialImage(String facialImage) {
        this.facialImage = facialImage;
    }
}
