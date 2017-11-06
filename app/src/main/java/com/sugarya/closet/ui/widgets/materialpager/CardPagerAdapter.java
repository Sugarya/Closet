package com.sugarya.closet.ui.widgets.materialpager;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.home.event.RemoveItemEvent;
import com.sugarya.closet.ui.home.event.SelectedDressingRoomMatchItemEvent;
import com.sugarya.closet.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private static final String TAG = "CardPagerAdapter";

    private List<CardView> mViews;
    private List<ProductEntity> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(ProductEntity item) {
        mViews.add(null);
        mData.add(item);
        notifyDataSetChanged();
    }

    public void removeCardItem(ProductEntity item){
        if(mData.contains(item)){
            mData.remove(item);
            mViews.remove(null);
            notifyDataSetChanged();
        }
    }


    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        if(position < mViews.size()){
            return mViews.get(position);
        }
        return  null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        final ProductEntity entity = mData.get(position);
        bind(entity, view);

        TextView tvMatch = (TextView)view.findViewById(R.id.tv_item_dressing_confirm);
        tvMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxBus.getInstance().send(new SelectedDressingRoomMatchItemEvent(entity));
            }
        });

        ImageView imgClose = (ImageView)view.findViewById(R.id.img_item_dressing_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(entity);
                mViews.set(position, null);
                notifyDataSetChanged();
                entity.setInDressingRoom(false);
                RxBus.getInstance().send(new RemoveItemEvent(entity));
            }
        });

        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(ProductEntity item, View view) {
        ImageView imgDressing = (ImageView) view.findViewById(R.id.img_item_dressing);
        int resId = item.getResId();
        if(resId <= 0){
            Uri uri = item.getUri();
            if(uri != null){
                imgDressing.setImageURI(uri);
            }
        }else {
            imgDressing.setImageResource(resId);
        }
    }

}
