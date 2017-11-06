package com.sugarya.closet.model.request;

import com.google.gson.annotations.SerializedName;
import com.sugarya.closet.model.base.BaseParams;

/**
 * Created by Ethan on 2017/9/23.
 */

public class UpLoadParams extends BaseParams {

    @SerializedName("img")
    private byte[] img;

    public UpLoadParams(byte[] img) {
        this.img = img;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
