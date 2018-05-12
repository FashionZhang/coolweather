package com.ikotori.coolweather.cityselect;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Fashion at 2018/05/12 14:50.
 * Describe:
 */

public class ItemTouchHelperCallback  extends ItemTouchHelper.Callback {

    private final int TYPE_NORMAL = 1;

    private final int TYPE_HOME = 0;

    private OnMoveAndSwipedListener mMoveAndSwipedListener;

    public ItemTouchHelperCallback(OnMoveAndSwipedListener mMoveAndSwipedListener) {
        this.mMoveAndSwipedListener = mMoveAndSwipedListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            //  TODO 暂未考虑GridLayout
        } else {
            if (viewHolder.getItemViewType() == TYPE_NORMAL) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }else {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mMoveAndSwipedListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder.getItemViewType() == TYPE_HOME) {
            return;
        }
        mMoveAndSwipedListener.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
