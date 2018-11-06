package com.example.sanchayita.sharkfeedapp.imageViewer

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sanchayita.sharkfeedapp.util.loadFromFlickr
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.service.DownloadImageService
import kotlinx.android.synthetic.main.activity_image_viewer.*

class ImageViewerActivity : AppCompatActivity(), ImageViewerContract.View {

    var presenter: ImageViewerPresenter? = null
    lateinit var mFlickrUrl : String
    lateinit var mImageUrlLarge :String
    lateinit var mImageTitle: String

    companion object {

        //Permision code that will be checked in the method onRequestPermissionsResult
        val STORAGE_PERMISSION_CODE = 21
        val TAG = ImageViewerActivity::class.java.getSimpleName()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

       // val photo: Photo = intent.getSerializableExtra("photo") as Photo
            presenter = ImageViewerPresenter(this, this)

            presenter!!.getStringExtra()

            mFlickrUrl = presenter!!.getFlickrUrl()

            mImageUrlLarge = presenter!!.getImageUrlLarge()

            mImageTitle = presenter!!.getImageTitle()

        // open the image in web browser
        openInApp.setOnClickListener {

            try {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mImageUrlLarge))
                startActivity(browserIntent)

            } catch (e: Exception) {

                Toast.makeText(this, getString(R.string.url_err), Toast.LENGTH_LONG)
                Log.e(TAG, getString(R.string.url_err))

            }
        }

        //download the image file on the phone
        downloadButton.setOnClickListener {

            if (checkPermission()) {

                startDownload()

            } else {

                Log.v(TAG, "Permission to Write onto External Storage denied")
                //If the app has not the permission then asking for the permission
                requestStoragePermission()

            }
        }
    }

    //Load the image using Picassa library
    override
    fun onDetailOpen(title: String, imageUrl: String) {

        imageDetail.loadFromFlickr(imageUrl)
        imageDescription.setText( title)

    }

    private fun startDownload() {
        val download_intent = Intent(this, DownloadImageService::class.java)
        // add infos for the service which file to download and where to store
        Log.v("Download Intent url" ,mFlickrUrl)
        download_intent.putExtra(DownloadImageService.URL, mFlickrUrl)
        Log.i(TAG, "Starting the Download Service : downloading ")

        startService(download_intent)

    }

    //Broadcast receiver to receive downloa notifications
    private val receiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            val bundle = intent.extras
            if (bundle != null) {

                val string = bundle.getString(DownloadImageService.FILEPATH)
                val resultCode = bundle.getInt(DownloadImageService.RESULT)

                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(
                            applicationContext,
                            "Download complete. Download URI: " + string!!,
                            Toast.LENGTH_LONG
                        ).show()
                        Log.v(TAG, "Download complete. Download URI: $string")

                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(
                            applicationContext, "Download failed",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.v(TAG, "Download failed")
                    }

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        Log.i(TAG, "Registering the Download Service")
        registerReceiver(receiver, IntentFilter(DownloadImageService.NOTIFICATION))

    }

    override fun onPause() {
        super.onPause()

        Log.i(TAG, "Un-Registering the Download Service")
        unregisterReceiver(receiver)

    }

    // Request user to grant write external storage permission.
    private fun checkPermission(): Boolean {

        val result = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        // ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        return  (result == PackageManager.PERMISSION_GRANTED)

    }


    //Requesting permission
    private fun requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Toast.makeText(this, "Permission required to write into the storage", Toast.LENGTH_LONG).show()

        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    //This method will be called when the user will tap on allow or deny
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can write into the storage", Toast.LENGTH_LONG).show()

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show()
            }
        }
    }
}
