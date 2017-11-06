package com.sugarya.closet.ui.home.event;

import com.sugarya.closet.model.entity.ProductEntity;

/**
 * Created by Ethan on 2017/9/24.
 */

public class AddToDressRoomEvent {

    private ProductEntity mProductEntity;
    private int deltaCount;

    public AddToDressRoomEvent(ProductEntity productEntity, int deltaCount) {
        mProductEntity = productEntity;
        this.deltaCount = deltaCount;
    }

    public int getDeltaCount() {
        return deltaCount;
    }

    public void setDeltaCount(int deltaCount) {
        this.deltaCount = deltaCount;
    }

    public ProductEntity getProductEntity() {
        return mProductEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        mProductEntity = productEntity;
    }
}
