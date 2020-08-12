package com.moviebuff.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.moviebuff.MainActivity
import com.moviebuff.R
import com.moviebuff.data.repositories.AppPreferencesHelper
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed(Runnable {
            if(getLoginStatus()){
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
            }else{
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
            }
        },1000)


        val aniFade: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)

        ivSplash.startAnimation(aniFade)
    }

    private fun getLoginStatus() : Boolean {
        return !AppPreferencesHelper(this).getSavedEmailId().isNullOrEmpty()
    }
}