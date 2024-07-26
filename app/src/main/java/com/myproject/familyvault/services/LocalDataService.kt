package com.myproject.familyvault.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myproject.familyvault.utils.AppData
import java.io.File
import java.io.IOException

class LocalDataService() : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        getLocalData()
//        checkAppFolder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getLocalData(){
        Log.v("TAG","inside getLocalData()")
        if(checkAppFolder()){
            val mainFile = File(AppData.EXTERNAL_STORAGE_PUBLIC_DIRECTORY, AppData.appName)
            if (mainFile.exists() && mainFile.isDirectory) {
                val memberFolders = mainFile.list()
                if (memberFolders != null) {
                    AppData.membersList.addAll(memberFolders)
                    for(member in memberFolders){
//                        Log.v("TAG", "member list inside appFolder : $member");
                        val memberFile = File(AppData.EXTERNAL_STORAGE_PUBLIC_DIRECTORY,AppData.appName+"/"+member+"/")
                        if(memberFile.exists() && memberFile.isDirectory){
                            Log.v("TAG", "member File absulate path : ${memberFile.path}");
                            val memberFolderItems = memberFile.list()
                            Log.v("TAG", "memberFolderItems : ${memberFile.list()?.size}");
                            if (memberFolderItems != null) {
                                val memberItemsList : MutableList<String> = ArrayList()
                                for(memberItem in memberFolderItems){
                                    Log.v("TAG", "member Item : $memberItem");
                                    memberItemsList.add(memberItem.toString())
                                }
                                AppData.membersItemsMap[member] = memberItemsList
                            }
                        }
                    }
//                    Log.v("TAG","members list inside appFolder : "+AppData.membersList);
                    Log.v("TAG","members map : "+AppData.membersItemsMap)
                }
            }
        }
        val intent = Intent("BROADCAST_DEFAULT_ALBUM_CHANGED")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun checkAppFolder() : Boolean{
        try {
            val file = File(AppData.EXTERNAL_STORAGE_PUBLIC_DIRECTORY, AppData.appName)
            if (file.exists()) {
               /* file.bufferedReader().useLines {

                }*/

                Log.v("TAG","file is exist")
                Log.v("TAG","file is exist file path : "+file.path)
                return true
            }else{
                Log.v("TAG","file is not exist creating file")
               file.mkdirs()
                if(file.exists()){
                    Log.v("TAG","file is exist file path : "+file.path)
                    return true
                }
            }
        } catch (e: IOException) {
            Log.e("TAG","checkAppFolder exception : "+e.localizedMessage)
        }
        return false
    }
}