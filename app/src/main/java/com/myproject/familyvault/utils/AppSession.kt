package com.myproject.familyvault.utils

import android.content.Context
import android.content.SharedPreferences
import com.myproject.familyvault.MyApplicationClass

class AppSession private constructor(myApplicationClass: MyApplicationClass){

    companion object{
   private var appSession : AppSession? = null

fun getInstance() : AppSession{
return appSession ?: run {
    AppSession(MyApplicationClass.getSessionInstance())
}
}
    }

    private val myApp = "vault"
    private val myApplicationClass : MyApplicationClass? = myApplicationClass
    private var shr : SharedPreferences? = null

    private fun getPrefer(): SharedPreferences {
        return shr ?: run {
            myApplicationClass?.getSharedPreferences(myApp, Context.MODE_PRIVATE)
                ?.also { shr = it } ?: throw IllegalStateException("ApplicationClass is null")
        }
    }
}