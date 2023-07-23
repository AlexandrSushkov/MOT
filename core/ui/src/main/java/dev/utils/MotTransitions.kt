package dev.utils

import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.ui.Alignment

object MotTransitions {

    private const val DEFAULT_INITIAL_ALPHA = 0.3f
    private const val DEFAULT_ANIMATION_DURATION = 500 // in milliseconds

    /**
     * Expand from the start.
     */
    val enterStartHorizontalTransition = expandHorizontally(
        expandFrom = Alignment.Start,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    ) + fadeIn(
        initialAlpha = DEFAULT_INITIAL_ALPHA,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    )

    val exitStartHorizontalTransition = shrinkHorizontally(
        shrinkTowards = Alignment.Start,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    ) + fadeOut(
        // Fade in with the initial alpha of 0.3f.
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    )

    val fadeInTrans = fadeIn(
        initialAlpha = 0.3f,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    )

    val fadeOutTrans = fadeOut(
        targetAlpha = 0f,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    )

    /**
     * Expand from the top.
     */
    val enterTopVerticalTransition = expandVertically(
        expandFrom = Alignment.Top,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION),
        initialHeight = { 60 }
    )
//    + fadeIn(
////        initialAlpha = 0.3f,
//        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
//    )

    val exitTopVerticalTransition = shrinkVertically(
        shrinkTowards = Alignment.Top,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION),
        targetHeight = { 60 }
    )
//    + fadeOut(
//        // Fade in with the initial alpha of 0.3f.
//        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
//    )

    val listItemExitTransition = shrinkVertically(
        shrinkTowards = Alignment.Top,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION),
    ) + fadeOut(animationSpec = tween(DEFAULT_ANIMATION_DURATION))

    val listItemEnterTransition = expandVertically(
        expandFrom = Alignment.Top,
        animationSpec = tween(DEFAULT_ANIMATION_DURATION)
    ) + fadeIn(animationSpec = tween(DEFAULT_ANIMATION_DURATION))
}
