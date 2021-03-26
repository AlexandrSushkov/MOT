package dev.nelson.mot.main.presentations.movieslist

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseListAdapter(@LayoutRes private val layout: Int,
                           private val count: Int,
                           private val holderBindingListener: HolderBinding)
    : RecyclerView.Adapter<DataBoundViewHolder<*>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<*> =
            DataBoundViewHolder.create<ViewDataBinding>(parent, layout)

    override fun onBindViewHolder(holder: DataBoundViewHolder<*>, position: Int) = holderBindingListener.customize(holder)

    override fun getItemCount() = count

    interface HolderBinding {
        fun customize(vh: DataBoundViewHolder<*>)
    }
}