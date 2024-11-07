package com.iotsmartaliv.model.booking;

public class CreateCustomerOnStripRequest {
    private String email;
    private String name;

    public CreateCustomerOnStripRequest(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
