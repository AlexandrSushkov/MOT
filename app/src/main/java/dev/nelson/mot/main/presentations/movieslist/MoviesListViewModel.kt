package dev.nelson.mot.main.presentations.movieslist

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import com.jakewharton.rxrelay2.PublishRelay
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy

class MoviesListViewModel : BaseViewModel() {

    val movies = ObservableArrayList<Movie>()
    val genres = ObservableArrayList<String>()
    val selectedCategories = ObservableArrayList<String>()
    val filter = ObservableField<String>("Filter test")
    val onMovieItemClickPublisher: PublishRelay<Movie> = PublishRelay.create()
    private lateinit var movieUseCase: MovieUseCase

    init {
        onMovieItemClickPublisher
                .applyThrottling()
                .subscribe { Log.e("tag", it.toString()) }
                .addTo(disposables)
    }


    fun initMovieList(movieUseCase: MovieUseCase) {
        this.movieUseCase = movieUseCase
        movieUseCase.getMovieList()
                .subscribeBy(onNext = { movies.addAll(it) },
                        onError = { it.printStackTrace() })
                .addTo(disposables)
    }

    fun initGenres() {
        //use case is not initialized
        movieUseCase.getGenres()
                .subscribeBy(onNext = {
                    genres.addAll(it)
                    Log.e("tag", it.toString())
                },
                        onError = { it.printStackTrace() })
                .addTo(disposables)
    }


}