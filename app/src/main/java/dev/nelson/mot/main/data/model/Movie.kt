package dev.nelson.mot.main.data.model

import com.google.gson.annotations.SerializedName

data class Movie(val id: Long,
                 @SerializedName("movie_title") val movieTitle: String,
                 @SerializedName("movie_ganre")val genre: String,
                 val date:String,
                 val country: String) {
}