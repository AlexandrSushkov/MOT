package dev.nelson.mot.main.presentations.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.main.presentations.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(HomeActivity.getIntent(this))
        finish()
    }
}
