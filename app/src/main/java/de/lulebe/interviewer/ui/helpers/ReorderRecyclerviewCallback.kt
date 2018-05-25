package de.lulebe.interviewer.ui.helpers

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class ReorderRecyclerviewCallback(
        val moveListener: (Int, Int) -> Unit
) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(ItemTouchHelper.UP.or(ItemTouchHelper.DOWN), 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        moveListener(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun isLongPressDragEnabled() = false
}