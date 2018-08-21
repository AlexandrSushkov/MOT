package dev.nelson.mot.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev.nelson.mot.R
import dev.nelson.mot.presentations.base.BaseActivity

class HomeActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

}
