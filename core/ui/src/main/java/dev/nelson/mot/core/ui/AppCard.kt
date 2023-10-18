package dev.nelson.mot.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.utils.preview.MotPreview

object AppCard {
    @Composable
    fun Rectangular(
        modifier: Modifier = Modifier,
        colors: CardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation: CardElevation = CardDefaults.cardElevation(),
        content: @Composable ColumnScope.() -> Unit
    ) {
        Card(
            modifier = modifier,
            colors = colors,
            shape = RoundedCornerShape(0.dp),
            content = content,
            elevation = elevation
        )
    }
}

@MotPreview
@Composable
private fun MotCardPreview() {
    AppTheme {
        AppCard.Rectangular {
            Box(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Mot Card"
                    )
                }
            )
        }
    }
}
