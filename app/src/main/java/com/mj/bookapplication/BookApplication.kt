package com.mj.bookapplication

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BookApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("BookApplication", "onCreate()")
    }
}