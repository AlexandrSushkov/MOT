package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppIcons
import dev.theme.AppDimens
import dev.utils.preview.MotPreview

@Composable
fun AppListPlaceholder(
    modifier: Modifier = Modifier,
    iconContent: (@Composable () -> Unit)? = null,
    text: String = stringResource(id = R.string.text_no_content)
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            iconContent?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                ) {
                    it.invoke()
                }
            }
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = text
            )
        }
    }
}

@MotPreview
@Composable
private fun EmptyListPlaceholderPreview() {
    AppTheme {
        Surface {
            AppListPlaceholder(
                modifier = Modifier.fillMaxSize(),
                iconContent = { AppIcons.EmptyList(Modifier.size(AppDimens.list_placeholder_icon_size)) },
                text = "No data yet."
            )
        }
    }
}
