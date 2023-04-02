package com.miaekebom.mynotesapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    companion object{
        fun lcLog(className:Any, funcName:String){
            Log.d(
                "lifecycle",
                "Class: ${className::class.java.name} Func: $funcName}"
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        object {}.javaClass.enclosingMethod?.name?.let { lcLog(this, it) }
    }

    override fun onTerminate() {
        super.onTerminate()
        object {}.javaClass.enclosingMethod?.name?.let { lcLog(this, it) }
    }
}