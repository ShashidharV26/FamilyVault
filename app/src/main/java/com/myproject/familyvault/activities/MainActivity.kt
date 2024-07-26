package com.myproject.familyvault.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myproject.familyvault.R
import com.myproject.familyvault.SharedViewModel
import com.myproject.familyvault.fragments.hall.HallFragment
import com.myproject.familyvault.services.LocalDataService

class MainActivity : AppCompatActivity() {

    companion object{
        private const val REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 100
        private var permissionsList : MutableList<String> = ArrayList()

    }

    private lateinit var sharedViewModel: SharedViewModel
    private val tag : String = "MainActivity"


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getPermissions()


        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        sharedViewModel.dataCollected.observeForever{isCollected ->
            run {
                if(isCollected){

                }
            }
        }




    }

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                "BROADCAST_DEFAULT_ALBUM_CHANGED" -> goToFragment()
            }
        }
    }

    private fun goToFragment(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadCastReceiver)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, HallFragment()).commit()
    }



    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private fun getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // android 13 or more
            addPermissions(permissionsList, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            addPermissions(permissionsList, Manifest.permission.READ_MEDIA_IMAGES)
            addPermissions(permissionsList, Manifest.permission.CAMERA)
        }else{
            addPermissions(permissionsList,Manifest.permission.READ_EXTERNAL_STORAGE)
            addPermissions(permissionsList,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            addPermissions(permissionsList,Manifest.permission.CAMERA)
        }

        if (permissionsList.isNotEmpty()) {
            Log.v(tag,"permissionsList is not empty")
            ActivityCompat.requestPermissions(this, permissionsList.toTypedArray<String>(), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS)
        }else{
            Log.v(tag,"permissionsList is empty")
            getDataFromLocal()
        }
    }

    private fun addPermissions(permissionsList : MutableList<String>, permission : String) : Boolean{
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission)
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            val permissionMap: MutableMap<String, Int> = HashMap()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionMap[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] = PackageManager.PERMISSION_GRANTED
                permissionMap[Manifest.permission.READ_MEDIA_IMAGES] = PackageManager.PERMISSION_GRANTED
                permissionMap[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                for (i in permissions.indices) {
                    permissionMap[permissions[i]] = grantResults[i]

                    if (permissionMap[Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED] == PackageManager.PERMISSION_GRANTED) {
                        Log.v(tag, "Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED -- PERMISSION_GRANTED")
                    }
                    if (permissionMap[Manifest.permission.READ_MEDIA_IMAGES] == PackageManager.PERMISSION_GRANTED) {
                        Log.v(tag, "Manifest.permission.READ_MEDIA_IMAGES -- PERMISSION_GRANTED")
                    }
                    if (permissionMap[Manifest.permission.CAMERA] == PackageManager.PERMISSION_GRANTED) {
                        Log.v(tag, " Camera permission granted ")
                    }
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                permissionMap[Manifest.permission.READ_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                permissionMap[Manifest.permission.WRITE_EXTERNAL_STORAGE] = PackageManager.PERMISSION_GRANTED
                permissionMap[Manifest.permission.CAMERA] = PackageManager.PERMISSION_GRANTED

                for (i in permissions.indices) {
                    permissionMap[permissions[i]] = grantResults[i]
                }
            } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
            getDataFromLocal()
        }
    }

    private fun getDataFromLocal(){

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadCastReceiver, IntentFilter("BROADCAST_DEFAULT_ALBUM_CHANGED"))

        val intent= Intent(this,LocalDataService()::class.java)
        startService(intent)
    }


}