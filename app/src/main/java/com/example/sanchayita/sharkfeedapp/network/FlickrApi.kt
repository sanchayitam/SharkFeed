package com.example.sanchayita.sharkfeedapp.network

import com.example.sanchayita.sharkfeedapp.model.FlickrResponse
import com.example.sanchayita.sharkfeedapp.model.PhotoInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

internal interface FlickrApi {

    //Define the REST API for Retrofit  to search query
    @GET("?format=json&nojsoncallback=1&method=flickr.photos.search&extras=url_s,url_t,url_c,url_l,url_o")
    fun search(@Query("text") text: String, @Query("page") page: Int)
            : Observable<FlickrResponse>

    // //Define the REST API for Retrofit  to get img details
    @GET("?format=json&nojsoncallback=1&method=flickr.photos.getInfo&format=json&nojsoncallback=1")
    fun getFlickrPhotoInfo(@Query("photo_id") photoId: String
    ): Observable<PhotoInfo>
}