package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppButtons
import dev.nelson.mot.core.ui.widget.AppIcons
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
import kotlinx.coroutines.delay

object AppDialog {
    @Composable
    fun EditCategoryDialog(
        categoryToEditId: Int? = null,
        categoryNameState: TextFieldValue,
        onCategoryNameChanged: (TextFieldValue) -> Unit,
        closeEditCategoryDialog: () -> Unit,
        onSaveCategoryClick: () -> Unit
    ) {
        val categoryNameFocusRequester = remember { FocusRequester() }
        val maxLength = 64
        LaunchedEffect(
            key1 = Unit,
            block = {
                delay(Constants.DEFAULT_ANIMATION_DELAY)
                categoryNameFocusRequester.requestFocus()
            }
        )

        AlertDialog(
            onDismissRequest = closeEditCategoryDialog,
            icon = { AppIcons.Category(Modifier.size(48.dp)) },
            text = {
                OutlinedTextField(
                    value = categoryNameState,
                    onValueChange = {
                        if (it.text.length <= maxLength) onCategoryNameChanged.invoke(it)
                    },
                    label = { Text(stringResource(R.string.category_name)) },
                    supportingText = {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.align(Alignment.End),
                                text = "${categoryNameState.text.length} / $maxLength"
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(categoryNameFocusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSaveCategoryClick.invoke() })
                )
            },
            confirmButton = {
                val buttonTextId = categoryToEditId?.let { R.string.text_edit } ?: R.string.text_add
                AppButtons.TextButton(
                    id = buttonTextId,
                    enabled = categoryNameState.text.isNotEmpty(),
                    onClick = onSaveCategoryClick
                )
            }
        )
    }
}

@MotPreview
@Composable
private fun EditCategoryDialogPreview() {
    AppTheme {
        AppDialog.EditCategoryDialog(
            categoryToEditId = null,
            categoryNameState = TextFieldValue(),
            onCategoryNameChanged = {},
            closeEditCategoryDialog = {},
            onSaveCategoryClick = {}
        )
    }
}
