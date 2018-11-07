package dev.nelson.mot.main.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.nelson.mot.main.data.model.Movie
import io.reactivex.Observable
import java.io.IOException

class MovieRepositoryImpl(private val context: Context): MovieRepository {

    private val movieList:List<Movie>
    private val genres:MutableSet<String> = HashSet()

    init {
        movieList = getTestData()
        movieList.forEach {
            val genresForMovie: List<String> = it.genre.split("|").map{ s -> s.trim() }
            genres.addAll(genresForMovie)
        }
    }

    override fun getMovieList() = Observable.just(movieList)!!

    override fun getGenres() = Observable.just(genres.toList())!!

    private fun getTestData(): List<Movie> {
        val movieListString = loadJSONFromAsset("movies_list.json")
        val gson = Gson()
        val collectionType = object : TypeToken<Collection<Movie>>() {}.type
        return gson.fromJson<List<Movie>>(movieListString, collectionType)
    }

    private fun loadJSONFromAsset(filename: String): String? {
        var json: String? = null
        try {
            val movieListFile = context.assets.open(filename)
            val size = movieListFile.available()
            val buffer = ByteArray(size)
            movieListFile.read(buffer)
            movieListFile.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }

}

interface MovieRepository{
    fun getMovieList(): Observable<List<Movie>>
    fun getGenres(): Observable<List<String>>
}