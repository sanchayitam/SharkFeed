package com.example.sanchayita.sharkfeedapp.splash

import android.widget.ProgressBar


class SplashContract {

    interface View {

        fun onSplashOpen()
    }

    interface Presenter {

        fun keepSplashWait()
    }
}