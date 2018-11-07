package dev.nelson.mot.main.domain

import dev.nelson.mot.main.data.repository.MovieRepository

class MovieUseCase(private val movieRepository: MovieRepository) {

    fun getMovieList() = movieRepository.getMovieList()

    fun getGenres() = movieRepository.getGenres()

}