package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.repository.MovieRepository
import dev.nelson.mot.main.presentations.movieslist.Letter
import dev.nelson.mot.main.presentations.movieslist.MoviesListItemModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import java.util.ArrayList
import java.util.function.BiConsumer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    fun getMovieList(): Observable<List<Movie>> = movieRepository.getMovieList().subscribeOn(Schedulers.io())

    fun getMovieModelList(): Observable<List<MoviesListItemModel>> =
        Observable.zip(
            Observable.just(MoviesListItemModel.Header),
            movieRepository.getMovieList()
                .map { movies: List<Movie> ->
                    movies.sortedBy { movie: Movie -> movie.movieTitle }
                        .groupBy { movie: Movie -> movie.movieTitle.first() }
                },
            Observable.just(MoviesListItemModel.Footer),
            Function3<MoviesListItemModel.Header, Map<Char, List<Movie>>, MoviesListItemModel.Footer, List<MoviesListItemModel>> { header, list, footer ->
                return@Function3 listOf<MoviesListItemModel>().toMutableList()
                    .apply {
                        add(header)
//                        list.sortedBy { movie: Movie -> movie.movieTitle }.groupBy { movie: Movie -> movie.movieTitle.first() }
//                            .map { entry ->
//                                {
//                                    add(MoviesListItemModel.Letter(Letter(entry.key.toString())))
//                                    addAll(entry.value.toMovieModelList())
//                                }
//                            }
                        list.forEach { (letter, moviesList) ->
                            add(MoviesListItemModel.Letter(Letter(letter.toString())))
                            addAll(moviesList.toMovieModelList())
                        }

                        add(footer)
                    }

            })
            .subscribeOn(Schedulers.io())

    fun getFilteredMovieList(selectedGenres: List<String>): Single<MutableList<Movie>> = movieRepository.getMovieList()
        .flatMapIterable { it }
        .filter { containSelected(it, selectedGenres) }
        .toList()
        .subscribeOn(Schedulers.io())

    private fun containSelected(movie: Movie, selectedGenres: List<String>): Boolean {
        var contains = false
        selectedGenres.forEach {
            if (movie.genre.contains(it)) {
                contains = true
            }
        }
        return contains
    }


    fun getGenres() = movieRepository.getGenres()

}

fun Movie.toMovieModel(): MoviesListItemModel.MovieItemModel = MoviesListItemModel.MovieItemModel(this)

fun List<Movie>.toMovieModelList(): List<MoviesListItemModel.MovieItemModel> = map { it.toMovieModel() }
