package com.sugarya.closet.ui;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sugarya.closet.R;
import com.sugarya.closet.ui.common.basic.BasicActivity;
import com.sugarya.closet.ui.common.basic.BasicFragment;
import com.sugarya.closet.ui.home.fragment.ClosetFragment;
import com.sugarya.closet.ui.home.fragment.HomeFragment;
import com.sugarya.closet.ui.mine.MineFragment;
import com.sugarya.closet.ui.square.SquareFragment;

import butterknife.BindView;

public class MainContainerActivity extends BasicActivity {

    private static final String TAG = "MainContainerActivity";


    @BindView(R.id.main_container_body)
    FrameLayout mMainContent;

    @BindView(R.id.bnv_navigation)
    BottomNavigationView mBnvNavigation;

    @BindView(R.id.main_container)
    RelativeLayout mContainer;


    //arguments


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBottomNavigationView();
        initializeShow();
    }


    @Override
    protected void onStart() {
        super.onStart();
        enterBottomBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        exitBottomBar();
    }

    private void initializeShow() {
        showOneFragment(HomeFragment.class.getSimpleName(), true, LaunchMode.STANDARD, false);
    }


    private void initBottomNavigationView() {
        mBnvNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        showOneFragment(HomeFragment.class.getSimpleName(), true, LaunchMode.EXCHANGE, false);
                        return true;
                    case R.id.navigation_square:
                        showOneFragment(SquareFragment.class.getSimpleName(), true, LaunchMode.EXCHANGE, false);
                        return true;
                    case R.id.navigation_mine:
                        showOneFragment(MineFragment.class.getSimpleName(), true, LaunchMode.EXCHANGE, false);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected BasicFragment fragmentProvider(String fragmentTab, Bundle arguments) {
        BasicFragment fragment;
        if(HomeFragment.class.getSimpleName().equals(fragmentTab)){
            fragment = HomeFragment.newInstance(arguments);
        }else if(SquareFragment.class.getSimpleName().equals(fragmentTab)){
            fragment = SquareFragment.newInstance(arguments);
        }else if(MineFragment.class.getSimpleName().equals(fragmentTab)){
            fragment = MineFragment.newInstance(arguments);
        }
        else{
            fragment = HomeFragment.newInstance(arguments);
        }
        return fragment;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 底部导航条进入动画
     */
    public void enterBottomBar() {
        String tab1 = HomeFragment.class.getSimpleName();
        String tab2 = SquareFragment.class.getSimpleName();
        String tab3 = MineFragment.class.getSimpleName();
        String currentTab = getCurrentFragment() != null ? getCurrentFragment().getClass().getSimpleName() : "";
        if (tab1.equals(currentTab) || tab2.equals(currentTab) || tab3.equals(currentTab)) {
            if (mBnvNavigation != null) {
                mBnvNavigation.post(new Runnable() {
                    @Override
                    public void run() {
                        Animator enterAnimator = AnimatorInflater.loadAnimator(MainContainerActivity.this, R.animator.bottom_enter);
                        enterAnimator.setTarget(mBnvNavigation);
                        enterAnimator.start();
                    }
                });
            }
        }
    }

    /**
     * 底部导航条退出动画
     */
    public void exitBottomBar() {
        if (mBnvNavigation != null) {
            mBnvNavigation.post(new Runnable() {
                @Override
                public void run() {
                    Animator exitAnimator = AnimatorInflater.loadAnimator(MainContainerActivity.this, R.animator.bottom_exit);
                    exitAnimator.setTarget(mBnvNavigation);
                    exitAnimator.start();
                }
            });
        }
    }

    @Override
    protected int getFragmentContainerResID() {
        return R.id.main_container_body;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }
}
