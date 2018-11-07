package dev.nelson.mot.main.utils.recycler.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dev.nelson.mot.main.R

class GridSpacingItemDecoration(context: Context,
                                paddingLeft: Int,
                                paddingTop: Int,
                                paddingRight: Int,
                                paddingBottom: Int, private val paddingFlag: Int) : RecyclerView.ItemDecoration() {

    private val paddingLeft: Int = paddingLeft
    private val paddingTop: Int = paddingTop
    private val paddingRight: Int = paddingRight
    private val paddingBottom: Int = paddingBottom

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
        outRect.left = paddingLeft / 2
        outRect.right = paddingRight / 2
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
