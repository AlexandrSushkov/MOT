@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MotCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(0.dp),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun MotCardPreviewLight() {
    MotMaterialTheme(darkTheme = false) {
        MotCardPreviewContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotCardPreviewDark() {
    MotMaterialTheme(darkTheme = true) {
        MotCardPreviewContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun MotCardPreviewDynamic() {
    MotMaterialTheme(dynamicColor = true) {
        MotCardPreviewContent()
    }
}

@Composable
private fun MotCardPreviewContent() {
    MotCard {
        Box(
            modifier = Modifier.fillMaxWidth(),
            content = { Text(
                modifier = Modifier.padding(16.dp),
                text = "Mot Card") }
        )
    }
}
