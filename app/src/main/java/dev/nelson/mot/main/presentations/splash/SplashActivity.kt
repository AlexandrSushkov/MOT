package dev.nelson.mot.main.presentations.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.main.presentations.home.MainActivityCompose

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(MainActivityCompose.getIntent(this))
        finish()
    }
}
