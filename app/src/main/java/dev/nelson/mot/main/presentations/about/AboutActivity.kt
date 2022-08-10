package dev.nelson.mot.main.presentations.about

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.ui.theme.MotTheme

@AndroidEntryPoint
class AboutActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text("About")
                }
            }
        }
    }
}
