package dev.nelson.mot.main.presentations.statistic

import android.os.Bundle
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityStatisticBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding

class StatisticActivity : BaseActivity(){

    lateinit var binding: ActivityStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_statistic)

    }
}
