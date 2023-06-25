@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.widgets

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.contentColorFor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.util.constant.Constants

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {
    val modelBottomSheetRoundedCornersTransitionState = remember { MutableTransitionState(false) }
    modelBottomSheetRoundedCornersTransitionState.targetState = sheetState.currentValue == ModalBottomSheetValue.Expanded

    val searchFieldTransition = updateTransition(
        transitionState = modelBottomSheetRoundedCornersTransitionState,
        label = "searchFieldTransition"
    )

    val modelBottomSheetRoundedCornersTransition by searchFieldTransition.animateDp(
//        transitionSpec = {
//            tween(
//                durationMillis = 200,
//                easing = FastOutSlowInEasing
//            )
//        },
        targetValueByState = { if (it) 0.dp else 24.dp },
        label = "modelBottomSheetRoundedCornersTransition"
    )

    ModalBottomSheetLayout(
        sheetContent = sheetContent,
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(
            topStart = modelBottomSheetRoundedCornersTransition,
            topEnd = modelBottomSheetRoundedCornersTransition
        ),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        content = content,
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun MotModalBottomSheetLayoutPreview() {
    MotModalBottomSheetLayout(
        sheetContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                content = { Text(text = "asd") }
            )
        },
        sheetState = ModalBottomSheetState(ModalBottomSheetValue.HalfExpanded),
        content = {}
    )
}
