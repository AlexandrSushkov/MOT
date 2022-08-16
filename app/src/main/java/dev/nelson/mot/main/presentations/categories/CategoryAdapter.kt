package dev.nelson.mot.main.presentations.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.databinding.ItemCategoryBinding
import dev.nelson.mot.main.presentations.payment_list.PaymentListAdapter

class CategoryAdapter(
    private val onItemClickPublisher: Relay<Category>,
    private val onItemLongClickPublisher: Relay<Category>,
    private val onSwipeToDeleteAction: Relay<Category>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val categoryItemModelList = emptyList<CategoryListItemModel>().toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_category -> CategoryItemViewHolder(
                ItemCategoryBinding.inflate(layoutInflater, parent, false),
                onItemClickPublisher,
                onItemLongClickPublisher
            )
            R.layout.item_empty -> EmptyViewHolder(v)
            R.layout.item_letter -> LetterViewHolder(v)
//            R.layout.item_header -> PaymentListAdapter.HeaderViewHolder(v)
            R.layout.item_footer -> PaymentListAdapter.FooterViewHolder(v)
            else -> LoadingViewHolder(layoutInflater.inflate(R.layout.item_loading, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = categoryItemModelList[position]
        when (holder) {
//            is CategoryAdapter.HeaderViewHolder -> holder.bind(item as PaymentListItemModel.Header)
//            is FooterItemViewHolder -> holder.bind(item as PaymentListItemModel.Footer)
            is CategoryItemViewHolder -> holder.bind(item as CategoryListItemModel.CategoryItemModel)
            is LetterViewHolder -> holder.bind(item as CategoryListItemModel.Letter)
        }
    }

    override fun getItemCount(): Int = categoryItemModelList.size

    fun setData(newCategories: List<CategoryListItemModel>) {
        val diffCallback = CategoryListDiffCallback(categoryItemModelList, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categoryItemModelList.clear()
        categoryItemModelList.addAll(newCategories)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int) = when (categoryItemModelList[position]) {
        is CategoryListItemModel.CategoryItemModel -> R.layout.item_category
        is CategoryListItemModel.Letter -> R.layout.item_letter
        is CategoryListItemModel.Header -> R.layout.item_header
        is CategoryListItemModel.Footer -> R.layout.item_footer
        is CategoryListItemModel.Empty -> R.layout.item_empty

    }

    fun deleteItem(position: Int) {
        if (categoryItemModelList[position] is CategoryListItemModel.CategoryItemModel) {
            val category = (categoryItemModelList[position] as CategoryListItemModel.CategoryItemModel).category
            onSwipeToDeleteAction.accept(category)
        }
    }

    companion object {
        class CategoryItemViewHolder(
            private val itemCategoryBinding: ItemCategoryBinding,
            private val onItemClickPublisher: Relay<Category>,
            private val onItemLongClickPublisher: Relay<Category>
        ) : RecyclerView.ViewHolder(itemCategoryBinding.root) {

            fun bind(categoryItemModel: CategoryListItemModel.CategoryItemModel) {
                with(itemCategoryBinding) {
                    category = categoryItemModel.category
                    categoryCard.setOnClickListener { onItemClickPublisher.accept(categoryItemModel.category) }
                    categoryCard.setOnLongClickListener {
                        onItemLongClickPublisher.accept(categoryItemModel.category)
                        true
                    }
                }
            }
        }

//        class FooterItemViewHolder() : RecyclerView.ViewHolder(itemCategoryBinding.root) {

//        }

        class LetterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bind(letter: CategoryListItemModel.Letter) {
                val letterView = itemView.findViewById<TextView>(R.id.letter)
                letterView.text = letter.letter
            }
        }

        class EmptyViewHolder(v: View) : RecyclerView.ViewHolder(v)

        class LoadingViewHolder(v: View) : RecyclerView.ViewHolder(v)

        class CategoryListDiffCallback(
            private val oldList: List<CategoryListItemModel>,
            private val newList: List<CategoryListItemModel>
        ) : DiffUtil.Callback() {

            override fun getOldListSize(): Int = oldList.size

            override fun getNewListSize(): Int = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return if (oldList[oldItemPosition] is CategoryListItemModel.CategoryItemModel && newList[newItemPosition] is CategoryListItemModel.CategoryItemModel) {
                    (oldList[oldItemPosition] as CategoryListItemModel.CategoryItemModel).category.id == (newList[newItemPosition] as CategoryListItemModel.CategoryItemModel).category.id
                } else {
                    false
                }
            }

            override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
                return if (oldList[oldPosition] is CategoryListItemModel.CategoryItemModel && newList[newPosition] is CategoryListItemModel.CategoryItemModel) {
                    //no need to check id, if id's is not the same this is different items
                    // also id's check is covered in areItemsTheSame() method
                    val name = (oldList[oldPosition] as CategoryListItemModel.CategoryItemModel).category.name
                    val name1 = (newList[newPosition] as CategoryListItemModel.CategoryItemModel).category.name
                    return name == name1
                } else {
                    false
                }
            }
        }
    }

}
