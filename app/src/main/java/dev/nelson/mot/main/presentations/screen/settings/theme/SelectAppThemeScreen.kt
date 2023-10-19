package dev.nelson.mot.main.presentations.screen.settings.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.widget.AppIconButtons
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.widget.AppToolbar
import dev.nelson.mot.core.ui.model.MotAppTheme
import dev.utils.preview.MotPreview

@Composable
fun SelectAppThemeScreen(
    selectAppThemeViewModel: SelectAppThemeViewModel,
    closeScreenAction: () -> Unit
) {
    val viewState by selectAppThemeViewModel.selectAppViewState.collectAsState()

    SelectAppThemeLayout(
        closeScreenAction = closeScreenAction,
        viewState = viewState,
        onThemeSelected = { selectAppThemeViewModel.onAppThemeSelected(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectAppThemeLayout(
    closeScreenAction: () -> Unit,
    viewState: SelectAppThemeViewState,
    onThemeSelected: (MotAppTheme) -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppToolbar.Regular(
                appBarTitle = stringResource(R.string.app_theme_title),
                navigationIcon = {
                    AppIconButtons.Back {
                        closeScreenAction.invoke()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            viewState.appThemeList.forEach {
                ListItem(
                    modifier = Modifier.clickable { onThemeSelected.invoke(it) },
                    leadingContent = {
                        RadioButton(
                            selected = it == viewState.selectedAppTheme,
                            onClick = { onThemeSelected.invoke(it) }
                        )
                    },
                    headlineContent = { Text(it.javaClass.name) }
                )
            }
        }
    }
}

@MotPreview
@Composable
private fun SelectAppThemePreview() {
    AppTheme {
        SelectAppThemeLayout(
            viewState = SelectAppThemeViewState(),
            closeScreenAction = {}
        )
    }
}
