package dev.nelson.mot.main.util.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.data.model.Movie
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.presentations.categories.CategoryAdapter
import dev.nelson.mot.main.presentations.movieslist.MoviesAdapter
import dev.nelson.mot.main.presentations.movieslist.MoviesListItemModel
import dev.nelson.mot.main.presentations.payment_list.PaymentListAdapter
import dev.nelson.mot.main.util.recycler.decoration.GridSpacingItemDecoration

@BindingAdapter(value = ["gridSpacingItemDecoration"])
fun RecyclerView.applyItemDecoration(padding: Float) =
        addItemDecoration(GridSpacingItemDecoration(context, padding.toInt(), GridSpacingItemDecoration.VERTICAL_AND_HORIZONTAL))

@BindingAdapter(value = ["setMovies", "onMovieItemClick"])
fun RecyclerView.setMovies(movies: List<Movie>, onItemClickPublisher: Relay<Movie>) {
}

@BindingAdapter(value = ["setMoviesModelsList", "onMovieItemClick"])
fun RecyclerView.setMoviesModelsList(movies: List<MoviesListItemModel>, onItemClickPublisher: Relay<Movie>) {
    if(adapter == null){
        adapter = MoviesAdapter()
    }
    (adapter as MoviesAdapter).submitData(movies)
}


@BindingAdapter(value = ["setCategories"])
fun RecyclerView.setCategories(categoryEntities: List<CategoryEntity>) {
}

@BindingAdapter(value = ["setPayments", "onPaymentClick"])
fun RecyclerView.setPayments(payments: List<Payment>, onPaymentClickListener: Relay<Payment>) {
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

@BindingAdapter(value = ["categoryAdapter"])
fun RecyclerView.setCategoryAdapter(adapter: CategoryAdapter){
    this.adapter = adapter
}

@BindingAdapter(value = ["paymentAdapter"])
fun RecyclerView.setPaymentAdapter(adapter: PaymentListAdapter){
    this.adapter = adapter
}