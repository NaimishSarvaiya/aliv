package com.iotsmartaliv.model;

/**
 * This class is used as
 *
 * @author CanopusInfoSystems
 * @version 1.0
 * @since 03 Apr,2021
 */
public class DocumentModel {
    private String txtDoc;
    private int imgDoc;

    public String getTxtDoc() {
        return txtDoc;
    }

    public int getImgDoc() {
        return imgDoc;
    }

    public void setTxtDoc(String txtDoc) {
        this.txtDoc = txtDoc;
    }

    public void setImgDoc(int imgDoc) {
        this.imgDoc = imgDoc;
    }

    public DocumentModel(String txtDoc, int imgDoc) {
        this.txtDoc = txtDoc;
        this.imgDoc = imgDoc;
    }


}
