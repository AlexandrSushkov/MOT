package dev.nelson.mot.main.util.binding

import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.BindingAdapter
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

@BindingAdapter(value = ["showSelectedCategories", "expandedLayout", "collapsedLayout"], requireAll = false)
fun ConstraintLayout.expandBottomMenu(isExpand: Boolean, expandedLayout: Int, collapsedLayout: Int) {
    val expandableMenu: ConstraintLayout = this
    val expandSet = ConstraintSet()
    val collapseSet = ConstraintSet()
    val transition = ChangeBounds()
    transition.interpolator = OvershootInterpolator()
    expandSet.clone(context, expandedLayout)
    collapseSet.clone(context, collapsedLayout)
    TransitionManager.beginDelayedTransition(expandableMenu, transition)
    val menuStateSet = if (isExpand) expandSet else collapseSet
    menuStateSet.applyTo(expandableMenu)
}

@BindingAdapter("isShow")
fun ProgressBar.isShow(isShow: Boolean) {
    visibility = setVisibility(isShow)
}

@BindingAdapter("isShow")
fun TextView.isShow(isShow: Boolean) {
    visibility = setVisibility(isShow)
}

@BindingAdapter("cost")
fun TextView.cost(cost: Int) {
    text=cost.toString()
}

private fun setVisibility(isShow: Boolean): Int = if (isShow) View.VISIBLE else View.GONE
