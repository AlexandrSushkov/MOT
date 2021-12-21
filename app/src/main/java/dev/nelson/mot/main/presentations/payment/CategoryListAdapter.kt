package dev.nelson.mot.main.presentations.payment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import dev.nelson.mot.main.data.room.model.category.CategoryEntity

class CategoryListAdapter(
    context: Context,
    @LayoutRes private val resourceLayout: Int,
    items: List<CategoryEntity>
) : ArrayAdapter<CategoryEntity>(context, resourceLayout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: createItemView()

        val categoryNameView = itemView.findViewById<TextView>(android.R.id.text1)
        categoryNameView?.text = getItem(position)?.name

        return itemView
    }

    private fun createItemView(): View = LayoutInflater.from(context).inflate(resourceLayout, null)
}