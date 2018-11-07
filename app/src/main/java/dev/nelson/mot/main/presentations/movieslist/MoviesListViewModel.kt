package dev.nelson.mot.main.presentations.movieslist

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.jakewharton.rxrelay2.PublishRelay
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class MoviesListViewModel : BaseViewModel() {

    val movies = ObservableArrayList<Movie>()
    val categories = ObservableArrayList<String>()
    val selectedCategories = ObservableArrayList<String>()
    val onMovieItemClickPublisher: PublishRelay<Movie> = PublishRelay.create()

    init {
        onMovieItemClickPublisher
                .applyThrottling()
                .subscribe{ Log.e("tag", it.toString())}
                .addTo(disposables)
    }


    fun initMovieList(movieUseCase: MovieUseCase) {
        movieUseCase.getMovieList()
                .subscribeBy(onNext = { movies.addAll(it) },
                        onError = { it.printStackTrace() })
                .addTo(disposables)
    }


}