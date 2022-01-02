package com.example.trelloclone.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trelloclone.R
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants

class MemberListAdapter(
    private val context: Context,
    private var list: ArrayList<User>
) : RecyclerView.Adapter<MemberListAdapter.MemberViewHolder>() {

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvMemberName = view.findViewById<TextView>(R.id.tv_member_name)
        var tvMemberEmail = view.findViewById<TextView>(R.id.tv_member_email)
        var ivMemberPic = view.findViewById<ImageView>(R.id.iv_member_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberListAdapter.MemberViewHolder {
        return MemberViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_member,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val model = list[position]
        Log.d("members", model.name!!)
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_circle_profile)
                .into(holder.ivMemberPic)

            holder.tvMemberName.text = model.name
            holder.tvMemberEmail.text = model.email
    }

    override fun getItemCount(): Int {
        return list.size
    }
}