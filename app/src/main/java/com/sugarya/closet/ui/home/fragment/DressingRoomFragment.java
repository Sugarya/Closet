package com.sugarya.closet.ui.home.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sugarya.closet.Data.DataLayer;
import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.HomeCategoryEntity;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.common.basic.BasicFragment;
import com.sugarya.closet.ui.home.adapter.DressingRoomMatchAdapter;
import com.sugarya.closet.ui.home.event.RemoveItemEvent;
import com.sugarya.closet.ui.home.event.SelectedDressingRoomMatchItemEvent;
import com.sugarya.closet.ui.widgets.filterbar.FilterBarLayout;
import com.sugarya.closet.ui.widgets.materialpager.CardPagerAdapter;
import com.sugarya.closet.ui.widgets.materialpager.ShadowTransformer;
import com.sugarya.closet.utils.HintUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Ethan 2017/09/23
 * 试衣间
 */
public class DressingRoomFragment extends BasicFragment {

    private static final String TAG = "DressingRoomFragment";
    private static final int TYPE_NATIVE_ALL= 0;
    private static final int TYPE_NATIVE_CLOSET = 1;
    private static final int TYPE_JD_CLOTHES = 2;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @BindView(R.id.filter_bar)
    FilterBarLayout mFilterBarLayout;

    @BindView(R.id.filter_footer_dressing)
    LinearLayout mFilterFooter;

    @BindView(R.id.container_footer_native_closet)
    RelativeLayout mContainerNativeCloset;

    @BindView(R.id.footer_native_closet)
    ImageView mImgNativeCloset;

    @BindView(R.id.container_footer_jd_clothes)
    RelativeLayout mContainerJDClothes;

    @BindView(R.id.footer_jd_clothes)
    ImageView mImgJDClothes;

    @BindView(R.id.tv_footer_native_closet)
    TextView mTvNativeCloset;

    @BindView(R.id.tv_footer_jd_clothes)
    TextView mTvJDClothes;

    @BindView(R.id.container_footer_all)
    RelativeLayout mContainerFooterAll;

    @BindView(R.id.footer_all)
    ImageView mImgAll;

    @BindView(R.id.tv_footer_all)
    TextView mTvFooterAll;

    @BindView(R.id.swipe_dressing_room)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_dressing_room)
    RecyclerView mRecyclerView;


    //arguments
    private List<ProductEntity> mSelectedProductEntityList = DataLayer.getInstance().getDataPool().getSelectedProductEntityList();
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private ProductEntity mProductEntity;
    private DressingRoomMatchAdapter mDressingRoomMatchAdapter;
    private int mFilterOptionType = TYPE_NATIVE_ALL;
    public DressingRoomFragment() {
        // Required empty public constructor
    }

    public static DressingRoomFragment newInstance() {
        DressingRoomFragment fragment = new DressingRoomFragment();
        return fragment;
    }

    private void onRxBus() {
        addSubscription(SelectedDressingRoomMatchItemEvent.class, new Action1<SelectedDressingRoomMatchItemEvent>() {
            @Override
            public void call(SelectedDressingRoomMatchItemEvent selectedDressingRoomMatchItemEvent) {
                mProductEntity = selectedDressingRoomMatchItemEvent.getProductEntity();
                reactionToFilterFooterClick();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        addSubscription(RemoveItemEvent.class, new Action1<RemoveItemEvent>() {
            @Override
            public void call(RemoveItemEvent removeItemEvent) {
                ProductEntity removeEntity = removeItemEvent.getRemoveEntity();
                if(mSelectedProductEntityList.contains(removeEntity)){
                    mSelectedProductEntityList.remove(removeEntity);
                }
                if(mSelectedProductEntityList.size() == 0){
                    mDressingRoomMatchAdapter.notifyData(null);
                }

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
        onRxBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView: ");
        initializeShow();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onCreateView: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @OnClick(R.id.container_footer_all)
    void onOptionAllClick(){
        mFilterOptionType = TYPE_NATIVE_ALL;
        mImgJDClothes.setVisibility(View.INVISIBLE);
        mImgNativeCloset.setVisibility(View.INVISIBLE);
        mImgAll.setVisibility(View.VISIBLE);

        String selectedOption = mTvFooterAll.getText().toString();
        mFilterBarLayout.back(0, selectedOption);
        reactionToFilterFooterClick();
    }

    private void reactionToFilterFooterClick() {
        Uri uri = mProductEntity.getUri();
        if(uri != null){
            fetchMatchWhiteCoatProductInfo(mProductEntity);
        }else {
            fetchMatchProductInfo(mProductEntity);
        }
    }

    @OnClick(R.id.container_footer_native_closet)
    void onNativeCloseClick() {
        mFilterOptionType = TYPE_NATIVE_CLOSET;
        mImgNativeCloset.setVisibility(View.VISIBLE);
        mImgJDClothes.setVisibility(View.INVISIBLE);
        mImgAll.setVisibility(View.INVISIBLE);

        String selectedOption = mTvNativeCloset.getText().toString();
        mFilterBarLayout.back(0, selectedOption);

        reactionToFilterFooterClick();
    }

    @OnClick(R.id.container_footer_jd_clothes)
    void onJDClothesClick() {
        mFilterOptionType = TYPE_JD_CLOTHES;
        mImgJDClothes.setVisibility(View.VISIBLE);
        mImgNativeCloset.setVisibility(View.INVISIBLE);
        mImgAll.setVisibility(View.INVISIBLE);

        String selectedOption = mTvJDClothes.getText().toString();
        mFilterBarLayout.back(0, selectedOption);

        reactionToFilterFooterClick();
    }


    private void initializeShow() {
        initTopCardsView();
        initFilterBar();
        initSwipeRefreshLayout();
        initBottomRecyclerView();
    }

    private void initFilterBar() {
        mFilterBarLayout.addFooterView(0, mFilterFooter, FilterBarLayout.FooterMode.MODE_EXPAND);
        mFilterBarLayout.setOnFilterIndicatorClickListener(new FilterBarLayout.OnFilterIndicatorClickListener() {
            @Override
            public void onClick(FilterBarLayout filterBarLayout, int index, boolean isFooterViewShowing) {

            }
        });
        mFilterBarLayout.setCanceledOnTouchOutside(true);

        setupFilterBarVisibility();
    }

    private void initTopCardsView() {
        mCardAdapter = new CardPagerAdapter();
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(4);
        mCardShadowTransformer.enableScaling(true);
    }

    private void initBottomRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mDressingRoomMatchAdapter = new DressingRoomMatchAdapter();
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mRecyclerView.setAdapter(mDressingRoomMatchAdapter);

    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange), getResources().getColor(R.color.yellow));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMatchProductInfo(mProductEntity);
            }
        });
    }

    private void fetchMatchProductInfo(ProductEntity productEntity) {
        if (productEntity == null) {
            return;
        }
        final int resId = productEntity.getResId();

        Observable.just("")
                .map(new Func1<String, List<ProductEntity>>() {
                    @Override
                    public List<ProductEntity> call(String s) {
                        List<ProductEntity> productEntityList = new ArrayList<>();
                        if(mFilterOptionType == TYPE_NATIVE_CLOSET){
                            productEntityList.add(new ProductEntity("https://", R.drawable.ic_closet_coat_02));
                        }else if(mFilterOptionType == TYPE_JD_CLOTHES){
                            productEntityList.add(new ProductEntity("https://item.jd.com/11411658130.html", R.drawable.ic_black_shorts_matcher_1));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12061110941.html", R.drawable.ic_black_shorts_matcher_3));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12141251057.html", R.drawable.ic_black_shorts_matcher_4));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13440473066.html", R.drawable.ic_black_shorts_matcher_5));
                            productEntityList.add(new ProductEntity("https://item.jd.com/15998812455.html", R.drawable.ic_black_shorts_matcher_6));
                            productEntityList.add(new ProductEntity("https://item.jd.com/16366311397.html", R.drawable.ic_black_shorts_matcher_7));
                        }else {
                            productEntityList.add(new ProductEntity("https://item.jd.com/11411658130.html", R.drawable.ic_black_shorts_matcher_1));
//                            productEntityList.add(new ProductEntity("https://item.jd.com/11553821705.html", R.drawable.ic_black_shorts_matcher_2));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12061110941.html", R.drawable.ic_black_shorts_matcher_3));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12141251057.html", R.drawable.ic_black_shorts_matcher_4));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13440473066.html", R.drawable.ic_black_shorts_matcher_5));
                            productEntityList.add(new ProductEntity("https://item.jd.com/15998812455.html", R.drawable.ic_black_shorts_matcher_6));
                            productEntityList.add(new ProductEntity("https://item.jd.com/16366311397.html", R.drawable.ic_black_shorts_matcher_7));
                            productEntityList.add(new ProductEntity("https://", R.drawable.ic_closet_coat_02));
                        }
                        return productEntityList;
                    }
                })
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mSwipeRefreshLayout != null) {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                })

                .subscribe(new Action1<List<ProductEntity>>() {
                    @Override
                    public void call(List<ProductEntity> productEntities) {
                        if(resId == R.drawable.ic_closet_shorts_5){
                            mDressingRoomMatchAdapter.notifyData(productEntities);
                        }else{
                            HintUtils.showShortToast(getContext(), "请稍后再试..");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void fetchMatchWhiteCoatProductInfo(ProductEntity productEntity) {
        if (productEntity == null) {
            return;
        }
        final int resId = productEntity.getResId();

        Observable.just("")
                .map(new Func1<String, List<ProductEntity>>() {
                    @Override
                    public List<ProductEntity> call(String s) {
                        List<ProductEntity> productEntityList = new ArrayList<>();
                        if(mFilterOptionType == TYPE_NATIVE_CLOSET){
                            productEntityList.add(new ProductEntity("https://", R.drawable.ic_black_shorts_matcher_2));
//                            productEntityList.add(new ProductEntity("https://item.jd.com/11553821705.html", R.drawable.ic_black_shorts_matcher_2));
                        }else if(mFilterOptionType == TYPE_JD_CLOTHES){
                            productEntityList.add(new ProductEntity("https://item.jd.com/11301333968.html", R.drawable.ic_white_coat_matcher_1));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12370076114.html", R.drawable.ic_white_coat_matcher_2));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12760792745.html", R.drawable.ic_white_coat_matcher_3));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13513628839.html", R.drawable.ic_white_coat_matcher_4));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13976732799.html", R.drawable.ic_white_coat_matcher_5));
                            productEntityList.add(new ProductEntity("https://item.jd.com/16250673301.html", R.drawable.ic_white_coat_matcher_6));
                        }else {
                            productEntityList.add(new ProductEntity("https://item.jd.com/11301333968.html", R.drawable.ic_white_coat_matcher_1));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12370076114.html", R.drawable.ic_white_coat_matcher_2));
                            productEntityList.add(new ProductEntity("https://item.jd.com/12760792745.html", R.drawable.ic_white_coat_matcher_3));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13513628839.html", R.drawable.ic_white_coat_matcher_4));
                            productEntityList.add(new ProductEntity("https://item.jd.com/13976732799.html", R.drawable.ic_white_coat_matcher_5));
                            productEntityList.add(new ProductEntity("https://item.jd.com/16250673301.html", R.drawable.ic_white_coat_matcher_6));
                            productEntityList.add(new ProductEntity("https://", R.drawable.ic_black_shorts_matcher_2));
                        }
                        return productEntityList;
                    }
                })
                .delay(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (mSwipeRefreshLayout != null) {
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mSwipeRefreshLayout.setRefreshing(true);
                        }
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                })

                .subscribe(new Action1<List<ProductEntity>>() {
                    @Override
                    public void call(List<ProductEntity> productEntities) {
                        mDressingRoomMatchAdapter.notifyData(productEntities);
//                        if(resId == R.drawable.ic_closet_shorts_5){
//                        }else{
//                            HintUtils.showShortSnackBar(mRecyclerView, "请稍后再试..");
//                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public void addCameraUri(List<Uri> uriList){
        if(uriList == null){
            return;
        }
        Uri uri = uriList.get(0);
        ProductEntity entity = new ProductEntity(true, uri);
        mCardAdapter.addCardItem(entity);
        mSelectedProductEntityList.add(entity);
        setupFilterBarVisibility();

        HomeCategoryEntity homeCategoryEntity = DataLayer.getInstance().getDataPool().getHomeCategoryEntityList().get(1);
        homeCategoryEntity.getProductEntityList().add(entity);
    }

    public void notifyProductData(ProductEntity entity) {
        if (entity == null) {
            return;
        }
        if (mSelectedProductEntityList.contains(entity)) {
            mSelectedProductEntityList.remove(entity);
            mCardAdapter.removeCardItem(entity);
        } else {
            mSelectedProductEntityList.add(entity);
            mCardAdapter.addCardItem(entity);
        }

        setupFilterBarVisibility();
    }

    private void setupFilterBarVisibility() {
        if(mSelectedProductEntityList == null || mSelectedProductEntityList.isEmpty()){
            mFilterBarLayout.setVisibility(View.GONE);
        }else{
            mFilterBarLayout.setVisibility(View.VISIBLE);
        }
    }



    @Override
    protected boolean onBackPressed() {
        if (mFilterBarLayout != null && mFilterBarLayout.isShowing()) {
            mFilterBarLayout.back();
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_dressing_room;
    }
}
