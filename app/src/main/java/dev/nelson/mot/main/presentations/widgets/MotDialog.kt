package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.widget.AppIcons
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.main.presentations.AlertDialogParams
import dev.utils.preview.MotPreviewScreen

@Composable
fun MotAlertDialog(alertDialogParams: AlertDialogParams) {
    AlertDialog(
        onDismissRequest = alertDialogParams.dismissClickCallback,
        icon = { AppIcons.Info(modifier = Modifier.size(32.dp)) },
        text = {
            Text(
                text = stringResource(alertDialogParams.message),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        dismissButton = {
            alertDialogParams.onNegativeClickCallback?.let {
                TextButton(onClick = it) {
                    Text(text = stringResource(android.R.string.cancel))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = alertDialogParams.onPositiveClickCallback) {
                Text(text = stringResource(android.R.string.ok))
            }
        }
    )
}

@MotPreviewScreen
@Composable
private fun MotAlertDialogPreview() {
    val alertDialogParams = AlertDialogParams(
        message = R.string.database_export_failed_dialog_message,
        onPositiveClickCallback = {},
        dismissClickCallback = {}
    )
    AppTheme {
        MotAlertDialog(alertDialogParams)
    }
}

@MotPreviewScreen
@Composable
private fun MotAlertDialogWithNegativeActionPreview() {
    val alertDialogParams = AlertDialogParams(
        message = R.string.database_export_failed_dialog_message,
        onPositiveClickCallback = {},
        onNegativeClickCallback = {},
        dismissClickCallback = {}
    )
    AppTheme {
        MotAlertDialog(alertDialogParams)
    }
}
