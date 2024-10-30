package com.iotsmartaliv.model.booking;

public class PaymentMethodDetails {
    private Card card;
    private String type;
    private TranscationHistoryPayNowModel paynow; // For PayNow payment details


    // Getters and Setters
    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
