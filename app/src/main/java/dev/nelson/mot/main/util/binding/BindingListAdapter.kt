package dev.nelson.mot.main.util.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.nitrico.lastadapter.Holder
import com.github.nitrico.lastadapter.ItemType
import com.github.nitrico.lastadapter.LastAdapter
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.BR
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.databinding.ItemMovieBinding
import dev.nelson.mot.main.util.recycler.decoration.GridSpacingItemDecoration

@BindingAdapter(value = ["gridSpacingItemDecoration"])
fun RecyclerView.applyItemDecoration(padding: Float) =
        addItemDecoration(GridSpacingItemDecoration(context, padding.toInt(), GridSpacingItemDecoration.VERTICAL_AND_HORIZONTAL))

@BindingAdapter(value = ["setMovies", "onMovieItemClick"])
fun RecyclerView.setMovies(movies: List<Movie>, onItemClickPublisher: Relay<Movie>) {
    LastAdapter(movies, BR.itemMovie)
            .map<Movie>(object : ItemType<ItemMovieBinding>(R.layout.item_movie) {
                override fun onBind(holder: Holder<ItemMovieBinding>) {
                    holder.binding.publisher = onItemClickPublisher
                }
            })
            .into(this)
}

@BindingAdapter(value = ["setGenres", "selectedGenres", "onGenreClick"], requireAll = false)
fun ChipGroup.setGenres(genres: List<String>, selectedGenres: List<String>, onGenreClickPublisher: Relay<Pair<String, Boolean>>){
    this.removeAllViews()
    genres.forEach{
        val chip = Chip(context)
        chip.text = it
        chip.isCheckable = true
        chip.isChecked = selectedGenres.contains(it)
        chip.setOnCheckedChangeListener { _, isChecked -> onGenreClickPublisher.accept(Pair(it, isChecked)) }
        this.addView(chip)}
}

@BindingAdapter(value = ["setSelectedGenres", "onSelectedGenreClick"], requireAll = false)
fun ChipGroup.setSelectedGenres(selectedGenres: List<String>, onSelectedGenreClick: Relay<String>){
    this.removeAllViews()
    selectedGenres.forEach{
        val chip = Chip(context)
        chip.text = it
        chip.isCheckable = true
        chip.isChecked = true
        chip.setOnCheckedChangeListener { _, _ -> onSelectedGenreClick.accept(it) }
        this.addView(chip)}
}
