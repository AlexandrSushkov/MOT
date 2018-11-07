package dev.nelson.mot.main.presentations.payment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev.nelson.mot.main.presentations.base.BaseActivity

class EditPaymentActivity: BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, EditPaymentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_edit_payment)
//
//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { finish() }
    }
}