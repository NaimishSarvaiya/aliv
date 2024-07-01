package com.iotsmartaliv.model;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 31 Mar,2021
 */
public class MassageModel {

    private String massageHead;
    private String massageDate;


    public MassageModel(String massageHead, String massageDate) {
        this.massageHead = massageHead;
        this.massageDate = massageDate;

    }

    public String getMassageHead() {
        return massageHead;
    }

    public void setMassageHead(String movieName) {
        this.massageHead = movieName;
    }

    public String getMassageDate() {
        return massageDate;
    }

    public void setMassageDate(String movieName) {
        this.massageDate = movieName;
    }

}
