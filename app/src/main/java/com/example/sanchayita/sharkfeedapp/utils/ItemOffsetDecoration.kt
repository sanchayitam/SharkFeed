package com.example.sanchayita.sharkfeedapp.utils

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

//An ItemDecoration allows the application to add a special drawing and layout offset to specific item views from the adapter’s data set.
// This can be useful for drawing dividers between items, highlights, visual grouping boundaries and more
class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

    constructor(context: Context, itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId))

    override
    fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    )
    {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
    }
}