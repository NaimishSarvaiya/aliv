package com.iotsmartaliv.model;

/**
 * This model class is used for cards.
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 2018-10-24
 */
public class CardModel {
    String crad_no, holder_name, expiry_date;

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getCrad_no() {
        return crad_no;
    }

    public void setCrad_no(String crad_no) {
        this.crad_no = crad_no;
    }

    public String getHolder_name() {
        return holder_name;
    }

    public void setHolder_name(String holder_name) {
        this.holder_name = holder_name;
    }
}
