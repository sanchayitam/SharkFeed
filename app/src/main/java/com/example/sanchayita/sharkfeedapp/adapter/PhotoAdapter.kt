package com.example.sanchayita.sharkfeedapp.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.model.FlickrParams
import com.example.sanchayita.sharkfeedapp.model.Photo
import com.example.sanchayita.sharkfeedapp.util.loadFromFlickr
import kotlinx.android.synthetic.main.photo_item.view.*

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
internal class PhotoAdapter(var photoList: MutableList<Photo>,
                            val itemClick: (Photo) -> Unit,
                            val searchCallback: (Int) -> Unit) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return ViewHolder(v, itemClick)

    }
    //Involves populating data into the item through holder
    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.bindPhoto(photoList.get(position))
        // if position is the last one, we need to request more
        if (position == photoList.size - 1) {
            // call onSearch
            searchCallback(getCurrentPage() + 1)
        }
    }

    // Returns the current page in the recyclerview
    fun getCurrentPage(): Int {

        return Math.ceil(itemCount / 100.0).toInt()

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {

        Log.d("Sharkfeed", "getItemCount called" + photoList.size)
        return photoList.size

    }

    // Provide a direct reference to each of the views within a data item
     //Used to cache the views within the item layout for fast access
    class ViewHolder(itemView: View, val itemClick: (Photo) -> Unit) : RecyclerView.ViewHolder(itemView) {

        // The holder should contain a member variable
        // for any view that will be set as you render a row
        val thumbnail: ImageView = itemView.findViewById(R.id.photoView)

        fun bindPhoto(photo: Photo) {
            Log.v("Sharkfeed", photo.uri)

            //check if thumbnail image available else medium image
            if(photo.url_t != null && photo.url_t.isNotEmpty()) {

                itemView.photoView.loadFromFlickr(photo.url_t)
            }
            else if (photo.url_c != null  && photo.url_c.isNotEmpty())

                itemView.photoView.loadFromFlickr(photo.url_c)
            else
            {

                itemView.photoView.loadFromFlickr(
                    FlickrParams(photo.farm, photo.server, photo.id,
                    photo.secret).toString())
            }

          itemView.setOnClickListener { itemClick(photo) }
       }
    }

    //set the data items in adapter
    fun setItems(newPhotos: List<Photo>) {

        photoList.clear()
        notifyDataSetChanged()
        addItems(newPhotos)
    }

    //add the data items in adapter
    fun addItems(newPhotos: List<Photo>) {

        newPhotos.forEach {
            photoList.add(it)
            notifyItemInserted(photoList.size - 1) // For one-by-one animations.
        }
    }
}