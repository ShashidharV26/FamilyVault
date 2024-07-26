package com.myproject.familyvault.utils

import android.os.Environment
import java.io.File

object AppData {

    val appName : String = "FamilyVault"
    var EXTERNAL_STORAGE_PUBLIC_DIRECTORY: File = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

    var membersList : MutableList<String> = ArrayList()
    var membersItemsMap : MutableMap<String,MutableList<String>> = HashMap()

}