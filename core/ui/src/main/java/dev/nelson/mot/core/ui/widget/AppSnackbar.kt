package dev.nelson.mot.core.ui.widget

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.AppTheme
import dev.utils.preview.MotPreview

object AppSnackbar {
    @Composable
    fun Regular(
        messageText: String,
        actionButtonText: String,
        onActionButtonClick: () -> Unit
    ) {

        Snackbar(
            action = {
                AppButtons.TextButton(
                    text = actionButtonText,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSecondary),
                    onClick = onActionButtonClick
                )
            },
            modifier = Modifier.padding(8.dp),
            content = { Text(messageText) }
        )
    }
}

@MotPreview
@Composable
private fun AppSnackbarPreview() {
    AppTheme {
        AppSnackbar.Regular(
            "Message",
            "Undo"
        ) {}
    }
}