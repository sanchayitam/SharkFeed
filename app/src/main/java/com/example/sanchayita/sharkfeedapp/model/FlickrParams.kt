package com.example.sanchayita.sharkfeedapp.model

class FlickrParams(val farm_id: Int, val server_id: String,
                  val id: String, val secret: String) {


    override fun toString(): String {

        return "http://farm${farm_id}.static.flickr.com/${server_id}/${id}_${secret}.jpg"

    }
}