package com.iotsmartaliv.modules.cardManager;

public class CardModel {
    String userName;
    String CardNo;
    int status;

    public CardModel(String userName, String cardNo, int status) {
        this.userName = userName;
        CardNo = cardNo;
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
