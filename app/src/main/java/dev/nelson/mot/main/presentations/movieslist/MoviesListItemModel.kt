package dev.nelson.mot.main.presentations.movieslist

import dev.nelson.mot.main.data.model.Movie

sealed class MoviesListItemModel {

    class MovieItemModel(val movieItem: Movie) : MoviesListItemModel()

    object Header : MoviesListItemModel()

    object Footer : MoviesListItemModel()

    class Letter(val letter: dev.nelson.mot.main.presentations.movieslist.Letter) : MoviesListItemModel()

}