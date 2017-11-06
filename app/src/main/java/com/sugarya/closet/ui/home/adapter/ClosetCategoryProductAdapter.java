package com.sugarya.closet.ui.home.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sugarya.closet.R;
import com.sugarya.closet.model.entity.ProductEntity;
import com.sugarya.closet.ui.home.event.AddToDressRoomEvent;
import com.sugarya.closet.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ethan on 2017/9/23.
 * 衣橱类衣服适配器
 */

public class ClosetCategoryProductAdapter extends RecyclerView.Adapter<ClosetCategoryProductAdapter.ProductViewHolder> {

    private static final String TAG = "ClosetCategoryProductAdapter";


    private List<ProductEntity> mDisplayDataList = new ArrayList<>();
    private List<ProductEntity> mSourceDataList;

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_closet_category_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        ProductEntity entity = mDisplayDataList.get(position);
        holder.onBindViewHolder(entity);
    }

    @Override
    public int getItemCount() {
        return mDisplayDataList != null ? mDisplayDataList.size() : 0;
    }


    public void updateData(List<ProductEntity> productEntities){
        if(productEntities == null || productEntities.isEmpty()){
            int originSize = mDisplayDataList.size();
            mDisplayDataList.clear();
            notifyItemRangeRemoved(0, originSize);
            return;
        }
        mSourceDataList = productEntities;

        int originSize = mDisplayDataList.size();
        mDisplayDataList.clear();
        notifyItemRangeRemoved(0, originSize);

        int itemCount = productEntities.size();
        mDisplayDataList.addAll(productEntities);
        notifyItemRangeChanged(0, itemCount);
    }

    public void updateData(ProductEntity entity){
        if(mDisplayDataList.contains(entity)){
            int indexOf = mDisplayDataList.indexOf(entity);
            mDisplayDataList.get(indexOf).setInDressingRoom(entity.isInDressingRoom());
            notifyDataSetChanged();
        }
    }

    public void addProductEntity(ProductEntity entity){
        int originSize = mDisplayDataList.size();
        if(!mDisplayDataList.contains(entity)){
            mDisplayDataList.add(entity);
            notifyItemRangeInserted(originSize, 1);
        }
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item_home_category_product)
        ImageView mImgProduct;

        @BindView(R.id.container_item_home_category_add_star)
        RelativeLayout mContainerAddStat;

        @BindView(R.id.tv_iem_home_category)
        TextView mTvCategory;

        @BindView(R.id.img_item_home_category_star)
        ImageView mImgStar;

        ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBindViewHolder(ProductEntity entity) {
            if (entity == null) {
                return;
            }
            int resId = entity.getResId();
            if(resId <= 0){
                Uri uri = entity.getUri();
                if(uri != null) {
                    mImgProduct.setImageURI(uri);
                }
            }else{
                mImgProduct.setImageResource(resId);
            }

            if(entity.isInDressingRoom()){
                mImgStar.setImageResource(R.drawable.ic_star_01);
            }else{
                mImgStar.setImageResource(R.drawable.ic_star_00);
            }
        }

        @OnClick(R.id.container_item_home_category_add_star)
        void onItemClick() {
            int position = getAdapterPosition();
            if (position >= 0 && position < mSourceDataList.size()) {
                ProductEntity productEntity = mSourceDataList.get(position);
                boolean inDressingRoom = productEntity.isInDressingRoom();
                if(!inDressingRoom){
                    productEntity.setInDressingRoom(true);
                    mImgStar.setImageResource(R.drawable.ic_star_01);
                    RxBus.getInstance().send(new AddToDressRoomEvent(productEntity, 1));
                }else{
                    productEntity.setInDressingRoom(false);
                    mImgStar.setImageResource(R.drawable.ic_star_00);
                    RxBus.getInstance().send(new AddToDressRoomEvent(productEntity, -1));
                }
            }
        }
    }
}
