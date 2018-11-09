package dev.nelson.mot.main.util.binding

import android.view.animation.OvershootInterpolator
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
