package com.iotsmartaliv.model;

/**
 * This class is used as .
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 18/7/19 :July : 2019 on 17 : 13.
 */
public class AddVisitor {
    private String visitorName;
    private String countryID;
    private String countryCode;
    private String contactNumber;
    private String licensePlate;


    public AddVisitor(String visitorName, String countryCode, String contactNumber, String countryID,String licensePlate) {
        this.visitorName = visitorName;
        this.countryCode = countryCode;
        this.contactNumber = contactNumber;
        this.countryID = countryID;
        this.licensePlate = licensePlate;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
