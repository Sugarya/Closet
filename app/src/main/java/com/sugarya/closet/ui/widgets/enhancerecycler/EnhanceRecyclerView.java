package com.sugarya.closet.ui.widgets.enhancerecycler;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.sugarya.closet.ui.widgets.enhancerecycler.adapter.InternalAdapter;


/**
 * Created by Ethan on 17/1/15.
 * 提供上拉加载更多数据，列表的数据刷新，需要使用该类成员方法notifyXX
 */
public class EnhanceRecyclerView extends RecyclerView {

    private static final String TAG = "EnhanceRecyclerView";

    private OnLoadMoreListener mOnLoadMoreListener;
    private InternalAdapter mInternalAdapter;
    private View mEmptyView;
    private
    @LayoutRes
    int mHeaderResId;
    private View mHeaderView;
    private AdapterDataObserver mAdapterDataObserver = new EnhanceAdapterDataObserver();
    private volatile boolean mIsLoadingMore = false;
    /**
     * 滚动方向
     */
    private int mScrollDy = 0;

    private boolean mIsHeaderViewIndependenceShow;

    public EnhanceRecyclerView(Context context) {
        super(context);
    }

    public EnhanceRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhanceRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        mScrollDy = dy;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        switch (state) {
            case SCROLL_STATE_IDLE:
                LayoutManager layoutManager = getLayoutManager();
                int itemCount = getAdapter().getItemCount();
                int lastVisibleItemPosition = 0;

                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                }

                if (lastVisibleItemPosition >= itemCount - 1) {
                    if (getParent() instanceof SwipeRefreshLayout) {
                        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) getParent();
                        if (swipeRefreshLayout.isRefreshing()) {
                            break;
                        }
                    }

                    if (mOnLoadMoreListener != null && mScrollDy > 0) {
                        if (!mIsLoadingMore) {
                            synchronized (EnhanceRecyclerView.class) {
                                if (!mIsLoadingMore) {
                                    mIsLoadingMore = true;
                                    mInternalAdapter.setFootViewVisible(true);
                                    mOnLoadMoreListener.onLoadMore();
                                }
                            }
                        }
                    }
                }
                break;
        }
    }


    /**
     * 重写此方法，设置GridLayout的上拉加载更多视图的位置
     *
     * @param layout
     */
    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof GridLayoutManager) {
            final GridLayoutManager externalGridLayoutManager = (GridLayoutManager) layout;
            final int spanCount = externalGridLayoutManager.getSpanCount();
            int orientation = externalGridLayoutManager.getOrientation();

            final GridLayoutManager innerGridLayoutManager = new GridLayoutManager(getContext(), spanCount, orientation, false);
            innerGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int headerViewCount = mInternalAdapter.getHeaderViewCount();
                    int footViewCount = mInternalAdapter.getFootViewCount();
                    if (position < headerViewCount) {
                        return spanCount;
                    }

                    int totalItemCount = innerGridLayoutManager.getItemCount();
                    if (position >= totalItemCount - footViewCount) {
                        return spanCount;
                    }

                    return externalGridLayoutManager.getSpanSizeLookup().getSpanSize(position - headerViewCount);
                }
            });
            super.setLayoutManager(innerGridLayoutManager);
        } else {
            super.setLayoutManager(layout);
        }
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public final void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        setupEmptyViewHierarchy(emptyView);
    }

    protected void setupEmptyViewHierarchy(View emptyView) {
        ((ViewGroup) getParent().getParent()).addView(emptyView, 0);
    }

    public void addHeaderResId(@LayoutRes int resId) {
        addHeaderResId(resId, false);
    }

    public void addHeaderView(View view) {
        if (mHeaderView != null) {
            return;
        }
        addHeaderView(view, false);
    }

    public void addHeaderView(View view, boolean isIndependenceShow) {
        mHeaderView = view;
        if (mInternalAdapter != null) {
            mInternalAdapter.setExternalHeaderView(view, isIndependenceShow);
        }
        mIsHeaderViewIndependenceShow = isIndependenceShow;
    }

    public void addHeaderResId(@LayoutRes int resId, boolean isIndependence) {
        if (mHeaderView != null) {
            return;
        }

        mHeaderResId = resId;
        if (mInternalAdapter != null) {
            mInternalAdapter.setExternalHeaderResId(resId, isIndependence);
        }
        mIsHeaderViewIndependenceShow = isIndependence;
    }

    public void setHeaderViewVisibility(boolean isVisibility) {
        setHeaderViewVisibility(isVisibility, true);
    }

    public void setHeaderViewVisibility(boolean isVisibility, boolean updateNow) {
        if (mInternalAdapter != null) {
            mInternalAdapter.setHeaderViewVisibility(isVisibility, updateNow);
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        mInternalAdapter = new InternalAdapter(adapter);
        super.setAdapter(mInternalAdapter);

        //addHeaderView方法依赖于setAdapter方法
        if (mHeaderResId <= 0 && mHeaderView != null) {
            addHeaderView(mHeaderView, mIsHeaderViewIndependenceShow);
        } else if (mHeaderResId > 0 && mHeaderView == null) {
            addHeaderResId(mHeaderResId, mIsHeaderViewIndependenceShow);
        }

        mInternalAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        mAdapterDataObserver.onChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }


    public void loadMoreOnSuccess() {
        mIsLoadingMore = false;
        if (mInternalAdapter != null) {
            mInternalAdapter.loadMoreOnSuccess();
        }
    }

    public void loadMoreOnError() {
        mIsLoadingMore = false;
        if (mInternalAdapter != null) {
            mInternalAdapter.loadMoreOnError();
        }
    }

    public void loadMoreOnComplete() {
        mIsLoadingMore = false;
        if (mInternalAdapter != null) {
            mInternalAdapter.loadMoreOnComplete();
        }
    }


    public final void notifyDataSetChanged() {
        mInternalAdapter.notifyDataSetChanged();
    }

    public final void notifyItemChanged(int position) {
        mInternalAdapter.notifyItemChanged(position);
    }

    public final void notifyItemChanged(int position, Object payload) {
        position = position + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemChanged(position, payload);
    }

    public final void notifyItemRangeChanged(int positionStart, int itemCount) {
        positionStart = positionStart + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    public final void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        positionStart = positionStart + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    public final void notifyItemInserted(int position) {
        position = position + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemInserted(position);
    }

    public final void notifyItemMoved(int fromPosition, int toPosition) {
        fromPosition = fromPosition + mInternalAdapter.getHeaderViewCount();
        toPosition = toPosition + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    public final void notifyItemRangeInserted(int positionStart, int itemCount) {
        positionStart = positionStart + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemRangeInserted(positionStart, itemCount);
    }

    public final void notifyItemRemoved(int position) {
        position = position + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemRemoved(position);
    }

    public final void notifyItemRangeRemoved(int positionStart, int itemCount) {
        positionStart = positionStart + mInternalAdapter.getHeaderViewCount();
        mInternalAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }


    public InternalAdapter getInternalAdapter() {
        return mInternalAdapter;
    }

    /**
     * 上拉加载更多回调
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    private class EnhanceAdapterDataObserver extends AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            if (getEmptyView() != null && getAdapter() != null) {
                int itemCount = getAdapter().getItemCount();
                if (itemCount == 0) {
                    getEmptyView().setVisibility(VISIBLE);
                    setVisibility(GONE);
                } else {
                    getEmptyView().setVisibility(GONE);
                    setVisibility(VISIBLE);
                }
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            onChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            onChanged();
        }
    }

}
