@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import android.animation.TimeInterpolator
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import dev.utils.MotTransitions
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

private const val ANIMATION_DURATION = 500 // in milliseconds
private const val SWIPE_TO_DISMISS_THRESHOLD = 125 // in dp

@Composable
fun MotDismissibleListItem(
    isShow: Boolean = true,
    directions: Set<DismissDirection> = emptySet(),
    onItemSwiped: () -> Unit,
    dismissContent: @Composable (RowScope.() -> Unit)
) {
    fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val enterTransition = remember { MotTransitions.listItemEnterTransition }
    val exitTransition = remember { MotTransitions.listItemExitTransition }

    val dismissState = rememberDismissState(
        confirmValueChange = { dismissValue ->
            Log.d("MotDismissibleListItem", "confirmValueChange: $dismissValue")
            if (dismissValue == DismissValue.DismissedToStart) {
                onItemSwiped.invoke()
                true
            } else {
                false
            }
        },
        positionalThreshold = { SWIPE_TO_DISMISS_THRESHOLD.dp.toPx() }
    )

    val dismissibleBackgroundTargetColor = when (dismissState.targetValue) {
        DismissValue.Default -> MaterialTheme.colorScheme.surfaceVariant
        DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.surfaceVariant
        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
    }
    val dismissibleBackgroundColor by animateColorAsState(
        targetValue = dismissibleBackgroundTargetColor,
        label = "dismissible Background  Color"
    )
    val dismissibleIconTargetColor = when (dismissState.targetValue) {
        DismissValue.Default -> MaterialTheme.colorScheme.onSurfaceVariant
        DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.onSurfaceVariant
        DismissValue.DismissedToStart -> MaterialTheme.colorScheme.onErrorContainer
    }
    val dismissibleIconColor by animateColorAsState(
        targetValue = dismissibleIconTargetColor,
        finishedListener = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
        label = "dismissible Icon Color"
    )

    LaunchedEffect(key1 = Any(), block = {
        if (isShow) {
            scope.launch { dismissState.snapTo(DismissValue.Default) }
        }
    })

    AnimatedVisibility(
        visible = isShow,
        enter = enterTransition,
        exit = exitTransition
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
                val alignment = when (direction) {
                    DismissDirection.StartToEnd -> Alignment.CenterStart
                    DismissDirection.EndToStart -> Alignment.CenterEnd
                }
                val icon: ImageVector? = when (direction) {
                    DismissDirection.EndToStart -> Icons.Default.Delete
                    else -> null
                }
                val iconScaleAnimation by animateFloatAsState(
                    targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.1f,
                    animationSpec = FloatTweenSpec(
                        duration = ANIMATION_DURATION,
                        easing = AnticipateOvershootInterpolator().toEasing()
                    ),
                    label = "icon Scale Animation"
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(dismissibleBackgroundColor),
                    contentAlignment = alignment
                ) {
                    icon?.let {
                        IconToggleButton(
                            modifier = Modifier.padding(end = 16.dp),
                            checked = false,
                            onCheckedChange = {}
                        ) {
                            Icon(
                                imageVector = it,
                                tint = dismissibleIconColor,
                                contentDescription = "Localized description",
                                modifier = Modifier.scale(iconScaleAnimation)
                            )
                        }
                    }
                }
            },
            dismissContent = dismissContent,
            directions = directions,
        )
    }
}

@MotPreview
@Composable
private fun MotDismissibleListItemDefaultPreview() {
    MotMaterialTheme {
        MotDismissibleListItem(
            directions = setOf(DismissDirection.EndToStart),
            onItemSwiped = {}
        ) {
            MotCard {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "MotDismissibleListItem"
                        )
                    }
                )
            }
        }
    }
}

@MotPreview
@Composable
private fun MotDismissibleListItemDismissedToStartPreview() {
    MotMaterialTheme {
        MotDismissibleListItem(
            directions = setOf(DismissDirection.EndToStart),
            onItemSwiped = {}
        ) {
            MotCard {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "MotDismissibleListItem"
                        )
                    }
                )
            }
        }
    }
}

@MotPreview
@Composable
private fun MotDismissibleListItemDismissedToEndPreview() {
    MotMaterialTheme {
        MotDismissibleListItem(
            directions = setOf(DismissDirection.EndToStart),
            onItemSwiped = {}
        ) {
            MotCard {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "MotDismissibleListItem"
                        )
                    }
                )
            }
        }
    }
}
