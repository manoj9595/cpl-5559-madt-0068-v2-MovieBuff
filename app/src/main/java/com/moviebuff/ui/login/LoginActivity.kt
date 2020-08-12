package com.moviebuff.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.moviebuff.MainActivity
import com.moviebuff.R
import com.moviebuff.data.repositories.AppPreferencesHelper
import com.moviebuff.db.AppDataBase
import com.moviebuff.utils.Coroutines
import com.moviebuff.utils.md5
import com.moviebuff.utils.toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginClicked(view: View) {
        val username = etUserName.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if(username.isEmpty()){
            toast("Enter Email Id")
            return
        }
        if(password.length<8){
            toast("Enter Password")
            return
        }


        checkLogin(username,password.md5())

    }

    private fun checkLogin(username: String, password: String) {

        Coroutines.io {
            val user  = AppDataBase.getAppDataBase(this)?.userDao()?.authenticateUser(username,password)
            if(user==null){
                Coroutines.main {
                    toast("Invalid UserName or Password")
                }
            }else{
                AppPreferencesHelper(this).setEmailId(username)
                Coroutines.main {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                }
            }
        }
    }

    fun onSignUpClicked(view: View) {
        startActivity(Intent(this,SignUpActivity::class.java))
    }
}