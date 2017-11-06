package com.sugarya.closet.ui.square;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sugarya.closet.R;
import com.sugarya.closet.ui.common.basic.BasicFragment;
import com.sugarya.closet.ui.common.basic.BasicToolbarFragment;

import butterknife.BindView;

/**
 * Created by Ethan 2017/09/17
 */
public class SquareFragment extends BasicToolbarFragment {

    @BindView(R.id.swipe_square)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_square)
    RecyclerView mRecyclerView;

    private static final String TAG = "SquareFragment";

    public SquareFragment() {
        // Required empty public constructor
    }

    public static SquareFragment newInstance(Bundle args ) {
        SquareFragment fragment = new SquareFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }



    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.orange), getResources().getColor(R.color.yellow));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
    }

    private void initRecyclerView(){

    }

    private void fetchData(){

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.square, menu);
    }

    @Override
    protected void setupToolbar() {
        setRawTitle("广 场");
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_square;
    }
}
