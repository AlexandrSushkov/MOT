package dev.nelson.mot.main.presentations.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentAboutBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class AboutFragment: BaseFragment() {

    lateinit var binding: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_about, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.navFragmentTitle.text = this.javaClass.simpleName
    }
}
