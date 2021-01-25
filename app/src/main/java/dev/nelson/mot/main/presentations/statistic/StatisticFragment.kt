package dev.nelson.mot.main.presentations.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityStatisticBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class StatisticFragment : BaseFragment(){

    lateinit var binding: ActivityStatisticBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.activity_statistic, container)
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        return binding.root
    }

}
