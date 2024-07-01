package com.iotsmartaliv.model;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class EventModel {
    private String massageHead;
    private String massageDate;
    private String massageOrganizer;
    private int massageImage;


    public int getMassageImage() {
        return massageImage;
    }

    public void setMassageImage(int massageImage) {
        this.massageImage = massageImage;
    }

    public EventModel(String massageDate, String massageHead, String massageOrganizer, int massageImage){
        this.massageDate = massageDate;
        this.massageHead = massageHead;
        this.massageOrganizer = massageOrganizer;
        this.massageImage = massageImage;
    }

    public String getMassageHead() {
        return massageHead;
    }

    public String getMassageDate() {
        return massageDate;
    }

    public String getMassageOrganizer() {
        return massageOrganizer;
    }
   

    public void setMassageHead(String massageHead) {
        this.massageHead = massageHead;
    }

    public void setMassageDate(String massageDate) {
        this.massageDate = massageDate;
    }

    public void setMassageOrganizer(String massageOrganizer) {
        this.massageOrganizer = massageOrganizer;
    }


}
