package dev.nelson.mot.main.presentations.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.repository.MovieRepository
import dev.nelson.mot.main.data.repository.MovieRepositoryImpl
import dev.nelson.mot.main.databinding.FragmentMoiveListBinding
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.utils.extention.getDataBinding
import dev.nelson.mot.main.utils.extention.getViewModel
import dev.nelson.mot.presentations.base.BaseFragment

class MoviesListFragment : BaseFragment() {

    private lateinit var binding: FragmentMoiveListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_moive_list, container)
        binding.viewModel = getViewModel(ViewModelProviders.DefaultFactory(activity!!.application))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val moviesRepository: MovieRepository = MovieRepositoryImpl(activity!!.applicationContext)
        val movieUseCase = MovieUseCase(moviesRepository)
        binding.viewModel?.initMovieList(movieUseCase)
    }
}
