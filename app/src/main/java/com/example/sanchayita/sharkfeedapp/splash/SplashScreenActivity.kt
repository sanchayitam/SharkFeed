package com.example.sanchayita.sharkfeedapp.splash

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.main.MainActivity
import com.example.sanchayita.sharkfeedapp.utils.Consts.Companion.splash_time

class  SplashScreenActivity : AppCompatActivity() , SplashContract.View {

    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        presenter = SplashPresenter(this, this)
        presenter.keepSplashWait()

        }

    override fun onSplashOpen() {

        if (!isFinishing) {

            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

