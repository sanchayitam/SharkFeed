package com.example.sanchayita.sharkfeedapp.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.v4.os.HandlerCompat.postDelayed
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.widget.ProgressBar
import android.app.Activity
import android.os.Handler
import com.example.sanchayita.sharkfeedapp.R
import com.example.sanchayita.sharkfeedapp.utils.Consts
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView


class SplashPresenter(private val context: Activity, private val splashView: SplashContract.View) :
    SplashContract.Presenter {

    private var mDelayHandler: Handler? = null

    internal val mRunnable: Runnable = Runnable {

        splashView.onSplashOpen()
    }

    override
    fun keepSplashWait() {

        //Initialize the Handler
        mDelayHandler = Handler()

        val sharkFeedLogo = context.findViewById(R.id.shark_feed_logo_image_view) as ImageView

        //set animation for shark logo
        sharkFeedLogo.setTranslationX(-1000f)
        sharkFeedLogo.animate()
            .rotationBy(360f)
            .translationXBy(1000f)
            .setDuration(3000)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }


                }
            })
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable , Consts.splash_time)

    }
}