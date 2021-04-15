package dev.nelson.mot.main.presentations.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.nelson.mot.main.presentations.home.HomeActivity
import dev.nelson.mot.main.presentations.paymentlist.PaymentListComposeActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        startActivity(HomeActivity.getIntent(this))
        startActivity(PaymentListComposeActivity.getIntent(this))
        finish()
    }
}
