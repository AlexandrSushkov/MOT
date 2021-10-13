package dev.nelson.mot.main.presentations.movieslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ItemMovieBinding

class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var moviesItemModelList: ArrayList<MoviesListItemModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_movie -> MovieViewHolder(ItemMovieBinding.inflate(layoutInflater, parent, false))
            R.layout.item_header -> HeaderViewHolder(v)
            R.layout.item_letter -> LetterViewHolder(v)
//            R.layout.item_footer -> FooterViewHolder(v)
//            else -> LoadingViewholder(v)
            else -> FooterViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = moviesItemModelList[position]
        when (holder) {
            is HeaderViewHolder -> holder.bind(item as MoviesListItemModel.Header)
            is FooterViewHolder -> holder.bind(item as MoviesListItemModel.Footer)
            is MovieViewHolder -> holder.bind(item as MoviesListItemModel.MovieItemModel)
            is LetterViewHolder -> holder.bind(item as MoviesListItemModel.Letter)
        }
    }

    fun submitData(listItem: List<MoviesListItemModel>) {
        moviesItemModelList.clear()
        moviesItemModelList.addAll(listItem)
    }

    override fun getItemViewType(position: Int) = when (moviesItemModelList[position]) {
        is MoviesListItemModel.MovieItemModel -> R.layout.item_movie
        is MoviesListItemModel.Header -> R.layout.item_header
        is MoviesListItemModel.Footer -> R.layout.item_footer
        is MoviesListItemModel.Letter -> R.layout.item_letter

    }

    override fun getItemCount(): Int = moviesItemModelList.size

}

class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(header: MoviesListItemModel.Header) {}
}

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(footer: MoviesListItemModel.Footer) {}
}

class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(letter: MoviesListItemModel.Letter) {
        val letterView = itemView.findViewById<TextView>(R.id.letter)
        letterView.text = letter.letter.letter
    }
}

class MovieViewHolder(private val itemMovieBinding: ItemMovieBinding) : RecyclerView.ViewHolder(itemMovieBinding.root) {

    fun bind(movieItemModel: MoviesListItemModel.MovieItemModel) {
        itemMovieBinding.itemMovie = movieItemModel.movieItem
    }
}