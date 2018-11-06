package com.example.sanchayita.sharkfeedapp.util


import android.content.Context
import android.net.ConnectivityManager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.utils.ItemOffsetDecoration
import com.squareup.picasso.Picasso

//Load image into ImageView using Picassa Library
fun ImageView.loadFromFlickr(mFlickUrl: String){

    val mPicasso = Picasso.get()
        .load(mFlickUrl)
        .error(R.drawable.ic_image_black_24px)
        .placeholder(R.drawable.ic_image_black_24px)

    mPicasso.into(this)
}

//Initialise the recyclerview
fun RecyclerView.init(mOrientation: Int = GridLayoutManager.VERTICAL){

    this.setHasFixedSize(true)
    val llm = GridLayoutManager(this.context, 3)
    llm.orientation = mOrientation
    this.layoutManager = llm
    this.addItemDecoration(ItemOffsetDecoration(this.context, R.dimen.item_offset))
    this.itemAnimator = DefaultItemAnimator()

}

//Check internet connectivity
fun Context.isConnectingToInternet(): Boolean {

  val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as
            ConnectivityManager
    return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected

}



