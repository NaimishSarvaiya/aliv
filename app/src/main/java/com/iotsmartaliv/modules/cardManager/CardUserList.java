package com.iotsmartaliv.modules.cardManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardUserList {
    @SerializedName("ID")
    @Expose
    private String iD;
    @SerializedName("appuser_ID")
    @Expose
    private String appuserID;
    @SerializedName("device_ID")
    @Expose
    private String deviceID;
    @SerializedName("operation")
    @Expose
    private String operation;
    @SerializedName("status")
    @Expose
    private String status;
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
    private Object userProfilePic;
    @SerializedName("user_country_code")
    @Expose
    private Object userCountryCode;
    @SerializedName("phone_no")
    @Expose
    private Object phoneNo;
    @SerializedName("appuser")
    @Expose
    private String appuser;
    @SerializedName("invite_code")
    @Expose
    private Object inviteCode;
    @SerializedName("fcm_token")
    @Expose
    private String fcmToken;
    @SerializedName("facial_image")
    @Expose
    private Object facialImage;
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
    private Object oldPassword1;
    @SerializedName("old_password_2")
    @Expose
    private Object oldPassword2;
    @SerializedName("old_password_3")
    @Expose
    private Object oldPassword3;
    @SerializedName("login_status")
    @Expose
    private String loginStatus;
    @SerializedName("last_login")
    @Expose
    private Object lastLogin;
    @SerializedName("password_last_change")
    @Expose
    private String passwordLastChange;
    @SerializedName("user_token")
    @Expose
    private Object userToken;
    @SerializedName("deleted")
    @Expose
    private String deleted;
    @SerializedName("comm_user_ID")
    @Expose
    private String commUserID;
    @SerializedName("community_ID")
    @Expose
    private String communityID;
    @SerializedName("authorized")
    @Expose
    private String authorized;

    public String getID() {
        return iD;
    }

    public void setID(String iD) {
        this.iD = iD;
    }

    public String getAppuserID() {
        return appuserID;
    }

    public void setAppuserID(String appuserID) {
        this.appuserID = appuserID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Object getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(Object userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public Object getUserCountryCode() {
        return userCountryCode;
    }

    public void setUserCountryCode(Object userCountryCode) {
        this.userCountryCode = userCountryCode;
    }

    public Object getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Object phoneNo) {
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

    public void setInviteCode(Object inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public Object getFacialImage() {
        return facialImage;
    }

    public void setFacialImage(Object facialImage) {
        this.facialImage = facialImage;
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

    public Object getOldPassword1() {
        return oldPassword1;
    }

    public void setOldPassword1(Object oldPassword1) {
        this.oldPassword1 = oldPassword1;
    }

    public Object getOldPassword2() {
        return oldPassword2;
    }

    public void setOldPassword2(Object oldPassword2) {
        this.oldPassword2 = oldPassword2;
    }

    public Object getOldPassword3() {
        return oldPassword3;
    }

    public void setOldPassword3(Object oldPassword3) {
        this.oldPassword3 = oldPassword3;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public Object getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Object lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getPasswordLastChange() {
        return passwordLastChange;
    }

    public void setPasswordLastChange(String passwordLastChange) {
        this.passwordLastChange = passwordLastChange;
    }

    public Object getUserToken() {
        return userToken;
    }

    public void setUserToken(Object userToken) {
        this.userToken = userToken;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getCommUserID() {
        return commUserID;
    }

    public void setCommUserID(String commUserID) {
        this.commUserID = commUserID;
    }

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getAuthorized() {
        return authorized;
    }

    public void setAuthorized(String authorized) {
        this.authorized = authorized;
    }

}
