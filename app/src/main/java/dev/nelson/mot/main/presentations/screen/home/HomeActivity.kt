@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.home

import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.app.MotApp
import dev.nelson.mot.main.util.FirebaseUtils

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by viewModels()
    private val motThemeViewModel: MotThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpenedFromWidget = intent
            ?.extras
            ?.getBoolean(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, false) ?: false

        installSplashScreen().apply {
            setKeepOnScreenCondition { splashScreenViewModel.isLoading.value }
        }

        setContent {
            val appThemeViewState by motThemeViewModel.appThemeViewState.collectAsState()

            MotMaterialTheme(appThemeViewState) {
                MotApp(
                    isOpenedFromWidget = isOpenedFromWidget,
                    finishAction = { finishAndRemoveTask() }
                )
            }
        }
        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        with(FirebaseUtils.remoteConfig) {
            setDefaultsAsync(R.xml.remote_config_defaults)
            setConfigSettingsAsync(configSettings)
            fetchAndActivate().addOnCompleteListener(this@HomeActivity) { task ->
//                if (task.isSuccessful) {
//                    Timber.d("config keys: ${remoteConfig.all.keys}")
//                    Toast.makeText(
//                        this,
//                        "Fetch and activate succeeded",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Fetch failed",
//                        Toast.LENGTH_SHORT,
//                    ).show()
//                }
                splashScreenViewModel.onRemoteConfigFetched()
            }
        }
    }
}
