package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.repository.MovieRepository

class MovieUseCase(private val movieRepository: MovieRepository) {

    fun getMovieList() = movieRepository.getMovieList()

    fun getFilteredMovieList(selectedGenres: List<String>) = movieRepository.getMovieList()
            .flatMapIterable { it }
//            .filter { it.genre.contains(selectedGenres[0]) }
//            .toList()
            .filter { containSelected(it, selectedGenres) }
            .toList()

    fun containSelected(movie: Movie, selectedGenres: List<String>): Boolean {
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