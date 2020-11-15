package com.meuus90.imagesearch.base.view.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class GridSpacingItemDecoration(
    private var spanCount: Int,
    private var spacing: Int,
    private var includeEdge: Boolean
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val spanIndex =
            (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        if (includeEdge) {
            if (spanIndex == 0) {
                outRect.right = spacing / spanCount
            } else {
                outRect.left = spacing / spanCount
            }

            if (position < spanCount) {
                outRect.top = spacing
            }

            outRect.bottom = spacing
        }
    }
}