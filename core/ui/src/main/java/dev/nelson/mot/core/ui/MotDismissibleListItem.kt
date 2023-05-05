@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.core.ui

import android.animation.TimeInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.IconToggleButton
import androidx.compose.material.ListItem
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MotDismissibleListItem(
    dismissState: DismissState,
    directions: Set<DismissDirection> = emptySet(),
    dismissContent: @Composable (RowScope.() -> Unit)
) {
    fun TimeInterpolator.toEasing() = Easing { x -> getInterpolation(x) }

    val haptic = LocalHapticFeedback.current
    val dismissibleBackgroundTargetColor = when (dismissState.targetValue) {
        DismissValue.Default -> MaterialTheme.colorScheme.background
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

    SwipeToDismiss(
        state = dismissState,
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.1f,
                animationSpec = FloatTweenSpec(
                    500,
                    0,
                    AnticipateOvershootInterpolator().toEasing()
                ),
                label = "scale",
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(dismissibleBackgroundColor),
                contentAlignment = alignment
            ) {
                IconToggleButton(
                    modifier = Modifier.padding(end = 16.dp),
                    checked = false,
                    onCheckedChange = {}
                ) {
                    Icon(
                        imageVector = icon,
                        tint = dismissibleIconColor,
                        contentDescription = "Localized description",
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        },
        dismissContent = dismissContent,
        directions = directions,
        dismissThresholds = { FractionalThreshold(0.35f) }
    )
}

@Preview(showBackground = true)
@Composable
private fun MotDismissibleListItemPreviewLight() {
    MotMaterialTheme(darkTheme = false) {
        MotDismissibleListItemPreviewData()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotDismissibleListItemPreviewDark() {
    MotMaterialTheme(darkTheme = true) {
        MotDismissibleListItemPreviewData()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotDismissibleListItemPreviewDynamic() {
    MotMaterialTheme(dynamicColor = true) {
        MotDismissibleListItemPreviewData()
    }
}

@Composable
private fun MotDismissibleListItemPreviewData() {
    MotDismissibleListItem(
        dismissState = DismissState(DismissValue.Default),
//        dismissState = DismissState(DismissValue.DismissedToStart),
        directions = setOf(DismissDirection.EndToStart),
        dismissContent = {
            Card {
                ListItem(
                    trailing = { },
                    text = { Text(text = "MotDismissibleListItem") }
                )
            }
        }
    )
}
