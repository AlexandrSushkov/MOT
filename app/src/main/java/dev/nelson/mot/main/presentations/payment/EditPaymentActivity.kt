package dev.nelson.mot.presentations.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.nelson.mot.R
import dev.nelson.mot.main.presentations.base.BaseActivity

class EditPaymentActivity: BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, EditPaymentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_payment)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { finish() }
    }
}