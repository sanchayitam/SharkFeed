package com.example.sanchayita.sharkfeedapp.model

import java.io.Serializable

internal data class Photo(val id: String,
                  val owner: String,
                  val secret: String,
                  val server: String,
                  val farm: Int,
                  val title: String,
                  val ispublic: Int,
                  val isfriend: Int,
                  val isfamily: Int,
                  val url_t:String,
                  val url_c:String,
                  val url_l:String,
                  val url_o:String,
                  val url_s :String
                  ) : Serializable {

    val uri: String
        get() = "http://farm${farm}.static.flickr.com/${server}/${id}_${secret}.jpg"

}