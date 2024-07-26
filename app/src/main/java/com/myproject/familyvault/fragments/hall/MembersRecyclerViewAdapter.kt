package com.myproject.familyvault.fragments.hall

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.myproject.familyvault.R
import com.myproject.familyvault.activities.membersFiles.MembersFiles
import com.myproject.familyvault.models.Member

internal class MembersRecyclerViewAdapter(private var context : Context, private var memberList:MutableList<String>) :
    RecyclerView.Adapter<MembersRecyclerViewAdapter.MemberViewHolder>() {
      internal inner class MemberViewHolder(view : View) : RecyclerView.ViewHolder(view) {
           val memberImg : ImageView = view.findViewById(R.id.member_img)
          val memberName : TextView = view.findViewById(R.id.member_name)
          val memberCard : CardView = view.findViewById(R.id.member_card)
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MembersRecyclerViewAdapter.MemberViewHolder {
       val view = LayoutInflater.from(context).inflate(R.layout.custom_members_list_layout,parent,false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MembersRecyclerViewAdapter.MemberViewHolder, position: Int) {
        val member = memberList[position]

        holder.memberName.text = member

        holder.memberCard.setOnClickListener(View.OnClickListener {
          val intent = Intent(context,MembersFiles::class.java)
            intent.putExtra("selectedMember",member)
            context.startActivity(intent)
        })
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}