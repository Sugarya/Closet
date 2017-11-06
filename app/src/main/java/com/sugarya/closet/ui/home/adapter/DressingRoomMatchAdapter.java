package com.sugarya.closet.ui.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.home.activity.WebContainerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ethan on 2017/9/24.
 */

public class DressingRoomMatchAdapter extends RecyclerView.Adapter<DressingRoomMatchAdapter.MatchViewHolder> {

    private List<ProductEntity> mDataList = new ArrayList<>();
    private Context mContext;

    public DressingRoomMatchAdapter() {
    }

    public DressingRoomMatchAdapter(List<ProductEntity> dataList) {
        mDataList = dataList;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_dressing_room_match, parent, false);
        return new MatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        ProductEntity entity = mDataList.get(position);
        holder.onBindViewHolder(entity);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void notifyData(List<ProductEntity> dataList){
        if(dataList == null || dataList.isEmpty()){
            mDataList.clear();
            notifyDataSetChanged();
            return;
        }
        if(!mDataList.isEmpty()){
            int originSize = mDataList.size();
            mDataList.clear();
            notifyItemRangeRemoved(0, originSize);
        }
        int itemCount = dataList.size();
        mDataList.addAll(dataList);
        notifyItemRangeChanged(0, itemCount);
    }

    class MatchViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.container_item_dressing_room_match)
        CardView mContainerCardView;

        @BindView(R.id.img_item_dressing_match)
        ImageView mImg;

        MatchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBindViewHolder(ProductEntity entity){
            String linkUrl = entity.getLinkUrl();
            int resId = entity.getResId();
            mImg.setImageResource(resId);
        }

        @OnClick(R.id.container_item_dressing_room_match)
        void onItemClick(){
            int position = getAdapterPosition();
            if(position < mDataList.size()){
                ProductEntity entity = mDataList.get(position);
                String linkUrl = entity.getLinkUrl();
                mContext.startActivity(WebContainerActivity.getCallingIntent(mContext, linkUrl, "商品详情"));
            }
        }

    }
}
