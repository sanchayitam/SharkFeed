package com.example.sanchayita.sharkfeedapp.network


import com.example.sanchayita.sharkfeedapp.model.FlickrResponse
import  com.example.sanchayita.sharkfeedapp.model.PhotoInfo
import com.example.sanchayita.sharkfeedapp.utils.Consts.Companion.API_KEY
import com.example.sanchayita.sharkfeedapp.utils.Consts.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {
    private val flickrApi: FlickrApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getHttpClient())
            .build()

        flickrApi = retrofit.create(FlickrApi::class.java)
    }

    //calls API interface for search query
    internal fun searchFlickr(text: String, page: Int): Observable<FlickrResponse> {

        return flickrApi.search(text, page)
    }

    ////calls API interface for img details
    internal fun getFlickrPhoto(photoId: String): Observable<PhotoInfo> {

        return flickrApi.getFlickrPhotoInfo(photoId)
    }

    //To add an interceptor, you have to use the okhttp3.OkHttpClient.Builder.addInterceptor(Interceptor) method on the OkHttp Builder.
    private fun getHttpClient(): OkHttpClient {

        return OkHttpClient.Builder().addInterceptor { chain ->

            val newUrl = chain.request().url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val newRequest = chain.request().newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }.build()
    }
}