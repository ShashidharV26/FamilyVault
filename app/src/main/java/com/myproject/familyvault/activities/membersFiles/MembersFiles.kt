package com.myproject.familyvault.activities.membersFiles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myproject.familyvault.R
import com.myproject.familyvault.utils.AppData

class MembersFiles : AppCompatActivity() {

    private lateinit var membersFilesRecyclerView : RecyclerView
    private lateinit var membersFilesRecyclerViewAdapter: MembersFilesRecyclerViewAdapter
    private var filesList : MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_members_files)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val selectedMember = intent.getStringExtra("selectedMember")

        Log.v("MembersFiles","Selected Member name $selectedMember")

        if(AppData.membersItemsMap.containsKey(selectedMember)){
            AppData.membersItemsMap[selectedMember]?.let { filesList.addAll(it) }
            Log.v("MembersFiles","files list {$filesList}")
        }

        membersFilesRecyclerView = findViewById(R.id.members_files_recyclerView)
        membersFilesRecyclerView.layoutManager = GridLayoutManager(this,2)
        if(filesList.isNotEmpty()){
            membersFilesRecyclerViewAdapter =
                selectedMember?.let { MembersFilesRecyclerViewAdapter(this,filesList, it) }!!
            membersFilesRecyclerView.adapter = membersFilesRecyclerViewAdapter
        }


    }
}