package com.francisumeanozie.baymax

import android.app.Application

class BaymaxApplication : Application() {

   
    override fun onCreate() {
        super.onCreate()

        mInstance = this
        //PreferencesHelper.initPreferences(this)
    }

    companion object {

        private lateinit var mInstance: BaymaxApplication

        @Synchronized
        fun getApp(): BaymaxApplication {
            return mInstance
        }
    }
    

}