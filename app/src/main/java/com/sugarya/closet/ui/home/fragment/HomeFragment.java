package com.sugarya.closet.ui.home.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.common.basic.BasicToolbarFragment;
import com.sugarya.closet.ui.home.adapter.HomePagerAdapter;
import com.sugarya.closet.ui.home.event.AddToDressRoomEvent;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Ethan 2017/09/23
 * 主页
 */
public class HomeFragment extends BasicToolbarFragment {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.tab_home)
    TabLayout mTabLayout;

    @BindView(R.id.pager_home)
    ViewPager mViewPager;

    @BindView(R.id.tv_record_count)
    TextView mTvRecordCount;

    @BindView(R.id.fab_home)
    FloatingActionButton mFab;

    //arguments

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private ClosetFragment mClosetFragment;
    private DressingRoomFragment mDressingRoomFragment;
    private MaterialDialog mMaterialDialog;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void onRxBus() {
        addSubscription(AddToDressRoomEvent.class, new Action1<AddToDressRoomEvent>() {
            @Override
            public void call(AddToDressRoomEvent addToDressRoomEvent) {
                ProductEntity productEntity = addToDressRoomEvent.getProductEntity();
                int deltaCount = addToDressRoomEvent.getDeltaCount();
                if (productEntity == null) {
                    return;
                }

                String countStr = mTvRecordCount.getText().toString();
                int count = Integer.parseInt(countStr);
                if (count + deltaCount <= 0) {
                    mTvRecordCount.setText("0");
                    mTvRecordCount.setVisibility(View.GONE);
                } else {
                    mTvRecordCount.setText(String.valueOf(count + deltaCount));
                    mTvRecordCount.setVisibility(View.VISIBLE);
                }

                mDressingRoomFragment.notifyProductData(productEntity);
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
        initializeShow();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RxView.clicks(mFab).throttleFirst(800, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        reactionToFABClick();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void reactionToFABClick() {
        RxPermissions permissions = new RxPermissions(getActivity());
        permissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            openGallery();
                        } else {
                            Toast.makeText(getContext(), "同意授权相机和读取存储卡内容才能使用搭配功能", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void openGallery() {
        Matisse.from(this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Zhihu)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.sugarya.closet.camera.fileprovider"))
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            showDialog(uriList);
        }
    }

    private void showDialog(final List<Uri> uriList) {
        mMaterialDialog = new MaterialDialog
                .Builder(getContext())
                .title("您要把图片放入？")
                .positiveText("到试衣间")
                .negativeText("到衣橱")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mDressingRoomFragment.addCameraUri(uriList);
                        mViewPager.setCurrentItem(1, true);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mClosetFragment.addCameraUri(uriList);
                        mViewPager.setCurrentItem(0, true);
                        dialog.dismiss();
                    }
                })
                .build();
        mMaterialDialog.show();
    }


    private void initializeShow() {
        initViewPager();
    }

    private void initViewPager() {
        mFragmentList.clear();
        mClosetFragment = ClosetFragment.newInstance();
        mDressingRoomFragment = DressingRoomFragment.newInstance();
        mFragmentList.add(mClosetFragment);
        mFragmentList.add(mDressingRoomFragment);

        mTitleList.clear();
        mTitleList.add("衣橱");
        mTitleList.add("试衣间");

        HomePagerAdapter pagerAdapter = new HomePagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mTvRecordCount.setText("0");
                    mTvRecordCount.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected boolean onBackPressed() {
        if (mDressingRoomFragment != null && mDressingRoomFragment.onBackPressed()) {
            return true;
        }
        return super.onBackPressed();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupToolbar() {
        setRawTitle("主页");
    }
}
