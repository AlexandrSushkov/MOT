package dev.nelson.mot.main.presentations.payment_list

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PaymentSwipeToDeleteCallback(
    private val adapter: PaymentListAdapter,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //swipes works only for PaymentListAdapter.Companion.PaymentItemViewHolder items
        return if (viewHolder is PaymentListAdapter.Companion.PaymentItemViewHolder) {
            super.getSwipeDirs(recyclerView, viewHolder)
        } else {
            return 0
        }
    }

    //    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

}