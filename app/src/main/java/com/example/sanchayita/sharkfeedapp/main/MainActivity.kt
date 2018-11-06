package com.example.sanchayita.sharkfeedapp.main


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sanchayita.sharkfeedapp.imageViewer.ImageViewerActivity
import com.example.sanchayita.sharkfeedapp.R

import com.example.sanchayita.sharkfeedapp.utils.EndlessRecyclerViewScrollListener
import com.example.sanchayita.sharkfeedapp.adapter.PhotoAdapter

import com.example.sanchayita.sharkfeedapp.network.ApiService
import kotlinx.android.synthetic.main.activity_main.*
import com.example.sanchayita.sharkfeedapp.util.init
import com.example.sanchayita.sharkfeedapp.model.Photo


internal class MainActivity : AppCompatActivity (), MainView {

     private lateinit  var presenter : MainPresenter
     private val api: ApiService = ApiService()
     private val TAG = MainActivity::class.java.getSimpleName()
     private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
     private var mEndLessScrollListener: EndlessRecyclerViewScrollListener? = null
     private val PAGE_NUMBER = "PAGE_NUMBER"
     private var sPageNumber = 1

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mSwipeRefreshLayout = findViewById(R.id.swipe_container)
        val gridLayoutManager = GridLayoutManager(this, 3)

        setSupportActionBar(toolbar)
        toolbar.setTitle(R.string.app_name)

      //Endless scrolling implementation
        mEndLessScrollListener = object : EndlessRecyclerViewScrollListener(gridLayoutManager) {
            override
            fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                Log.d(TAG, "Load More Page Number : $page")
                sPageNumber = page
                onSearch(page)
            }
        }

        // recovering the instance state
        if(savedInstanceState != null) {

            sPageNumber = savedInstanceState.getInt(PAGE_NUMBER)
            mEndLessScrollListener!!.setCurrentPage(sPageNumber)

        }

         //set up the recyclerview
        setupPhotoRecyclerView()
        recycler_view.addOnScrollListener(mEndLessScrollListener as EndlessRecyclerViewScrollListener)

         presenter = MainPresenter(this, this);

         onSearch(sPageNumber, true)

        //Pull to refresh
        mSwipeRefreshLayout!!.setOnRefreshListener{

            onSearch(sPageNumber)

        }

        //setting color schema for the progress symbol
        mSwipeRefreshLayout!!.setColorSchemeResources(
            R.color.orange,
            R.color.green,
            R.color.blue
        )
     }

   override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.run {putInt(PAGE_NUMBER, sPageNumber)}

        super.onSaveInstanceState(outState, outPersistentState)
   }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {

        sPageNumber = savedInstanceState!!.getInt(PAGE_NUMBER)
        mEndLessScrollListener!!.setCurrentPage(sPageNumber)

    }

    private val photosAdapter: PhotoAdapter = PhotoAdapter(ArrayList(),
        { photo ->
            // launch imageviewer activity with this photo
                val intent = Intent(this, ImageViewerActivity::class.java)
               intent.putExtra("photo", photo)
               startActivity(intent)
        },
        { page -> onSearch(page) }
    )

    //initialise the recyclerview and set its adapter
    private fun setupPhotoRecyclerView() {

        recycler_view.init()
        recycler_view.adapter = photosAdapter

    }

    // send the search query to the Presenter
     fun onSearch(page: Int = 1 , mFromPagination : Boolean = false) {

         presenter.retrofitRequest(page , mFromPagination )

    }

// broadcast receiver to receive network changes
    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(ConnectivityManager
                .EXTRA_NO_CONNECTIVITY, false)
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }


    override fun onRefreshFinish() {
        mSwipeRefreshLayout!!.isRefreshing = false
    }

    //load the adapter with photos from the Presenter
    override fun onFlickrSucces(photos: List<Photo> , mFromPagination: Boolean) {
        if(mFromPagination || sPageNumber == 1 )
            photosAdapter.setItems(photos)
        else
            photosAdapter.addItems(photos)
    }

    //display error message if API errors
    override fun onFlickrError(message: String) {

        Toast.makeText(this,message,Toast.LENGTH_LONG)

    }

    //no wait after pages loaded in recyclerview
    override fun removeWait(mError: Boolean) {
        mSwipeRefreshLayout!!.isRefreshing = false
        if(mError) {

            recycler_view.visibility = View.GONE

        }
        else {

            recycler_view.visibility = View.VISIBLE
        }
    }

    //// wait for initialise loading of pages in recyclerview
    override fun showWait() {

        recycler_view.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter( "android.net.conn.CONNECTIVITY_CHANGE"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

// no internet connectivity
    private fun disconnected() {
        recycler_view.visibility = View.INVISIBLE
        imageView.visibility = View.VISIBLE
    }

    // on internet connectivity
    private fun connected() {
        recycler_view.visibility = View.VISIBLE
        imageView.visibility = View.INVISIBLE
       onSearch(sPageNumber)
    }
}


