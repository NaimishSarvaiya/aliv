package com.iotsmartaliv.model.feedback;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeedbackCategoryData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cat_name")
    @Expose
    private String catName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

}
