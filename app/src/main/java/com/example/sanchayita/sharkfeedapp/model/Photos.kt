package com.example.sanchayita.sharkfeedapp.model

internal data class Photos(val page: Int, val pages: Int, val perpage: Int, val total: String, val photo: List<Photo>)
