package com.sugarya.closet.ui.home.event;

import com.sugarya.closet.model.entity.ProductEntity;

/**
 * Created by Ethan on 2017/9/24.
 */

public class SelectedDressingRoomMatchItemEvent {

    private ProductEntity mProductEntity;

    public SelectedDressingRoomMatchItemEvent(ProductEntity productEntity) {
        mProductEntity = productEntity;
    }

    public ProductEntity getProductEntity() {
        return mProductEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        mProductEntity = productEntity;
    }
}
