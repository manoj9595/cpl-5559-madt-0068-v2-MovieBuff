package com.moviebuff.data.repositories

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import com.moviebuff.ui.login.LoginActivity


private const val PREF_KEY_EMAIL_ID = "PREF_KEY_EMAIL_ID"
private const val PREF_NAME = "AUTH_PREF"
class AppPreferencesHelper(val context: Context) {

    private var mPrefs: SharedPreferences? = null

    init {
        mPrefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }


    fun getSavedEmailId(): String? {
        return mPrefs!!.getString(PREF_KEY_EMAIL_ID, null)
    }

    fun setEmailId(string: String){
        return mPrefs!!.edit().putString(PREF_KEY_EMAIL_ID, string).apply()
    }

    fun getValueBoolean(key:String) : Boolean{
        return mPrefs!!.getBoolean(key, false)

    }
    fun getValueString(key:String) : String{
        return mPrefs!!.getString(key, "")!!
    }

    fun setValueBoolean(key: String,value : Boolean){
        mPrefs!!.edit().putBoolean(key, value).apply()

    }

    fun setValueString(key: String,value : String){
        mPrefs!!.edit().putString(key, value).apply()
    }


    fun clearPrefData(){
        mPrefs!!.edit().clear().apply()
    }

    fun logoutUser(){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                clearPrefData()
                dialog?.dismiss()
                val intent = Intent(context, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                context.startActivity(intent)
            }
        })
        builder.setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.dismiss()
            }

        })
        val alertDialog = builder.create()
        alertDialog.show()
    }


}