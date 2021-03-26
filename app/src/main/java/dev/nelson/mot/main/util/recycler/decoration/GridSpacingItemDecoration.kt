package dev.nelson.mot.main.util.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.nelson.mot.main.R

class GridSpacingItemDecoration(context: Context,
                                private val paddingLeft: Int,
                                private val paddingTop: Int,
                                private val paddingRight: Int,
                                private val paddingBottom: Int, private val paddingFlag: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, flag: Int) : this(context, R.dimen.small, flag)

    constructor(context: Context, padding: Int, flag: Int) : this(context, padding, padding, padding, padding, flag)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (paddingFlag) {
            VERTICAL -> setVerticalPadding(outRect)
            HORIZONTAL -> setHorizontalPadding(outRect)
            VERTICAL_AND_HORIZONTAL -> {
                setVerticalPadding(outRect)
                setHorizontalPadding(outRect)
            }
        }
    }

    private fun setVerticalPadding(outRect: Rect) {
        outRect.left = paddingLeft
        outRect.right = paddingRight
    }

    private fun setHorizontalPadding(outRect: Rect) {
        outRect.top = paddingTop / 2
        outRect.bottom = paddingBottom / 2
    }

    companion object {
        const val VERTICAL = 1
        const val HORIZONTAL = 2
        const val VERTICAL_AND_HORIZONTAL = 3
    }
}
