package com.sugarya.closet.ui.home.fragment;


import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sugarya.closet.Data.DataLayer;
import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.HomeCategoryEntity;
import com.sugarya.closet.model.entity.MasterCategoryEntity;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.common.basic.BasicFragment;
import com.sugarya.closet.ui.home.adapter.ClosetCategoryProductAdapter;
import com.sugarya.closet.ui.home.event.RemoveItemEvent;
import com.sugarya.closet.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.functions.Action1;

/**
 * Created by Ethan 2017/09/19
 * 主页
 */
public class ClosetFragment extends BasicFragment {

    private static final String TAG = "ClosetFragment";

    private static final String[] mCategoryNameArrays = {"T恤衫", "上衣", "大衣", "连衣裙", "裤子", "短裤", "鞋子"};
    private static final int[] mCategoryIconArrays = {R.drawable.ic_shirt_00, R.drawable.ic_coat_00, R.drawable.ic_overcoat_00, R.drawable.ic_dress_00, R.drawable.ic_trousers_00, R.drawable.ic_shorts_00, R.drawable.ic_shoe_00};
    private static final int[] mCategoryIconSelectedArrays = {R.drawable.ic_shirt_01, R.drawable.ic_coat_01, R.drawable.ic_overcoat_01, R.drawable.ic_dress_01, R.drawable.ic_trousers_01, R.drawable.ic_shorts_00, R.drawable.ic_shoe_01};


    @BindView(R.id.tv_home_category_indicator)
    TextView mTvCategoryIndicator;

    @BindView(R.id.container_home_category)
    LinearLayout mContainerCategory;

    @BindView(R.id.home_body)
    LinearLayout mFrameBody;

    @BindView(R.id.recycler_closet)
    RecyclerView mRecyclerView;


    //arguments
    private List<TextView> mTvCategoryNameList = new ArrayList<>();
    private List<ImageView> mImgCategoryIconList = new ArrayList<>();
    private List<LinearLayout> mUnitCategoryLayoutList = new ArrayList<>();
    private List<HomeCategoryEntity> mHomeCategoryEntityList = DataLayer.getInstance().getDataPool().getHomeCategoryEntityList();
    private static SparseArray<List<ProductEntity>> mProductSparseArray = new SparseArray<>();
    private int mLastIndex = -1;

    static {
        List<ProductEntity> productEntityList0 = new ArrayList<>();
        productEntityList0.add(new ProductEntity(R.drawable.ic_closet_shirt_1));
        productEntityList0.add(new ProductEntity(R.drawable.ic_closet_shirt_2));
        productEntityList0.add(new ProductEntity(R.drawable.ic_closet_shirt_3));
        mProductSparseArray.put(0, productEntityList0);

        List<ProductEntity> productEntityList1 = new ArrayList<>();
        productEntityList1.add(new ProductEntity(R.drawable.ic_closet_coat_01));
        productEntityList1.add(new ProductEntity(R.drawable.ic_closet_coat_02));
        mProductSparseArray.put(1, productEntityList1);

        List<ProductEntity> productEntityList3 = new ArrayList<>();
        productEntityList3.add(new ProductEntity(R.drawable.ic_closet_skirt_1));
        productEntityList3.add(new ProductEntity(R.drawable.ic_closet_skirt_2));
        productEntityList3.add(new ProductEntity(R.drawable.ic_closet_skirt_3));
        productEntityList3.add(new ProductEntity(R.drawable.ic_closet_skirt_4));
        mProductSparseArray.put(3, productEntityList3);

        List<ProductEntity> productEntityList4 = new ArrayList<>();
        productEntityList4.add(new ProductEntity(R.drawable.ic_closet_changku_1));
        productEntityList4.add(new ProductEntity(R.drawable.ic_closet_changku_2));
        productEntityList4.add(new ProductEntity(R.drawable.ic_closet_changku_3));
        productEntityList4.add(new ProductEntity(R.drawable.ic_closet_changku_4));
        mProductSparseArray.put(4, productEntityList4);

        List<ProductEntity> productEntityList5 = new ArrayList<>();
        productEntityList5.add(new ProductEntity(R.drawable.ic_closet_shorts_5));
        productEntityList5.add(new ProductEntity(R.drawable.ic_closet_shorts_2));
        productEntityList5.add(new ProductEntity(R.drawable.ic_closet_shorts_3));
        productEntityList5.add(new ProductEntity(R.drawable.ic_closet_shorts_1));
        productEntityList5.add(new ProductEntity(R.drawable.ic_closet_shorts_4));
        mProductSparseArray.put(5, productEntityList5);

    }

    private ClosetCategoryProductAdapter mCategoryProductAdapter;

    public ClosetFragment() {
        // Required empty public constructor
    }

    public static ClosetFragment newInstance() {
        ClosetFragment fragment = new ClosetFragment();
        return fragment;
    }

    public static ClosetFragment newInstance(Bundle args) {
        ClosetFragment fragment = new ClosetFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initRxBus(){
        addSubscription(RemoveItemEvent.class, new Action1<RemoveItemEvent>() {
            @Override
            public void call(RemoveItemEvent removeItemEvent) {
                ProductEntity removeEntity = removeItemEvent.getRemoveEntity();
                if(DataLayer.getInstance().getDataPool().getSelectedProductEntityList().contains(removeEntity)){
                    DataLayer.getInstance().getDataPool().getSelectedProductEntityList().remove(removeEntity);
                }
                mCategoryProductAdapter.updateData(removeEntity);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRxBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initializeShow();
        Log.d(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchData();
        Log.d(TAG, "onViewCreated");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {

        }
    }

    private void initializeShow() {
        initRecyclerView();
    }

    private void fetchData() {
        int length = mCategoryNameArrays.length;
        if(mHomeCategoryEntityList == null || mHomeCategoryEntityList.size() <= 0) {
            for (int i = 0; i < length; i++) {
                mHomeCategoryEntityList.add(new HomeCategoryEntity(
                        new MasterCategoryEntity(mCategoryNameArrays[i], mCategoryIconArrays[i], mCategoryIconSelectedArrays[i]),
                        mProductSparseArray.get(i)));
            }
        }
        initializeCategory(mHomeCategoryEntityList);
    }

    private void initializeCategory(List<HomeCategoryEntity> homeCategoryEntityList) {
        if (homeCategoryEntityList == null) {
            return;
        }

        int size = homeCategoryEntityList.size();
        for (int i = 0; i < size; i++) {
            HomeCategoryEntity homeCategoryEntity = homeCategoryEntityList.get(i);
            setupUnitCategory(homeCategoryEntity, i);
        }

        initCategoryStatus();
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mCategoryProductAdapter = new ClosetCategoryProductAdapter();
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(mCategoryProductAdapter);
    }

    /**
     * 初始化分类导航状态
     */
    private void initCategoryStatus() {
        mUnitCategoryLayoutList.get(0).setBackgroundResource(R.color.bg_selected_category);
        mTvCategoryNameList.get(0).setVisibility(View.VISIBLE);
        mImgCategoryIconList.get(0).setImageResource(mCategoryIconSelectedArrays[0]);

        List<ProductEntity> productEntityList = mProductSparseArray.get(0);
        if(productEntityList != null) {
            mCategoryProductAdapter.updateData(productEntityList);
        }
    }

    private void setupUnitCategory(HomeCategoryEntity homeCategoryEntity, final int index) {
        MasterCategoryEntity categoryEntity = homeCategoryEntity.getCategoryEntity();
        String cateName = categoryEntity.getCateName();
        final int picResId = categoryEntity.getPicResId();
        final List<ProductEntity> productEntityList = homeCategoryEntity.getProductEntityList();

        LinearLayout unitContainer = new LinearLayout(getContext());
        unitContainer.setOrientation(LinearLayout.VERTICAL);
        unitContainer.setGravity(Gravity.CENTER_VERTICAL);
        unitContainer.setBackgroundResource(R.color.bg_default_category);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getResources().getInteger(R.integer.category_unit_height), getContext()));
        layoutParams.gravity = Gravity.CENTER;
        unitContainer.setLayoutParams(layoutParams);

        final ImageView imgIcon = new ImageView(getAttachActivity());
        int length = DisplayUtil.dip2px(32, getAttachActivity());
        LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(length, length);
        iconLp.gravity = Gravity.CENTER_HORIZONTAL;
        imgIcon.setLayoutParams(iconLp);
        imgIcon.setImageResource(picResId);


        final TextView tvCategory = new TextView(getContext());
        LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.gravity = Gravity.CENTER_HORIZONTAL;
        tvCategory.setLayoutParams(tvLp);
        tvCategory.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        tvCategory.setTextColor(getAttachActivity().getResources().getColor(R.color.font_red));
        tvCategory.setText(cateName);
        tvCategory.setVisibility(View.GONE);

        View lineView = new View(getContext());
        LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        lineView.setLayoutParams(lineLp);
        lineView.setBackgroundResource(R.color.line);

        mUnitCategoryLayoutList.add(unitContainer);
        mTvCategoryNameList.add(tvCategory);
        mImgCategoryIconList.add(imgIcon);

        unitContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactionToClickCategory(index, productEntityList);
            }
        });

        unitContainer.addView(imgIcon);
        unitContainer.addView(tvCategory);
        mContainerCategory.addView(unitContainer);
        mContainerCategory.addView(lineView);
    }

    private void reactionToClickCategory(int index, List<ProductEntity> productEntityList) {
        startIndicatorAnimator(index, mLastIndex);
        for (int i = 0; i < mCategoryNameArrays.length; i++) {
            if (i == index) {
                mUnitCategoryLayoutList.get(i).setBackgroundResource(R.color.bg_selected_category);
                mTvCategoryNameList.get(i).setVisibility(View.VISIBLE);
                mImgCategoryIconList.get(i).setImageResource(mCategoryIconSelectedArrays[i]);
            } else {
                mUnitCategoryLayoutList.get(i).setBackgroundResource(R.color.bg_default_category);
                mTvCategoryNameList.get(i).setVisibility(View.GONE);
                mImgCategoryIconList.get(i).setImageResource(mCategoryIconArrays[i]);
            }
        }

        mCategoryProductAdapter.updateData(productEntityList);
        mLastIndex = index;
    }

    private void startIndicatorAnimator(int currentIndex, int lastIndex) {
        if (lastIndex == -1) {
            lastIndex = 0;
        }
        int unit = DisplayUtil.dip2px(getResources().getInteger(R.integer.category_unit_height), getContext());
        float startValue = lastIndex * unit;
        float endValue = currentIndex * unit;
        int count = Math.abs(currentIndex - lastIndex);

        ObjectAnimator animator = ObjectAnimator.ofFloat(mTvCategoryIndicator, "Y", startValue, endValue);
        animator.setInterpolator(new OvershootInterpolator(0.15f * count));
        animator.setDuration(260);
        animator.start();
    }

    public void addCameraUri(List<Uri> uriList){
        if(uriList == null){
            return;
        }

        ProductEntity productEntity = new ProductEntity(false, uriList.get(0));
        List<ProductEntity> productEntityList = DataLayer.getInstance().getDataPool().getHomeCategoryEntityList().get(1).getProductEntityList();
        productEntityList.add(productEntity);
        reactionToClickCategory(1, productEntityList);

        mCategoryProductAdapter.addProductEntity(productEntity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home, menu);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_closet;
    }
}
