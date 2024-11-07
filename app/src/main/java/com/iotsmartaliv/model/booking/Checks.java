package com.iotsmartaliv.model.booking;

public class Checks {
    private String address_line1_check;
    private String address_postal_code_check;
    private String cvc_check;

    // Getters and Setters
    public String getAddress_line1_check() {
        return address_line1_check;
    }

    public void setAddress_line1_check(String address_line1_check) {
        this.address_line1_check = address_line1_check;
    }

    public String getAddress_postal_code_check() {
        return address_postal_code_check;
    }

    public void setAddress_postal_code_check(String address_postal_code_check) {
        this.address_postal_code_check = address_postal_code_check;
    }

    public String getCvc_check() {
        return cvc_check;
    }

    public void setCvc_check(String cvc_check) {
        this.cvc_check = cvc_check;
    }
}
