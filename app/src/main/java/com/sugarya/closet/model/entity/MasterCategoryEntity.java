package com.sugarya.closet.model.entity;

import com.sugarya.closet.model.base.BaseEntity;

/**
 * Created by Ethan on 2017/9/19.
 */

public class MasterCategoryEntity extends BaseEntity {

//    private String picUrl = "";
//    private String selectedPicUrl = "";
    private String cateName = "";

    private int picResId;
    private int selectedPicResId;

    public MasterCategoryEntity() {
    }


    public MasterCategoryEntity(String cateName, int picResId, int selectedPicResId) {
        this.cateName = cateName;
        this.picResId = picResId;
        this.selectedPicResId = selectedPicResId;
    }


    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public int getPicResId() {
        return picResId;
    }

    public void setPicResId(int picResId) {
        this.picResId = picResId;
    }

    public int getSelectedPicResId() {
        return selectedPicResId;
    }

    public void setSelectedPicResId(int selectedPicResId) {
        this.selectedPicResId = selectedPicResId;
    }
}
