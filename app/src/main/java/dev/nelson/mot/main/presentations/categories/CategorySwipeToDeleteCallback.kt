package dev.nelson.mot.main.presentations.categories

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dev.nelson.mot.main.util.extention.isEven

class CategorySwipeToDeleteCallback(
    private val adapter: CategoryAdapter,
    dragDirs: Int,
    swipeDirs: Int
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        TODO("Not yet implemented")
    }

    override fun getSwipeDirs(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //swipes works only for CategoryAdapter.Companion.CategoryItemViewHolder items
        return if (viewHolder is CategoryAdapter.Companion.CategoryItemViewHolder) {
            super.getSwipeDirs(recyclerView, viewHolder)
            val position = viewHolder.adapterPosition
            if (position.isEven()) ItemTouchHelper.LEFT else ItemTouchHelper.RIGHT
        } else {
            ItemTouchHelper.ACTION_STATE_IDLE
        }
    }

    //    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
    }

}