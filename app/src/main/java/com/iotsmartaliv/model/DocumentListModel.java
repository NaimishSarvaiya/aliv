package com.iotsmartaliv.model;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class DocumentListModel {

    private String txtDocList;
    private int imgDocList;

    public String getTxtDocList() {
        return txtDocList;
    }

    public int getImgDocList() {
        return imgDocList;
    }

    public void setTxtDocList(String txtDocList) {
        this.txtDocList = txtDocList;
    }

    public void setImgDocList(int imgDocList) {
        this.imgDocList = imgDocList;
    }

    public DocumentListModel(String txtDocList, int imgDocList) {
        this.txtDocList = txtDocList;
        this.imgDocList = imgDocList;
    }


}
