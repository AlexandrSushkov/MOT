package dev.nelson.mot.presentations.screen

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev.nelson.mot.R
import dev.nelson.mot.databinding.ActivityDataBaseTransferBinding
import dev.nelson.mot.extentions.getDataBinding
import dev.nelson.mot.extentions.getViewModel
import dev.nelson.mot.presentations.base.BaseActivity
import javax.inject.Inject

class DataBaseTransferActivity: BaseActivity(){

    companion object {
        fun getIntent(context: Context) = Intent(context, DataBaseTransferActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var b: ActivityDataBaseTransferBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_data_base_transfer)
        b.vm = getViewModel(factory)
    }
}