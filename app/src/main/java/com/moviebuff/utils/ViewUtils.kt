package com.moviebuff.utils

import android.content.Context
import android.util.Patterns
import android.view.View
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest

fun Context.toast(message :String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun View.setVisible(){
    this.visibility = View.VISIBLE
}
fun View.setGone(){
    this.visibility = View.GONE
}

