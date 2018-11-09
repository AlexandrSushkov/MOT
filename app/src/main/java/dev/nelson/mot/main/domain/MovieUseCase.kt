package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.repository.MovieRepository
import io.reactivex.Single

class MovieUseCase(private val movieRepository: MovieRepository) {

    fun getMovieList() = movieRepository.getMovieList()

    fun getFilteredMovieList(selectedGenres: List<String>): Single<MutableList<Movie>> = movieRepository.getMovieList()
            .flatMapIterable { it }
            .filter { containSelected(it, selectedGenres) }
            .toList()

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