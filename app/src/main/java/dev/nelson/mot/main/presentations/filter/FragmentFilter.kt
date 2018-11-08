package dev.nelson.mot.main.presentations.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentFilterBinding
import dev.nelson.mot.main.presentations.movieslist.MoviesListViewModel
import dev.nelson.mot.main.util.extention.getDataBinding

class FragmentFilter : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_filter, container)
        val model = activity?.run { ViewModelProviders.of(this).get(MoviesListViewModel::class.java) }
//        binding.viewModel = getViewModel(ViewModelProviders.DefaultFactory(activity!!.application))
        binding.viewModel = model
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.viewModel?.initGenres()
//    }
}