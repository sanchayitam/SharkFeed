package com.example.sanchayita.sharkfeedapp.imageViewer

class ImageViewerContract {

    interface View {

        fun onDetailOpen(title: String, imageUrl: String)

    }

    interface Presenter {

        fun getStringExtra()

        fun getFlickrUrl() :String

        fun getImageUrlLarge() :String

        fun getImageTitle() :String

    }
}