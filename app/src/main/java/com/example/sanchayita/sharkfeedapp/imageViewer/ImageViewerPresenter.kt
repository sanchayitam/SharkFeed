package com.example.sanchayita.sharkfeedapp.imageViewer


import android.app.Activity
import com.example.sanchayita.sharkfeedapp.model.Photo
import com.example.sanchayita.sharkfeedapp.model.FlickrParams

class ImageViewerPresenter(private val context: Activity, private val detailView: ImageViewerContract.View) :
    ImageViewerContract.Presenter {

    //gets the title and image url from Model
    override
    fun getStringExtra() {

        val title = getImageTitle()
        val url = getImageUrlLarge()
        detailView.onDetailOpen(title, url)

    }

    //returns original image else medium image to the view
    override fun getFlickrUrl() :String {

        val photo: Photo = context.intent.getSerializableExtra("photo") as Photo

        //load original images (if available)
        if (photo.url_o != null && photo.url_o.isNotEmpty()) {

            return photo.url_o
        }
        else if (photo.url_c != null  && photo.url_c.isNotEmpty())   //load low definition images

           return photo.url_c
        else {

            return FlickrParams(photo.farm, photo.server, photo.id, photo.secret).toString()
        }
    }

    //return high definition image to view
    override fun getImageUrlLarge() :String {

        val photo: Photo = context.intent.getSerializableExtra("photo") as Photo

        if (photo.url_l != null && photo.url_l.isNotEmpty()) {
            return photo.url_l
        }
        else {

            return FlickrParams(photo.farm, photo.server, photo.id, photo.secret).toString()
        }
    }

//gets title of image from Model
    override fun getImageTitle() :String{

        val photo: Photo = context.intent.getSerializableExtra("photo") as Photo
        return photo.title
    }
}