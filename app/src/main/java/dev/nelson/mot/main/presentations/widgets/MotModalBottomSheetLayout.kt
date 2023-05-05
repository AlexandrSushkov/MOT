@file:OptIn(ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.widgets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.contentColorFor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetState: ModalBottomSheetState,
    content: @Composable () -> Unit
) {
    ModalBottomSheetLayout(
        sheetContent = sheetContent,
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun assdf() {
    MotModalBottomSheetLayout(sheetContent = {
        Column(modifier = Modifier.fillMaxSize(), content = { Text(text = "asd") })
    }, sheetState = ModalBottomSheetState(ModalBottomSheetValue.HalfExpanded)) {}
}
