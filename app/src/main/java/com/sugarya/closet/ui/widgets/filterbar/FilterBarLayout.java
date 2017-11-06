package com.sugarya.closet.ui.widgets.filterbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sugarya.closet.R;

import java.util.LinkedList;
import java.util.List;

import static com.sugarya.closet.ui.widgets.filterbar.FilterBarLayout.FooterMode.MODE_EXPAND;


/**
 * Created by Ethan on 2017/8/9.
 * 筛选条，支持代码和布局混合控制，支持五层筛选条件（可轻松拓展筛选数量），支持两种张开方式，支持高度wrap_content配置
 * 父容器必须是FrameLayout及其子控件
 * todo 1.网络请求时，过度动画，放在FooterView上实现
 */
public class FilterBarLayout extends RelativeLayout {

    private static final String TAG = "FilterBarLayout";

    private static final String BODY_LAYOUT_TAG = "body_layout";
    private static final int OPEN_FOOTER_ANIMATION_DURATION = 280;
    private static final int CLOSE_FOOTER_ANIMATION_DURATION = 280;

    private static final int DEFAULT_FILTER_BAR_UNIT_HEIGHT = 135;
    private static final int DEFAULT_FILTER_COVER_COLOR = Color.parseColor("#66000000");
    private static final int DEFAULT_FILTER_TITLE_COLOR = Color.parseColor("#333333");
    private static final int DEFAULT_FILTER_TITLE_SELECTED_COLOR = Color.parseColor("#00a7f8");
    private static final int DEFAULT_LINE_COLOR = Color.parseColor("#e0e0e0");
    private static final int DEFAULT_FILTER_TITLE_SIZE = 20;
    private static final int ORIGIN_HEIGHT = 1;
    private static final int DEFAULT_INDICATOR_GRAVITY = Gravity.CENTER_HORIZONTAL;

    private int mFilterBarHeight = DEFAULT_FILTER_BAR_UNIT_HEIGHT;
    private int mFilterCoverColor = DEFAULT_FILTER_COVER_COLOR;
    private int mFilterTitleColor = DEFAULT_FILTER_TITLE_COLOR;
    private float mFilterTitleSize = DEFAULT_FILTER_TITLE_SIZE;
    private int mFilterTitleSelectedColor = DEFAULT_FILTER_TITLE_SELECTED_COLOR;
    private int mIndicatorGravity = DEFAULT_INDICATOR_GRAVITY;
    private String mFirstFilterTitle;
    private String mSecondFilterTitle;
    private String mThirdFilterTitle;
    private String mFourthFilterTitle;
    private String mFifthFilterTitle;
    private Drawable mIndicatorDrawable;
    private Drawable mIndicatorSelectedDrawable;

    private List<IndicatorUnit> mIndicatorUnitList = new LinkedList<>();

    /**
     * 筛选条点击监听
     */
    private OnFilterIndicatorClickListener mOnFilterIndicatorClickListener;
    /**
     * 筛选条主体布局
     */
    private LinearLayout mIndicatorLayout;
    /**
     * 包裹筛选条主体的布局
     */
    private RelativeLayout mContainIndicatorAndFooterLayout;
    /**
     * 原始布局参数
     */
    private LayoutParams mOriginRootLayoutParams;
    /**
     * 原始布局参数
     */
    private LayoutParams mOriginContainIndicatorAndFooterLayoutParams;

    /**
     * 是否正在展示下拉视图
     */
    private boolean isShowing = false;
    /**
     * 当前选中的
     */
    private int mSelectedIndex = -1;


    public FilterBarLayout(Context context) {
        super(context);
        init(context);
    }

    public FilterBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTypedArray(context, attrs);
        init(context);
    }

    public FilterBarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FilterBarLayout);
        mFirstFilterTitle = typedArray.getString(R.styleable.FilterBarLayout_firstFilterText);
        mSecondFilterTitle = typedArray.getString(R.styleable.FilterBarLayout_secondFilterText);
        mThirdFilterTitle = typedArray.getString(R.styleable.FilterBarLayout_thirdFilterText);
        mFourthFilterTitle = typedArray.getString(R.styleable.FilterBarLayout_fourthFilterText);
        mFifthFilterTitle = typedArray.getString(R.styleable.FilterBarLayout_fifthFilterText);

        mFilterBarHeight = (int) typedArray.getDimension(R.styleable.FilterBarLayout_filterBarHeight, DEFAULT_FILTER_BAR_UNIT_HEIGHT);
        mFilterTitleSize = typedArray.getDimension(R.styleable.FilterBarLayout_filterTextSize, DEFAULT_FILTER_TITLE_SIZE);
        mFilterTitleColor = typedArray.getColor(R.styleable.FilterBarLayout_filterTextColor, DEFAULT_FILTER_TITLE_COLOR);
        mFilterTitleSelectedColor = typedArray.getColor(R.styleable.FilterBarLayout_filterTextSelectedColor, DEFAULT_FILTER_TITLE_SELECTED_COLOR);
        mFilterCoverColor = typedArray.getColor(R.styleable.FilterBarLayout_filterCoverColor, DEFAULT_FILTER_COVER_COLOR);

        mIndicatorDrawable = typedArray.getDrawable(R.styleable.FilterBarLayout_indicatorDrawable);
        mIndicatorSelectedDrawable = typedArray.getDrawable(R.styleable.FilterBarLayout_indicatorSelectedDrawable);
        mIndicatorGravity = typedArray.getInt(R.styleable.FilterBarLayout_indicatorGravity, DEFAULT_INDICATOR_GRAVITY);

        typedArray.recycle();

        initializeFromTypedArray();
    }

    private void initializeFromTypedArray() {
        if (!TextUtils.isEmpty(mFirstFilterTitle)) {
            IndicatorUnit indicatorUnit = new IndicatorUnit(mFirstFilterTitle, true);
            mIndicatorUnitList.add(indicatorUnit);
        }
        if (!TextUtils.isEmpty(mSecondFilterTitle)) {
            IndicatorUnit indicatorUnit = new IndicatorUnit(mSecondFilterTitle, true);
            mIndicatorUnitList.add(indicatorUnit);
        }
        if (!TextUtils.isEmpty(mThirdFilterTitle)) {
            IndicatorUnit indicatorUnit = new IndicatorUnit(mThirdFilterTitle, true);
            mIndicatorUnitList.add(indicatorUnit);
        }
        if (!TextUtils.isEmpty(mFourthFilterTitle)) {
            IndicatorUnit indicatorUnit = new IndicatorUnit(mFourthFilterTitle, true);
            mIndicatorUnitList.add(indicatorUnit);
        }
        if (!TextUtils.isEmpty(mFifthFilterTitle)) {
            IndicatorUnit indicatorUnit = new IndicatorUnit(mFifthFilterTitle, true);
            mIndicatorUnitList.add(indicatorUnit);
        }
    }

    private void init(Context context) {
        mContainIndicatorAndFooterLayout = new RelativeLayout(getContext());
        mOriginContainIndicatorAndFooterLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContainIndicatorAndFooterLayout.setLayoutParams(mOriginContainIndicatorAndFooterLayoutParams);

        mIndicatorLayout = new LinearLayout(context);
        //用来标记子控件,以便重排子控件在父容器顺序
        mIndicatorLayout.setTag(BODY_LAYOUT_TAG);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFilterBarHeight);
        mIndicatorLayout.setLayoutParams(layoutParams);
        mIndicatorLayout.setBackgroundColor(Color.WHITE);
        mIndicatorLayout.setGravity(Gravity.CENTER_VERTICAL);
        mIndicatorLayout.setOrientation(LinearLayout.HORIZONTAL);

        mContainIndicatorAndFooterLayout.addView(mIndicatorLayout);
        addView(mContainIndicatorAndFooterLayout);

        int size = mIndicatorUnitList.size();
        for (int i = 0; i < size; i++) {
            addIndicatorUnit(context, i);
            if (i < size - 1) {
                addFilterBarLine(context);
            }
        }
    }

    private void backWhenTouchOutside() {
        if (mSelectedIndex >= 0 && mSelectedIndex < mIndicatorUnitList.size()) {
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(mSelectedIndex);
            if (indicatorUnit != null) {
                if (indicatorUnit.isCanceledOnTouchOutside()) {
                    back();
                }
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addChildView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int needHeightSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, needHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        fixUpHeight();
    }

    /**
     * 固定FilterBarLayout高度是wrap_content
     */
    private void fixUpHeight() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        if (!(layoutParams instanceof LayoutParams) && !(layoutParams instanceof FrameLayout.LayoutParams)) {
            throw new RuntimeException("FilterBarLayout needs a RelativeLayout or FrameLayout as parent layout");
        }
    }

    /**
     * 添加子控件到筛选条容器
     */
    private void addChildView() {
        int childCount = getChildCount();
        int subChildCount = mContainIndicatorAndFooterLayout.getChildCount();
        if (childCount > 1 && subChildCount == 1) {
            List<View> childViewList = new LinkedList<>();
            for (int i = 1; i < childCount; i++) {
                childViewList.add(getChildAt(i));
            }

            int size = childViewList.size();
            for (int j = 0; j < size; j++) {
                View childView = childViewList.get(j);
                removeView(childView);
                addFooterViewOnFinishInflate(j, childView, MODE_EXPAND);
            }
            childViewList.clear();
        }
    }

    /**
     * 当控件注入到xml文件时，设置把子控件当作下拉视图
     *
     * @param index
     * @param footerView
     * @param footerMode
     */
    private void addFooterViewOnFinishInflate(int index, View footerView, FooterMode footerMode) {
        if (footerView == null) {
            return;
        }

        int size = mIndicatorUnitList.size();
        if (index >= size) {
            Log.d(TAG, "The index value should be less than the count of indicator unit");
            return;
        }
        IndicatorUnit indicatorUnit = mIndicatorUnitList.get(index);
        if (indicatorUnit == null) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
        if (layoutParams == null) {
            throw new RuntimeException("The LayoutParams of this footer view is null, You need to create a layout params");
        }

        indicatorUnit.setFooterMode(footerMode);
        indicatorUnit.setFooterView(footerView);

        mContainIndicatorAndFooterLayout.addView(footerView);
    }

    /**
     * 添加分割线
     *
     * @param context
     */
    private void addFilterBarLine(Context context) {
        View lineView = new View(context);
        lineView.setBackgroundColor(DEFAULT_LINE_COLOR);
        LinearLayout.LayoutParams lineLayoutParams = new LinearLayout.LayoutParams(2, mFilterBarHeight / 3);
        lineLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        lineView.setLayoutParams(lineLayoutParams);
        mIndicatorLayout.addView(lineView);
    }

    /**
     * 添加筛选条选项
     *
     * @param context
     * @param i
     */
    private void addIndicatorUnit(Context context, int i) {
        LinearLayout indicatorUnitLayout = new LinearLayout(context);
        indicatorUnitLayout.setOrientation(LinearLayout.HORIZONTAL);
        indicatorUnitLayout.setGravity(mIndicatorGravity);
        LinearLayout.LayoutParams unitLayoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        indicatorUnitLayout.setLayoutParams(unitLayoutParams);

        final TextView titleView = new TextView(context);
        LinearLayout.LayoutParams titleViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        titleView.setLayoutParams(titleViewLayoutParams);
        titleView.setTextColor(mFilterTitleColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mFilterTitleSize);
        titleView.setSingleLine(true);
        titleView.setEllipsize(TextUtils.TruncateAt.END);

        final IndicatorUnit indicatorUnit = mIndicatorUnitList.get(i);
        if (indicatorUnit == null) {
            return;
        }

        String filterTitle = indicatorUnit.getUnitTitle();
        titleView.setText(filterTitle);
        indicatorUnit.setTvUnit(titleView);

        final ImageView imgIcon = new ImageView(context);
        LinearLayout.LayoutParams imgIconLayoutParams = new LinearLayout.LayoutParams(dip2px(8), dip2px(5));
        imgIconLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        imgIconLayoutParams.leftMargin = dip2px(5);
        imgIcon.setLayoutParams(imgIconLayoutParams);
        imgIcon.setImageDrawable(mIndicatorDrawable);
        indicatorUnit.setImgUnit(imgIcon);

        indicatorUnitLayout.addView(titleView);
        indicatorUnitLayout.addView(imgIcon);
        mIndicatorLayout.addView(indicatorUnitLayout);
        indicatorUnit.setIndicatorUnit(indicatorUnitLayout);

        final int index = i;
        indicatorUnitLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!indicatorUnit.isExpanded()) {
                    mSelectedIndex = index;
                    if (mOnFilterIndicatorClickListener != null) {
                        mOnFilterIndicatorClickListener.onClick(FilterBarLayout.this, index, false);
                    }
                } else {
                    mSelectedIndex = -1;
                    if (mOnFilterIndicatorClickListener != null) {
                        mOnFilterIndicatorClickListener.onClick(FilterBarLayout.this, index, true);
                    }
                }

                if (!indicatorUnit.isExpanded()) {
                    isShowing = true;
                    reactFilterIndicatorUIWhenOpen(indicatorUnit);
                    setupOneClickableWhenOpen(index);
                    reactToBackgroundWhenOpen(indicatorUnit);
                } else {
                    isShowing = false;
                    reactFilterIndicatorUIWhenClose(indicatorUnit);
                    recoverAllClickableWhenClose();
                    reactToBackgroundWhenClose(indicatorUnit);
                }

                if (!indicatorUnit.isExpanded()) {
                    expandFooter(indicatorUnit);
                } else {
                    collapseFooter(indicatorUnit);
                }

                indicatorUnit.setExpanded(!indicatorUnit.isExpanded());
            }
        });
    }

    private void reactFilterIndicatorUIWhenOpen(IndicatorUnit indicatorUnit) {
        if (indicatorUnit.getTvUnit() == null || indicatorUnit.getImgUnit() == null) {
            return;
        }

        indicatorUnit.getTvUnit().setTextColor(mFilterTitleSelectedColor);
        indicatorUnit.getImgUnit().setImageDrawable(mIndicatorSelectedDrawable);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backWhenTouchOutside();
            }
        });
    }

    private void reactFilterIndicatorUIWhenClose(IndicatorUnit indicatorUnit) {
        if (indicatorUnit.getTvUnit() == null || indicatorUnit.getImgUnit() == null) {
            return;
        }

        indicatorUnit.getTvUnit().setTextColor(mFilterTitleColor);
        indicatorUnit.getImgUnit().setImageDrawable(mIndicatorDrawable);

        setClickable(false);
    }

    /**
     * 伸展下拉视图
     *
     * @param indicatorUnit
     */
    private void expandFooter(final IndicatorUnit indicatorUnit) {
        View childAt = mContainIndicatorAndFooterLayout.getChildAt(0);
        if (childAt != null) {
            Object tag = childAt.getTag();
            if (tag instanceof String) {
                String s = (String) tag;
                if (BODY_LAYOUT_TAG.equals(s)) {
                    childAt.bringToFront();
                }
            }
        }

        final View expandFooter = getFooterView(indicatorUnit);
        if (expandFooter == null) {
            return;
        }

        ValueAnimator valueAnimator;

        if (indicatorUnit.getFooterMode() == MODE_EXPAND) {
            int height;
            if (expandFooter.getTag() instanceof Integer) {
                height = (int) expandFooter.getTag();
            } else {
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            valueAnimator = ValueAnimator.ofInt(ORIGIN_HEIGHT, height);
        } else {
            int height = expandFooter.getHeight();
            valueAnimator = ValueAnimator.ofInt(-height, mFilterBarHeight);
        }

        valueAnimator.setDuration(OPEN_FOOTER_ANIMATION_DURATION);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = expandFooter.getLayoutParams();
                if (layoutParams != null && layoutParams instanceof LayoutParams) {
                    LayoutParams childParams = (LayoutParams) layoutParams;
                    if (indicatorUnit.getFooterMode() == MODE_EXPAND) {
                        childParams.height = (int) valueAnimator.getAnimatedValue();
                    } else {
                        childParams.topMargin = (int) valueAnimator.getAnimatedValue();
                    }
                    expandFooter.setLayoutParams(childParams);
                }
            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1);
        alphaAnimation.setDuration(OPEN_FOOTER_ANIMATION_DURATION);

        valueAnimator.start();
        expandFooter.startAnimation(alphaAnimation);
    }

    /**
     * 折叠下拉视图
     *
     * @param indicatorUnit
     */
    private void collapseFooter(final IndicatorUnit indicatorUnit) {
        final View expandFooter = getFooterView(indicatorUnit);
        if (expandFooter == null) {
            return;
        }

        ValueAnimator valueAnimator;

        if (indicatorUnit.getFooterMode() == MODE_EXPAND) {
            int height;
            if (expandFooter.getTag() instanceof Integer) {
                height = (int) expandFooter.getTag();
            } else {
                height = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            valueAnimator = ValueAnimator.ofInt(height, ORIGIN_HEIGHT);
        } else {
            int height = expandFooter.getHeight();
            valueAnimator = ValueAnimator.ofInt(mFilterBarHeight, -height);
        }

        valueAnimator.setDuration(CLOSE_FOOTER_ANIMATION_DURATION);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ViewGroup.LayoutParams layoutParams = expandFooter.getLayoutParams();
                if (layoutParams != null && layoutParams instanceof LayoutParams) {
                    LayoutParams childParams = (LayoutParams) layoutParams;

                    if (indicatorUnit.getFooterMode() == MODE_EXPAND) {
                        childParams.height = (int) valueAnimator.getAnimatedValue();
                    } else {
                        childParams.topMargin = (int) valueAnimator.getAnimatedValue();
                    }
                    expandFooter.setLayoutParams(childParams);
                }
            }
        });

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.3f);
        alphaAnimation.setDuration(CLOSE_FOOTER_ANIMATION_DURATION);

        valueAnimator.start();
        expandFooter.startAnimation(alphaAnimation);

    }

    /**
     * 获取下拉视图
     *
     * @param indicatorUnit
     * @return
     */
    private View getFooterView(IndicatorUnit indicatorUnit) {
        return indicatorUnit.getFooterView();
    }

    /**
     * 设置一个筛选条可点击
     *
     * @param selectedIndex 可以点击的筛选序号
     */
    private void setupOneClickableWhenOpen(int selectedIndex) {
        int size = mIndicatorUnitList.size();
        for (int i = 0; i < size; i++) {
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(i);
            if (indicatorUnit != null) {
                if (i == selectedIndex) {
                    indicatorUnit.getIndicatorUnit().setClickable(true);
                } else {
                    indicatorUnit.getIndicatorUnit().setClickable(false);
                }
            }
        }
    }

    /**
     * 打开时背景暗色
     */
    private void reactToBackgroundWhenOpen(IndicatorUnit indicatorUnit) {
        if (indicatorUnit.isScreenDimAvailable()) {
            setBackgroundColor(mFilterCoverColor);
            ViewGroup.LayoutParams lp = getLayoutParams();
            if (lp instanceof LayoutParams) {
                mOriginRootLayoutParams = (LayoutParams) lp;
                LayoutParams containerLayoutParams = new LayoutParams(mOriginRootLayoutParams.width, mOriginContainIndicatorAndFooterLayoutParams.height);
                containerLayoutParams.leftMargin = mOriginRootLayoutParams.leftMargin;
                containerLayoutParams.topMargin = mOriginRootLayoutParams.topMargin;
                containerLayoutParams.rightMargin = mOriginRootLayoutParams.rightMargin;
                containerLayoutParams.bottomMargin = mOriginRootLayoutParams.bottomMargin;
                mContainIndicatorAndFooterLayout.setLayoutParams(containerLayoutParams);

                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                layoutParams.leftMargin = 0;
                layoutParams.topMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.bottomMargin = 0;
                setLayoutParams(layoutParams);
            } else {
                Log.e(TAG, "The parent layout of FilterBarLayout should be RelativeLayout");
            }
        }
    }

    /**
     * 恢复所有的筛选条可点击
     */
    private void recoverAllClickableWhenClose() {
        int size = mIndicatorUnitList.size();
        for (int i = 0; i < size; i++) {
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(i);
            if (indicatorUnit != null && indicatorUnit.getIndicatorUnit() != null) {
                indicatorUnit.getIndicatorUnit().setClickable(true);
            }
        }
    }

    /**
     * 关闭时，恢复背景
     */
    private void reactToBackgroundWhenClose(IndicatorUnit indicatorUnit) {
        if (indicatorUnit.isScreenDimAvailable() && mOriginRootLayoutParams != null) {
            setLayoutParams(mOriginRootLayoutParams);
            mContainIndicatorAndFooterLayout.setLayoutParams(mOriginContainIndicatorAndFooterLayoutParams);
            setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        }
    }

    private int dip2px(float dipValue) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void showIndicator(IndicatorUnit indicatorUnit) {
        int index = mIndicatorUnitList.indexOf(indicatorUnit);
        reactFilterIndicatorUIWhenOpen(indicatorUnit);
        setupOneClickableWhenOpen(index);
        indicatorUnit.setExpanded(!indicatorUnit.isExpanded());
    }

    private void closeIndicator(IndicatorUnit indicatorUnit) {
        if (indicatorUnit == null) {
            return;
        }
        recoverAllClickableWhenClose();
        reactFilterIndicatorUIWhenClose(indicatorUnit);
        indicatorUnit.setExpanded(!indicatorUnit.isExpanded());
    }


    public enum FooterMode {
        /**
         * 竖直移动下拉视图
         */
        MODE_TRANSLATE,
        /**
         * 竖直膨胀显示下拉视图
         */
        MODE_EXPAND
    }

    public interface OnFilterIndicatorClickListener {

        void onClick(FilterBarLayout filterBarLayout, int index, boolean isFooterViewShowing);
    }


    /**
     * 添加筛选条元素标题
     *
     * @param unitTitle
     */
    public void addFilterTitle(String unitTitle) {
        if (unitTitle == null) {
            return;
        }

        int size = mIndicatorUnitList.size();
        if (size > 0) {
            addFilterBarLine(getContext());
        }

        IndicatorUnit indicatorUnit = new IndicatorUnit(unitTitle, false);
        mIndicatorUnitList.add(indicatorUnit);
        addIndicatorUnit(getContext(), size);
    }

    /**
     * 监听筛选项的点击事件
     *
     * @param onFilterIndicatorClickListener
     */
    public void setOnFilterIndicatorClickListener(OnFilterIndicatorClickListener onFilterIndicatorClickListener) {
        if (onFilterIndicatorClickListener == null) {
            return;
        }
        mOnFilterIndicatorClickListener = onFilterIndicatorClickListener;
    }

    /**
     * 添加下拉视图
     *
     * @param index 序号
     * @param view  下拉视图
     */
    public void addFooterView(int index, View view) {
        addFooterView(index, view, MODE_EXPAND);
    }

    /**
     * 添加下拉视图
     *
     * @param index      序号
     * @param footerView 下拉视图
     * @param footerMode 视图展开方式
     */
    public void addFooterView(int index, View footerView, FooterMode footerMode) {
        if (footerView == null) {
            return;
        }

        checkAndRemoveDuplicateFooterView(footerView);

        int size = mIndicatorUnitList.size();
        if (index >= size) {
            Log.d(TAG, "The index value should be less than the count of indicator unit");
            return;
        }
        IndicatorUnit indicatorUnit = mIndicatorUnitList.get(index);
        if (indicatorUnit == null) {
            return;
        }

        indicatorUnit.setFooterView(footerView);

        ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
        if (layoutParams == null) {
            throw new RuntimeException("The LayoutParams of this footer view is null, You need to create a layout params");
        }
        LayoutParams lp = new LayoutParams(layoutParams);
        if (lp.height == LayoutParams.WRAP_CONTENT
                || lp.height == LayoutParams.MATCH_PARENT) {
            Log.d(TAG, "If the height of layout param equals to match_parent or wrap_content, the foot mode value has to be set MODE_ MODE_EXPAND");
            footerMode = MODE_EXPAND;
        }
        indicatorUnit.setFooterMode(footerMode);

        if (footerMode != null && footerMode == MODE_EXPAND) {
            footerView.setTag(lp.height);
            lp.height = ORIGIN_HEIGHT;
            lp.topMargin = mFilterBarHeight;
        } else {
            lp.topMargin = -lp.height;
        }

        footerView.setLayoutParams(lp);

        boolean hasAddition = isFooterViewAddition(footerView);
        if (!hasAddition) {
            mContainIndicatorAndFooterLayout.addView(footerView);
        }
    }

    private boolean isFooterViewAddition(View footerView) {
        int childCount = mContainIndicatorAndFooterLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = mContainIndicatorAndFooterLayout.getChildAt(i);
            if (childAt == footerView) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查该下拉视图是否在控件填充布局的时候添加过
     *
     * @param footerView
     * @return
     */
    private void checkAndRemoveDuplicateFooterView(View footerView) {
        int size = mIndicatorUnitList.size();
        for (int i = 0; i < size; i++) {
            IndicatorUnit unit = mIndicatorUnitList.get(i);
            if (footerView == unit.getFooterView()) {
                unit.setFooterView(null);
                unit.setFooterMode(MODE_EXPAND);
                break;
            }
        }
    }

    /**
     * 设置点击筛选条，是否屏幕变暗
     *
     * @param index
     * @param isAvailable
     */
    public void setScreenDimAvailable(int index, boolean isAvailable) {
        IndicatorUnit indicatorUnit = mIndicatorUnitList.get(index);
        if (indicatorUnit == null) {
            return;
        }
        indicatorUnit.setScreenDimAvailable(isAvailable);
    }

    public void back() {
        if (isShowing() && mSelectedIndex >= 0) {
            back(mSelectedIndex, null);
        }
    }

    /**
     * 还原筛选条
     *
     * @param index 筛选序号
     */
    public void back(int index) {
        back(index, null);
    }

    /**
     * 还原筛选条
     *
     * @param index 序号
     * @param title 筛选条选中的内容
     */
    public void back(int index, String title) {
        IndicatorUnit indicatorUnit = mIndicatorUnitList.get(index);
        if (indicatorUnit == null) {
            return;
        }
        collapseFooter(indicatorUnit);
        closeIndicator(indicatorUnit);
        reactToBackgroundWhenClose(indicatorUnit);
        isShowing = false;

        TextView tvFilterTitle = indicatorUnit.getTvUnit();
        if (tvFilterTitle != null && title != null) {
            tvFilterTitle.setText(title);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }


    public void setCanceledOnTouchOutside(boolean enable) {
        for (IndicatorUnit unit : mIndicatorUnitList) {
            unit.setCanceledOnTouchOutside(enable);
        }
    }

    public void setCanceledOnTouchOutside(int index, boolean enable) {
        if (index >= 0 && index < mIndicatorUnitList.size()) {
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(index);
            indicatorUnit.setCanceledOnTouchOutside(enable);
        }
    }

    public void setIndicatorResource(@DrawableRes int resId){
        mIndicatorDrawable = getResources().getDrawable(resId);
    }

    public void setIndicatorSelectedResource(@DrawableRes int resId){
        mIndicatorSelectedDrawable = getResources().getDrawable(resId);
    }

    public void setIndicatorDrawable(Drawable indicatorDrawable) {
        mIndicatorDrawable = indicatorDrawable;
    }

    public void setIndicatorSelectedDrawable(Drawable indicatorSelectedDrawable) {
        mIndicatorSelectedDrawable = indicatorSelectedDrawable;
    }

    public String getFirstFilterTitle() {
        return mFirstFilterTitle;
    }

    public void setFirstFilterTitle(String firstFilterTitle) {
        if(mIndicatorUnitList == null){
            return;
        }
        int size = mIndicatorUnitList.size();
        if(0 < size){
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(0);
            if(indicatorUnit.getTvUnit() != null){
                indicatorUnit.getTvUnit().setText(firstFilterTitle);
            }
            mFirstFilterTitle = firstFilterTitle;
        }
    }

    public String getSecondFilterTitle() {
        return mSecondFilterTitle;
    }

    public void setSecondFilterTitle(String secondFilterTitle) {
        if(mIndicatorUnitList == null){
            return;
        }
        int size = mIndicatorUnitList.size();
        if(1 < size){
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(1);
            if(indicatorUnit.getTvUnit() != null){
                indicatorUnit.getTvUnit().setText(secondFilterTitle);
            }
            mSecondFilterTitle = secondFilterTitle;
        }
    }

    public String getThirdFilterTitle() {
        return mThirdFilterTitle;
    }

    public void setThirdFilterTitle(String thirdFilterTitle) {
        if(mIndicatorUnitList == null){
            return;
        }
        int size = mIndicatorUnitList.size();
        if(2 < size){
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(2);
            if(indicatorUnit.getTvUnit() != null){
                indicatorUnit.getTvUnit().setText(thirdFilterTitle);
            }
            mThirdFilterTitle = thirdFilterTitle;
        }
    }

    public String getFourthFilterTitle() {
        return mFourthFilterTitle;
    }

    public void setFourthFilterTitle(String fourthFilterTitle) {
        if(mIndicatorUnitList == null){
            return;
        }
        int size = mIndicatorUnitList.size();
        if(3 < size){
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(3);
            if(indicatorUnit.getTvUnit() != null){
                indicatorUnit.getTvUnit().setText(fourthFilterTitle);
            }
            mFourthFilterTitle = fourthFilterTitle;
        }
    }

    public String getFifthFilterTitle() {
        return mFifthFilterTitle;
    }

    public void setFifthFilterTitle(String fifthFilterTitle) {
        if(mIndicatorUnitList == null){
            return;
        }
        int size = mIndicatorUnitList.size();
        if(4 < size){
            IndicatorUnit indicatorUnit = mIndicatorUnitList.get(4);
            if(indicatorUnit.getTvUnit() != null){
                indicatorUnit.getTvUnit().setText(fifthFilterTitle);
            }
            mFifthFilterTitle = fifthFilterTitle;
        }
    }

    public int getIndicatorGravity() {
        return mIndicatorGravity;
    }

    public void setIndicatorGravity(int indicatorGravity) {
        mIndicatorGravity = indicatorGravity;
        requestLayout();
    }
}
