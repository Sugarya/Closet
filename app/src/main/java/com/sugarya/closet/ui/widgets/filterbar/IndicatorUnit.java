package com.sugarya.closet.ui.widgets.filterbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ethan on 2017/8/15.
 * <p>
 * 筛选条单元实体类
 */

public class IndicatorUnit {

    /**
     * 各个筛选单元的标题
     */
    private String unitTitle;
    /**
     * 打开下拉视图时，是否需要暗色屏幕显示
     */
    private boolean screenDimAvailable;
    /**
     * 下拉视图动画模式 1.平移  2.折叠
     */
    private FilterBarLayout.FooterMode footerMode;

    private View indicatorUnit;
    /**
     * 筛选标题控件
     */
    private TextView tvUnit;
    /**
     * 筛选单元图片
     */
    private ImageView imgUnit;
    /**
     * 是否处在下拉状态
     */
    private boolean isExpanded = false;

    private boolean isCanceledOnTouchOutside = false;

    private View footerView;

    IndicatorUnit(String unitTitle, boolean screenDimAvailable) {
        this.unitTitle = unitTitle;
        this.screenDimAvailable = screenDimAvailable;
    }

    public IndicatorUnit(String unitTitle, boolean screenDimAvailable, FilterBarLayout.FooterMode footerMode, View indicatorUnit, TextView tvUnit, ImageView imgUnit, boolean isExpanded, boolean isCanceledOnTouchOutside, View footerView) {
        this.unitTitle = unitTitle;
        this.screenDimAvailable = screenDimAvailable;
        this.footerMode = footerMode;
        this.indicatorUnit = indicatorUnit;
        this.tvUnit = tvUnit;
        this.imgUnit = imgUnit;
        this.isExpanded = isExpanded;
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
        this.footerView = footerView;
    }

    String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    boolean isScreenDimAvailable() {
        return screenDimAvailable;
    }

    void setScreenDimAvailable(boolean screenDimAvailable) {
        this.screenDimAvailable = screenDimAvailable;
    }

    FilterBarLayout.FooterMode getFooterMode() {
        return footerMode;
    }

    void setFooterMode(FilterBarLayout.FooterMode footerMode) {
        this.footerMode = footerMode;
    }

     View getIndicatorUnit() {
        return indicatorUnit;
    }

     void setIndicatorUnit(View indicatorUnit) {
        this.indicatorUnit = indicatorUnit;
    }

     TextView getTvUnit() {
        return tvUnit;
    }

     void setTvUnit(TextView tvUnit) {
        this.tvUnit = tvUnit;
    }

     ImageView getImgUnit() {
        return imgUnit;
    }

     void setImgUnit(ImageView imgUnit) {
        this.imgUnit = imgUnit;
    }

     boolean isExpanded() {
        return isExpanded;
    }

     void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

     View getFooterView() {
        return footerView;
    }

     void setFooterView(View footerView) {
        this.footerView = footerView;
    }

     boolean isCanceledOnTouchOutside() {
        return isCanceledOnTouchOutside;
    }

     void setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        isCanceledOnTouchOutside = canceledOnTouchOutside;
    }
}
