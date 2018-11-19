package dev.nelson.mot.main.presentations.navigationcomponent.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.NavHomeFragmentBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class NavHomeFragment : BaseFragment() {

    lateinit var binding:NavHomeFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.nav_home_fragment, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navFragmentTitle.text = this.javaClass.simpleName
    }
}