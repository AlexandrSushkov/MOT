package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.repository.MovieRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    fun getMovieList(): Observable<List<Movie>> = movieRepository.getMovieList().subscribeOn(Schedulers.io())

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
