package com.example.sanchayita.sharkfeedapp.main

import com.example.sanchayita.sharkfeedapp.model.Photo

 internal  interface MainView {

     fun showWait()

     fun removeWait(mError: Boolean)

    fun onFlickrSucces(photos: List<Photo> , mFromPagination: Boolean = false)

    fun onFlickrError(message : String)

    fun onRefreshFinish()

 }


