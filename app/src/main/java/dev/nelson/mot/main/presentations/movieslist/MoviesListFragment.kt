package dev.nelson.mot.main.presentations.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentMoiveListBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.getViewModel
import javax.inject.Inject

class MoviesListFragment : BaseFragment() {

    companion object {
        fun getInstance(): MoviesListFragment = MoviesListFragment()
    }

    @Inject lateinit var factory: ViewModelProvider.Factory

    private lateinit var binding: FragmentMoiveListBinding
    private lateinit var viewModel: MoviesListViewModel
//    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_moive_list, container)
        viewModel = getViewModel(factory)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        val moviesRepository: MovieRepository = MovieRepositoryImpl(activity!!.applicationContext)
//        val movieUseCase = MovieUseCase(moviesRepository)
//        binding.viewModel?.initMovieList(movieUseCase)
//        bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.filter_sheet))
    }

    fun expandFilterFragment() {
//        val state = bottomSheetBehavior.state
//        when (state) {
//            BottomSheetBehavior.STATE_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            BottomSheetBehavior.STATE_HIDDEN -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//            BottomSheetBehavior.STATE_HALF_EXPANDED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//            BottomSheetBehavior.STATE_COLLAPSED -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//        }
    }

    fun collapseFilterFragment() {
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun isFilterOpen(): Boolean {
//        bottomSheetBehavior.state == BottomSheetBehavior.STATE_HALF_EXPANDED || bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
    return false
    }
}
