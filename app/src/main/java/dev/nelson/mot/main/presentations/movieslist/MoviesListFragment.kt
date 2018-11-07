package dev.nelson.mot.main.presentations.movieslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.repository.MovieRepository
import dev.nelson.mot.main.data.repository.MovieRepositoryImpl
import dev.nelson.mot.main.databinding.FragmentMoiveListBinding
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.utils.extention.getDataBinding
import dev.nelson.mot.main.utils.extention.getViewModel
import dev.nelson.mot.presentations.base.BaseFragment
import java.io.IOException

class MoviesListFragment : BaseFragment() {

    lateinit var bindind: FragmentMoiveListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindind = getDataBinding(inflater, R.layout.fragment_moive_list, container)
        bindind.viewModel = getViewModel(ViewModelProviders.DefaultFactory(activity!!.application))
        return bindind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val viewModel = ViewModelProviders.of(this).get(MoviesListViewModel::class.java)
        val moviesRepository:MovieRepository = MovieRepositoryImpl(activity!!.applicationContext)
        val movieUseCase = MovieUseCase(moviesRepository)
        bindind.viewModel?.initMovieList(movieUseCase)
    }

    private fun getTestData(): List<Movie> {
        val movieListString = loadJSONFromAsset("movies_list.json")
        val gson = Gson()
        val collectionType = object : TypeToken<Collection<Movie>>() {}.type
        return gson.fromJson<List<Movie>>(movieListString, collectionType)
    }

    private fun loadJSONFromAsset(filename: String): String? {
        var json: String? = null
        try {
            val movieListFile = activity?.assets?.open(filename)
            val size = movieListFile?.available()
            val buffer = ByteArray(size!!)
            movieListFile.read(buffer)
            movieListFile.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }
}
