package com.francisumeanozie.baymax.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.francisumeanozie.baymax.modules.base.BaseActivity
import com.francisumeanozie.baymax.modules.main.HomeActivity

class SplashActivity : BaseActivity() {

    private val SPLASH_TIME_OUT:Long = 600 // 1 sec
    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_splash)

        startHomeActivity()
    }

    //endregion

    //region Private

    private fun startHomeActivity() {

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app main activity

            startActivity(Intent(this, HomeActivity::class.java))

            // close this activity
            finish()
        }, SPLASH_TIME_OUT)

    }

    //endregion
}