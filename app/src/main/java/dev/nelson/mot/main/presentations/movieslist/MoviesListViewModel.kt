package dev.nelson.mot.main.presentations.movieslist

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import com.jakewharton.rxrelay2.PublishRelay
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.util.*

class MoviesListViewModel : BaseViewModel() {

    private val genresArray = Arrays.asList("Film-Noir", "Action", "Adventure", "Horror", "Romance", "War", "Documentary", "Sci-Fi", "Drama", "Thriller", "(no genres listed)",
            "Crime", "Fantasy", "Animation", "IMAX", "Comedy", "Mystery", "Children", "Musical")
    val movies = ObservableArrayList<Movie>()
    val genres = ObservableArrayList<String>()
    val selectedGenres = ObservableArrayList<String>()
    val selectedGenresVisibility = ObservableBoolean()
    val filter = ObservableField<String>("Filter test")
    val onMovieItemClickPublisher: PublishRelay<Movie> = PublishRelay.create()
    val onGenreItemClickPublisher: PublishRelay<Pair<String, Boolean>> = PublishRelay.create()
    val onSelectedGenreClickPublisher: PublishRelay<String> = PublishRelay.create()
    private lateinit var movieUseCase: MovieUseCase

    val expandedLayout = ObservableInt(R.layout.expanded)
    val collapsedLayout = ObservableInt(R.layout.collapsed)
    var isShowSelectedCategories = ObservableBoolean()

    init {
        onMovieItemClickPublisher
                .applyThrottling()
                .subscribe { Log.e("tag", it.toString()) }
                .addTo(disposables)

        onGenreItemClickPublisher
                .map {
                    if (it.second && !selectedGenres.contains(it.first)) selectedGenres.add(it.first)
                    if (!it.second && selectedGenres.contains(it.first)) selectedGenres.remove(it.first)
                    isShowSelectedCategories.set(selectedGenres.isEmpty())
                }
                .flatMapSingle { if (selectedGenres.isEmpty()) movieUseCase.getMovieList().firstOrError() else movieUseCase.getFilteredMovieList(selectedGenres) }
                .subscribe {
                    movies.clear()
                    movies.addAll(it)
                }
                .addTo(disposables)

        onSelectedGenreClickPublisher
                .map { selectedGenres.remove(it) }
                .flatMapSingle { if (selectedGenres.isEmpty()) movieUseCase.getMovieList().firstOrError() else movieUseCase.getFilteredMovieList(selectedGenres) }
                .subscribe {
                    movies.clear()
                    movies.addAll(it)
                    isShowSelectedCategories.set(selectedGenres.isEmpty())
                }
                .addTo(disposables)

        genres.addAll(genresArray)
    }


    fun initMovieList(movieUseCase: MovieUseCase) {
        this.movieUseCase = movieUseCase
        movieUseCase.getMovieList()
                .subscribeBy(onNext = {
                    movies.addAll(it)
                    isShowSelectedCategories.set(selectedGenres.isEmpty())
                },
                        onError = { it.printStackTrace() })
                .addTo(disposables)
    }

    fun onResetFilterClick() {
        selectedGenres.clear()
        isShowSelectedCategories.set(selectedGenres.isEmpty())
    }

    //    fun onClickClick() = selectedGenresVisibility.set(!selectedGenresVisibility.get())


//    fun initGenres() {
//        movieUseCase.getGenres()
//                .subscribeBy(onNext = {
//                    genres.addAll(it)
//                    Log.e("tag", it.toString())
//                },
//                        onError = { it.printStackTrace() })
//                .addTo(disposables)
//    }


}