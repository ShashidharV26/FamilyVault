package com.myproject.familyvault.fragments.hall

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myproject.familyvault.R
import com.myproject.familyvault.utils.AppData

class HallFragment : Fragment() {

    private lateinit var membersRecyclerView : RecyclerView
    private lateinit var membersRecyclerViewAdapter: MembersRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_hall, container, false)

        initializeViews(view)
        Log.v("TAG","members list : "+AppData.membersList)
        membersRecyclerView.layoutManager = LinearLayoutManager(context);
        membersRecyclerViewAdapter= MembersRecyclerViewAdapter(requireContext(),AppData.membersList)
        membersRecyclerView.adapter=membersRecyclerViewAdapter

        return view
    }

    private fun initializeViews(view: View) {
        membersRecyclerView = view.findViewById(R.id.members_list_recyclerView)

    }


}