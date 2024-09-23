package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FcmUpdateModel {

    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("brands")
    @Expose
    private String brands;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("app_type")
    @Expose
    private String appType;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("ret")
    @Expose
    private Integer ret;

    public FcmUpdateModel(String account, String token, String brands, String phone, String appType) {
        this.account = account;
        this.token = token;
        this.brands = brands;
        this.phone = phone;
        this.appType = appType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBrands() {
        return brands;
    }

    public void setBrands(String brands) {
        this.brands = brands;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

}