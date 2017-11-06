package com.sugarya.closet.ui.widgets.enhancerecycler.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sugarya.closet.R;
import com.sugarya.closet.ui.widgets.enhancerecycler.indicator.LoadingIndicatorView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ethan on 17/1/15.
 */
public class InternalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "InternalAdapter";

    private static final int HEADER_ITEM_TYPE = 170118;
    private static final int FOOTER_ITEM_TYPE = 170116;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> mExternalAdapter;
    private int mBodyItemCount;
    private FooterView mFooterView;
    private
    @LayoutRes
    int mExternalHeaderResId;
    private View mHeaderView;

    private boolean isHeaderViewVisibility = true;
    /**
     * HeaderView是否和body的item同步显示
     */
    private boolean mIsHeaderViewIndependenceShow;
    private boolean mIsShowFootView = true;

    public InternalAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> externalAdapter) {
        mExternalAdapter = externalAdapter;
        mBodyItemCount = externalAdapter.getItemCount();
    }


    @Override
    public int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return HEADER_ITEM_TYPE;
        } else if (isFootView(position)) {
            return FOOTER_ITEM_TYPE;
        }
        return mExternalAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_ITEM_TYPE:
                if (mExternalHeaderResId <= 0 && mHeaderView != null) {
                    return new HeaderView(mHeaderView);
                }
                View headerView = LayoutInflater.from(parent.getContext()).inflate(mExternalHeaderResId, parent, false);
                return new HeaderView(headerView);
            case FOOTER_ITEM_TYPE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_indicator, parent, false);
                mFooterView = new FooterView(view);
                return mFooterView;
            default:
                return mExternalAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position)) {
            return;
        }
        if (isFootView(position)) {
            return;
        }
        if (mExternalHeaderResId > 0 || mHeaderView != null) {
            position = position - getHeaderViewCount();
        }
        mExternalAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        mBodyItemCount = mExternalAdapter.getItemCount();
        if (mBodyItemCount == 0) {
            if (mIsHeaderViewIndependenceShow) {
                return getHeaderViewCount();
            }
            return 0;
        }

        return getHeaderViewCount() + mBodyItemCount + getFootViewCount();
    }


    private boolean isHeaderView(int position) {
        if (isHeaderViewVisibility()) {
            return (mExternalHeaderResId > 0 || mHeaderView != null) && position == 0;
        }
        return false;
    }

    private boolean isFootView(int position) {
        return (position >= mBodyItemCount + getHeaderViewCount());
    }

    public int getFootViewCount() {
        return mIsShowFootView ? 1 : 0;
    }

    public int getHeaderViewCount() {
        if (isHeaderViewVisibility()) {
            if (mExternalHeaderResId > 0 || mHeaderView != null) {
                return 1;
            }
        }
        return 0;
    }


    public void setFootViewVisible(boolean visible) {
        if(visible) {
            if (mFooterView != null) {
                mFooterView.setLoadingIndicatorViewVisible(View.VISIBLE);
            }
        }else{
            if (mFooterView != null) {
                mFooterView.setLoadingIndicatorViewVisible(View.GONE);
            }
        }
    }


    public void setExternalHeaderResId(int externalHeaderResId) {
        setExternalHeaderResId(externalHeaderResId, false);
    }

    public void setExternalHeaderView(View headerView) {
        setExternalHeaderView(headerView, false);
    }

    public void setExternalHeaderView(View headerView, boolean isIndependenceShow) {
        mHeaderView = headerView;
        mIsHeaderViewIndependenceShow = isIndependenceShow;
    }

    /**
     * @param externalHeaderResId
     * @param isIndependenceShow  HeaderView是否独立显示
     */
    public void setExternalHeaderResId(int externalHeaderResId, boolean isIndependenceShow) {
        mExternalHeaderResId = externalHeaderResId;
        mIsHeaderViewIndependenceShow = isIndependenceShow;
    }

    public void loadMoreOnSuccess() {
        loadMoreOnComplete();
    }

    public void loadMoreOnError() {
        loadMoreOnComplete();
    }

    public void loadMoreOnComplete() {
        setFootViewVisible(false);
    }


    public boolean isHeaderViewVisibility() {
        return isHeaderViewVisibility;
    }

    public void setHeaderViewVisibility(boolean headerViewVisibility, boolean notifyNow) {
        isHeaderViewVisibility = headerViewVisibility;
        if (notifyNow) {
            notifyDataSetChanged();
        }
    }

    static class HeaderView extends RecyclerView.ViewHolder {

        HeaderView(View itemView) {
            super(itemView);
        }
    }

    static class FooterView extends RecyclerView.ViewHolder {

        @BindView(R.id.item_footer_indicator)
        LoadingIndicatorView mLoadingIndicatorView;

        FooterView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setLoadingIndicatorViewVisible(int visible) {
            mLoadingIndicatorView.setVisibility(visible);
        }
    }
}
