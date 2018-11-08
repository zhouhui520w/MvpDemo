package com.xiaoxi.base;

import android.support.annotation.AnimRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


import com.xiaoxi.listener.OnItemClickListener;

import java.util.List;

/**
 * Created by zhouhui on 2018/4/18.
 *
 */

public class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_ITEM = 0;
    public static final int TYPE_FOOTER = 1;
    public static final int TYPE_HEADER = 2;
    protected int mLastPosition = -1;
    protected boolean mIsShowFooter;
    protected boolean mIsShowHeader;
    protected List<T> mList;
    protected OnItemClickListener mOnItemClickListener;

    public BaseRecyclerViewAdapter(List<T> list) {
        mList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (getItemViewType(position) == TYPE_FOOTER) {
            if (layoutParams != null) {
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView
                            .getLayoutParams();
                    params.setFullSpan(true);
                }
            }
        }
        if (getItemViewType(position) == TYPE_HEADER) {
            if (layoutParams != null) {
                if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView
                            .getLayoutParams();
                    params.setFullSpan(true);
                }
            }
        }
    }

/*    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFooterPosition(position)) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                params.setFullSpan(true);
            }
        }

    }*/

    protected View getView(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        int itemSize = mList.size();
        if (mIsShowFooter) {
            itemSize += 1;
        }
        if (mIsShowHeader) {
            itemSize += 1;
        }
        return itemSize;
    }

    protected void setItemAppearAnimation(RecyclerView.ViewHolder holder, int position, @AnimRes int type) {
        if (position > mLastPosition/* && !isFooterPosition(position)*/) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), type);
            holder.itemView.startAnimation(animation);
            mLastPosition = position;
        }
    }

    protected boolean isFooterPosition(int position) {
        return (getItemCount() - 1) == position;
    }

    public void add(int position, T item) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void addMore(List<T> data) {
        int startPosition = mList.size();
        mList.addAll(data);
        notifyItemRangeInserted(startPosition, mList.size());
    }

    public void delete(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> items) {
        mList = items;
    }

    public void showFooter() {
        mIsShowFooter = true;
        notifyItemInserted(getItemCount());
    }

    public void hideFooter() {
        mIsShowFooter = false;
        notifyItemRemoved(getItemCount());
    }

    public void showHeader() {
        mIsShowHeader = true;
        notifyItemInserted(getItemCount());
    }

    public void hideHeader() {
        mIsShowHeader = false;
        notifyItemRemoved(getItemCount());
    }

    protected class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


}
