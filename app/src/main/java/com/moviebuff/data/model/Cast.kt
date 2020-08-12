package com.moviebuff.data.model

data class Cast(
    var cast_id: Int,
    var character: String?,
    var credit_id: String,
    var gender: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var profile_path: String,
    var department: String? //for Crew
){
    fun getImageUrl():String{
        return "https://image.tmdb.org/t/p/w500$profile_path"
    }
}