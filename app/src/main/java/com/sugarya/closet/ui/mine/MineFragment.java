package com.sugarya.closet.ui.mine;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugarya.closet.R;
import com.sugarya.closet.ui.common.basic.BasicToolbarFragment;

/**
 * Created by Ethan 2017/09/19
 */
public class MineFragment extends BasicToolbarFragment {


    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(Bundle args) {
        MineFragment fragment = new MineFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mine, menu);
    }


    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void setupToolbar() {
        setRawTitle("我 的");
    }
}
