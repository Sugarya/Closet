package com.sugarya.closet.model.entity;

import android.net.Uri;
import android.support.annotation.DrawableRes;

import com.sugarya.closet.model.base.BaseEntity;

/**
 * Created by Ethan on 2017/9/19.
 */

public class ProductEntity extends BaseEntity {

    private String linkUrl;
    private String picUrl;
    private String productName;
    private String productPrice;
    private @DrawableRes int resId;
    private boolean isInDressingRoom = false;
    private Uri uri;

    public Uri getUri() {
        return uri;
    }


    public ProductEntity(boolean isInDressingRoom, Uri uri) {
        this.isInDressingRoom = isInDressingRoom;
        this.uri = uri;
    }

    public ProductEntity(String linkUrl, int resId) {
        this.linkUrl = linkUrl;
        this.resId = resId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductEntity)) return false;

        ProductEntity that = (ProductEntity) o;

        return getResId() == that.getResId();

    }

    @Override
    public int hashCode() {
        return getResId();
    }

    public boolean isInDressingRoom() {
        return isInDressingRoom;
    }

    public void setInDressingRoom(boolean inDressingRoom) {
        isInDressingRoom = inDressingRoom;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public ProductEntity(int resId) {
        this.resId = resId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
