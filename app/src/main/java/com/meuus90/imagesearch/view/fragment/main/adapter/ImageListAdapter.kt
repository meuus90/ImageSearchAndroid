package com.meuus90.imagesearch.view.fragment.main.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.meuus90.imagesearch.R
import com.meuus90.imagesearch.base.view.util.BaseViewHolder
import com.meuus90.imagesearch.model.schema.image.ImageDoc
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_image.view.*

class ImageListAdapter(val doOnClick: (item: ImageDoc, sharedView: View) -> Unit) :
    PagingDataAdapter<ImageDoc, BaseViewHolder<ImageDoc>>(DIFF_CALLBACK) {
    companion object {
        private val PAYLOAD_TITLE = Any()

        private val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ImageDoc>() {
                override fun areItemsTheSame(oldItem: ImageDoc, newItem: ImageDoc): Boolean =
                    oldItem.databaseId == newItem.databaseId

                override fun areContentsTheSame(oldItem: ImageDoc, newItem: ImageDoc): Boolean =
                    oldItem == newItem
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ImageDoc> {
        val inflater = LayoutInflater.from(parent.context.applicationContext)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ImageItemHolder(view, this)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ImageDoc>, position: Int) {
        getItem(position)?.let { item ->
            holder.bindItemHolder(holder, item, position)
        }
    }

    class ImageItemHolder(
        override val containerView: View,
        private val adapter: ImageListAdapter
    ) : BaseViewHolder<ImageDoc>(containerView), LayoutContainer {
        val placeholderResList = listOf(
            R.drawable.placeholder0,
            R.drawable.placeholder1,
            R.drawable.placeholder2,
            R.drawable.placeholder3,
            R.drawable.placeholder4,
            R.drawable.placeholder5
        )

        override fun bindItemHolder(
            holder: BaseViewHolder<ImageDoc>,
            item: ImageDoc,
            position: Int
        ) {
            containerView.apply {
                val placeholderRes = placeholderResList[position % placeholderResList.size]

                val requestOption = RequestOptions()
                    .placeholder(placeholderRes).centerCrop()

                Glide.with(context).asDrawable().clone()
                    .load(item.thumbnail_url)
                    .apply(requestOption)
                    .fitCenter()
                    .error(R.drawable.ic_no_image)
                    .into(iv_thumbnail)

                tv_name.text =
                    if (item.display_sitename.isEmpty()) context.getString(R.string.item_web) else item.display_sitename

                v_root.setOnClickListener {
                    adapter.doOnClick(item, iv_thumbnail)
                }
            }
        }

        override fun onItemSelected() {
            containerView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            containerView.setBackgroundColor(0)
        }
    }
}