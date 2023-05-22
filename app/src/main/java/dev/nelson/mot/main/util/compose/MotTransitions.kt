package dev.nelson.mot.main.util.compose

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment
import dev.nelson.mot.main.util.constant.Constants

object MotTransitions {

    private const val DEFAULT_INITIAL_ALPHA = 0.3f

    /**
     * Expand from the start.
     */
    val enterStartHorizontalTransition = expandHorizontally(
        expandFrom = Alignment.Start,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    ) + fadeIn(
        initialAlpha = DEFAULT_INITIAL_ALPHA,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    )

    val exitStartHorizontalTransition = shrinkHorizontally(
        shrinkTowards = Alignment.Start,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    ) + fadeOut(
        // Fade in with the initial alpha of 0.3f.
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    )

    /**
     * Expand from the top.
     */
    val enterTopVerticalTransition = expandVertically(
        expandFrom = Alignment.Top,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    ) + fadeIn(
        initialAlpha = 0.3f,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    )

    val exitTopVerticalTransition = shrinkVertically(
        shrinkTowards = Alignment.Top,
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    ) + fadeOut(
        // Fade in with the initial alpha of 0.3f.
        animationSpec = tween(Constants.DEFAULT_ANIMATION_DURATION)
    )
}
