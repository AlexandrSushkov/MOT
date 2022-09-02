package dev.nelson.mot.main.presentations.splash

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.nelson.mot.main.presentations.home.MainActivityCompose
import kotlinx.coroutines.coroutineScope

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        splash.setOnExitAnimationListener{
            startActivity(MainActivityCompose.getIntent(this))
            finish()
        }
    }
}
