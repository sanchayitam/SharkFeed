package com.example.sanchayita.sharkfeedapp.main

import android.content.Context
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.network.ApiService
import com.example.sanchayita.sharkfeedapp.util.isConnectingToInternet
import com.example.sanchayita.sharkfeedapp.utils.Consts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


internal  class MainPresenter(private val context: Context,  private val mainView: MainView)  {

    val api: ApiService = ApiService()



    fun retrofitRequest(page : Int ,  mFromPagination: Boolean = false) {

        if(!mFromPagination){
            mainView.showWait()
        }

        var mError = false
        if (context.isConnectingToInternet()) {


            api.searchFlickr(Consts.query, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.photos.photo }

                .subscribe({ result ->
                        mainView.onFlickrSucces(result)

                }, { error ->

                        mainView.onFlickrError(error.localizedMessage)
                        mError = true
                })

            mainView.removeWait(mError)
            mainView.onRefreshFinish()
        }
        else {

            mainView.removeWait(true)
            mainView.onFlickrError(context.getString(R.string.no_internet_connection))

        }
    }


}
