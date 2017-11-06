package com.sugarya.closet.model.entity;

import com.sugarya.closet.model.base.BaseEntity;

import java.util.List;

/**
 * Created by Ethan on 2017/9/19.
 */

public class HomeCategoryEntity extends BaseEntity {

    private MasterCategoryEntity mCategoryEntity;
    private List<ProductEntity> mProductEntityList;

    public HomeCategoryEntity() {
    }

    public HomeCategoryEntity(MasterCategoryEntity categoryEntity) {
        mCategoryEntity = categoryEntity;
    }

    public HomeCategoryEntity(MasterCategoryEntity categoryEntity, List<ProductEntity> productEntityList) {
        mCategoryEntity = categoryEntity;
        mProductEntityList = productEntityList;
    }

    public MasterCategoryEntity getCategoryEntity() {
        return mCategoryEntity;
    }

    public void setCategoryEntity(MasterCategoryEntity categoryEntity) {
        mCategoryEntity = categoryEntity;
    }

    public List<ProductEntity> getProductEntityList() {
        return mProductEntityList;
    }

    public void setProductEntityList(List<ProductEntity> productEntityList) {
        mProductEntityList = productEntityList;
    }
}
