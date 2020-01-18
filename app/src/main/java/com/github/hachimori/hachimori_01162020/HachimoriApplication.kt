package com.github.hachimori.hachimori_01162020

import android.app.Application
import timber.log.Timber

class HachimoriApplication: Application() {

   override fun onCreate() {
       super.onCreate()

       if (BuildConfig.DEBUG) {
           Timber.plant(Timber.DebugTree())
       }
   }
}