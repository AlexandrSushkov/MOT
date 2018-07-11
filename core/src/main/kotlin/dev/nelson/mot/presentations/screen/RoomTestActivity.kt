package dev.nelson.mot.presentations.screen

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import dev.nelson.mot.R
import dev.nelson.mot.databinding.ActivityRoomTestBinding
import dev.nelson.mot.extentions.getDataBinding
import dev.nelson.mot.extentions.getViewModel
import dev.nelson.mot.presentations.base.BaseActivity
import javax.inject.Inject

class RoomTestActivity: BaseActivity(){

    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var b: ActivityRoomTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = getDataBinding(R.layout.activity_room_test)
        b.vm = getViewModel(factory)
    }
}