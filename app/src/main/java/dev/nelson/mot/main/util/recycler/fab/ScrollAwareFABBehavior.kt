package dev.nelson.mot.main.util.recycler.fab

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrollAwareFABBehavior(context: Context?, attrs: AttributeSet?) : FloatingActionButton.Behavior() {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View,
                                     axes: Int,
                                     type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout,
                                child: FloatingActionButton,
                                target: View,
                                dxConsumed: Int,
                                dyConsumed: Int,
                                dxUnconsumed: Int,
                                dyUnconsumed: Int,
                                type: Int,
                                consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)

        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide(object : FloatingActionButton.OnVisibilityChangedListener() {
                //bug from support library, fab doesn't change visibility, just scale;
                override fun onHidden(fab: FloatingActionButton) {
                    super.onHidden(fab)
                    child.animate().rotation(90f).setInterpolator(LinearInterpolator()).start()
                    fab.visibility = View.INVISIBLE
                }
            })
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.animate().rotation(-90f).setInterpolator(LinearInterpolator()).start()
            child.show()
        }
    }
}
