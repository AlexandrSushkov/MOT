package dev.nelson.mot.main.presentations.movieslist

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.hilt.lifecycle.ViewModelInject
import com.jakewharton.rxrelay2.PublishRelay
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.domain.MovieUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class MoviesListViewModel @ViewModelInject constructor(private var movieUseCase: MovieUseCase) : BaseViewModel() {

    private val genresArray = listOf("Film-Noir", "Action", "Adventure", "Horror", "Romance", "War", "Documentary", "Sci-Fi", "Drama", "Thriller", "(no genres listed)",
        "Crime", "Fantasy", "Animation", "IMAX", "Comedy", "Mystery", "Children", "Musical")
    val movies = ObservableArrayList<Movie>()
    val moviesModelList = ObservableArrayList<MoviesListItemModel>()
    val genres = ObservableArrayList<String>()
    val selectedGenres = ObservableArrayList<String>()
    val selectedGenresVisibility = ObservableBoolean()
    val filter = ObservableField<String>("Filter test")
    val onMovieItemClickPublisher: PublishRelay<Movie> = PublishRelay.create()
    val onGenreItemClickPublisher: PublishRelay<Pair<String, Boolean>> = PublishRelay.create()
    val onSelectedGenreClickPublisher: PublishRelay<String> = PublishRelay.create()

    val expandedLayout = ObservableInt(R.layout.expanded)
    val collapsedLayout = ObservableInt(R.layout.collapsed)
    var isShowSelectedCategories = ObservableBoolean()
//    val onItemClickEvent: SingleLiveEvent<Void> = SingleLiveEvent()
    val onItemClickEvent: SingleLiveEvent<Unit> = SingleLiveEvent()


    init {
        movieUseCase.getMovieList()
            .subscribeBy(onNext = {
                movies.addAll(it)
                isShowSelectedCategories.set(selectedGenres.isEmpty())
            },
                onError = { it.printStackTrace() })
            .addToDisposables()

        onMovieItemClickPublisher
            .applyThrottling()
            .subscribe {
                Log.e("tag", it.toString())
                onItemClickEvent.postValue(Unit)
            }
            .addToDisposables()

        movieUseCase.getMovieModelList()
            .doOnNext { moviesModelList.addAll(it) }
            .doOnError { it.printStackTrace() }
            .subscribe()
            .addToDisposables()

        onMovieItemClickPublisher
            .applyThrottling()
            .subscribe {
                Log.e("tag", it.toString())
                onItemClickEvent.postValue(Unit)
            }
            .addToDisposables()

        onGenreItemClickPublisher
            .map {
                if (it.second && !selectedGenres.contains(it.first)) selectedGenres.add(it.first)
                if (!it.second && selectedGenres.contains(it.first)) selectedGenres.remove(it.first)
                isShowSelectedCategories.set(selectedGenres.isEmpty())
            }
            .flatMapSingle { if (selectedGenres.isEmpty()) movieUseCase.getMovieList().firstOrError() else movieUseCase.getFilteredMovieList(selectedGenres) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                movies.clear()
                movies.addAll(it)
            }
            .addToDisposables()

        onSelectedGenreClickPublisher
            .map { selectedGenres.remove(it) }
            .flatMapSingle { if (selectedGenres.isEmpty()) movieUseCase.getMovieList().firstOrError() else movieUseCase.getFilteredMovieList(selectedGenres) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                movies.clear()
                movies.addAll(it)
                isShowSelectedCategories.set(selectedGenres.isEmpty())
            }
            .addToDisposables()

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
                .addToDisposables()
    }

    fun onResetFilterClick() {
        selectedGenres.clear()
        isShowSelectedCategories.set(selectedGenres.isEmpty())
    }

    fun onResetFilterTextClick(view: View) {
        selectedGenres.clear()
        isShowSelectedCategories.set(selectedGenres.isEmpty())
    }


    fun initGenres() {
        movieUseCase.getGenres()
                .subscribeBy(onNext = {
                    genres.addAll(it)
                    Timber.e(it.toString())
                },
                        onError = { it.printStackTrace() })
                .addToDisposables()
    }


}