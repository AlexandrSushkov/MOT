package dev.nelson.mot.main.presentations.navigationcomponent.screen.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.NavAboutFragmentBinding
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.presentations.base.BaseFragment

class NavAboutFragment: BaseFragment() {

    lateinit var binding: NavAboutFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.nav_about_fragment, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navFragmentTitle.text = this.javaClass.simpleName
    }
}