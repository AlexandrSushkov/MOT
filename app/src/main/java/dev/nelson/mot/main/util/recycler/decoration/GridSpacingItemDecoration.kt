package dev.nelson.mot.main.util.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.nelson.mot.main.R
import dev.nelson.mot.main.util.extention.isEven

/**
 * this space decoration works only with grid with 2 columns and vertical orientation
 */
class GridSpacingItemDecoration(
    context: Context,
    private val paddingLeft: Int,
    private val paddingTop: Int,
    private val paddingRight: Int,
    private val paddingBottom: Int,
    private val paddingFlag: Int
) : RecyclerView.ItemDecoration() {

    constructor(context: Context, flag: Int) : this(context, R.dimen.small, flag)

    constructor(context: Context, padding: Int, flag: Int) : this(context, padding, padding, padding, padding, flag)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildViewHolder(view)?.adapterPosition
        val itemsCount = parent.adapter?.itemCount
        if (position != null && itemsCount != null){
//            setTopPadding(outRect, position)
//            setInnerPadding(outRect, position)
//            setBottomPadding(outRect, position, itemsCount)

            when (paddingFlag) {
                VERTICAL -> setVerticalPadding(outRect, position, itemsCount)
                HORIZONTAL -> setHorizontalPadding(outRect, position, itemsCount)
                VERTICAL_AND_HORIZONTAL -> {
                    setVerticalPadding(outRect, position, itemsCount)
                    setHorizontalPadding(outRect, position, itemsCount)
                }
            }
        }

    }

    private fun setTopPadding(outRect: Rect, position: Int) {
        if (position == 1 || position == 2) {
            outRect.top = paddingTop
        }
    }

    private fun setBottomPadding(outRect: Rect, position: Int, itemsCount: Int) {
        if (itemsCount.isEven() && position == itemsCount - 1) {
            outRect.bottom = paddingBottom
        }
        if (position == itemsCount) {
            outRect.bottom = paddingBottom
        }
    }

    private fun setInnerPadding(outRect: Rect, position: Int) {
        if (position.isEven()) {
            outRect.top = paddingTop
        }
    }

    private fun setVerticalPadding(outRect: Rect, position: Int, itemsCount: Int) {
            if (position.isEven()){
                outRect.left = paddingLeft
                outRect.right = paddingRight / 2
            }else{
                outRect.left = paddingLeft / 2
                outRect.right = paddingRight
            }
    }

    private fun setHorizontalPadding(outRect: Rect, position: Int, itemsCount: Int) {
        outRect.top = paddingTop / 2
        outRect.bottom = paddingBottom / 2
    }

    companion object {
        const val VERTICAL = 1
        const val HORIZONTAL = 2
        const val VERTICAL_AND_HORIZONTAL = 3
    }
}
