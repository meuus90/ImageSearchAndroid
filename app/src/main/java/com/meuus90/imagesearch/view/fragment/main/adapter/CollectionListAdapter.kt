package com.meuus90.imagesearch.view.fragment.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.model.schema.image.CollectionItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_collection.view.*

class CollectionListAdapter(
    private val onItemClickListener: (collection: String?) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val ALL = "all"
    }

    class CollectionDiffCallback(
        private val oldList: List<CollectionItem>,
        private val newList: List<CollectionItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    private var collections: ArrayList<CollectionItem> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_collection, viewGroup, false)

        return ItemHolder(view)
    }

    override fun getItemCount(): Int {
        return collections.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemHolder) {
            val col = collections[position]

            holder.containerView.tv_collection.isSelected = col.isSelected

            holder.containerView.tv_collection.text = col.collection ?: ALL

            holder.containerView.tv_collection.setOnClickListener {
                onItemClickListener(col.collection)
            }
        }
    }

    fun setList(newList: List<CollectionItem>) {
        val diffCallback = CollectionDiffCallback(collections, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.collections.clear()
        this.collections.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    class ItemHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer
}