package com.sugarya.closet.ui.square;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sugarya.closet.model.entity.SquareEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Ethan on 2017/9/24.
 */

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.SquareViewHolder> {

    private static final String TAG = "TAG";

    private List<SquareEntity> mDataList;

    public SquareAdapter() {
        mDataList = new ArrayList<>();
    }

    @Override
    public SquareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SquareViewHolder holder, int position) {
        SquareEntity entity = mDataList.get(position);
        holder.onBindViewHolder(entity);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class SquareViewHolder extends RecyclerView.ViewHolder{

        public SquareViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBindViewHolder(SquareEntity entity){

        }
    }
}
