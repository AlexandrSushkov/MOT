package dev.nelson.mot.main.presentations.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev.nelson.mot.main.presentations.base.BaseActivity

class DataBaseTransferActivity: BaseActivity(){

    companion object {
        fun getIntent(context: Context) = Intent(context, DataBaseTransferActivity::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_data_base_transfer)
    }
}