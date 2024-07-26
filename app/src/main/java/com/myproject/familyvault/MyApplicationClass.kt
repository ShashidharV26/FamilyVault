package com.myproject.familyvault

import android.app.Application

class MyApplicationClass : Application() {

    companion object{
        lateinit var instance : MyApplicationClass

        fun getSessionInstance() : MyApplicationClass{
            return  instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}