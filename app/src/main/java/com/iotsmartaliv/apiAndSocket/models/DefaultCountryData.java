package com.iotsmartaliv.apiAndSocket.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 7/8/19 :August.
 */
public class DefaultCountryData {
    @SerializedName("zone_id")
    @Expose
    private String zoneId;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("zone_name")
    @Expose
    private String zoneName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iso")
    @Expose
    private String iso;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nicename")
    @Expose
    private String nicename;
    @SerializedName("iso3")
    @Expose
    private String iso3;
    @SerializedName("numcode")
    @Expose
    private String numcode;
    @SerializedName("phonecode")
    @Expose
    private String phonecode;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getNumcode() {
        return numcode;
    }

    public void setNumcode(String numcode) {
        this.numcode = numcode;
    }

    public String getPhonecode() {
        return phonecode;
    }

    public void setPhonecode(String phonecode) {
        this.phonecode = phonecode;
    }
}
