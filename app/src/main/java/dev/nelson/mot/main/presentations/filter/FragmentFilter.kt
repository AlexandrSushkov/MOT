package dev.nelson.mot.main.presentations.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentFilterBinding
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.getViewModel

class FragmentFilter : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_filter, container)
        binding.viewModel = getViewModel(ViewModelProviders.DefaultFactory(activity!!.application))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewModel?.initGenres()
    }
}