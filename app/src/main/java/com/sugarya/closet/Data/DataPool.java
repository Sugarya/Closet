package com.sugarya.closet.Data;

import com.sugarya.closet.model.entity.HomeCategoryEntity;
import com.sugarya.closet.model.entity.ProductEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 2017/9/24.
 */

public class DataPool {

    /**
     * 首页数据
     */
    private List<HomeCategoryEntity> mHomeCategoryEntityList = new ArrayList<>();
    /**
     * 试衣间数据
     */
    private List<ProductEntity> mSelectedProductEntityList = new ArrayList<>();

    public List<HomeCategoryEntity> getHomeCategoryEntityList() {
        return mHomeCategoryEntityList;
    }

    public void setHomeCategoryEntityList(List<HomeCategoryEntity> homeCategoryEntityList) {
        mHomeCategoryEntityList = homeCategoryEntityList;
    }


    public List<ProductEntity> getSelectedProductEntityList() {
        return mSelectedProductEntityList;
    }

    public void setSelectedProductEntityList(List<ProductEntity> selectedProductEntityList) {
        mSelectedProductEntityList = selectedProductEntityList;
    }


}
