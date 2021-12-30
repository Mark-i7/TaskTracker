package com.example.trelloclone.adapter

import android.content.Context
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
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    inner class MemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvUserName = view.findViewById<TextView>(R.id.tv_user_name)
        var tvUserEmail = view.findViewById<TextView>(R.id.tv_user_mail)
        var ivUserPic = view.findViewById<ImageView>(R.id.iv_member_image)
        var ivMemberSelected = view.findViewById<ImageView>(R.id.iv_selected_member)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, user: User, action: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MemberViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_member,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MemberViewHolder) {

            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_circle_profile)
                .into(holder.ivUserPic)

            holder.tvUserName.text = model.name
            holder.tvUserEmail.text = model.email
            if(model.selected){
                holder.ivMemberSelected.visibility = View.VISIBLE
            } else {
                holder.ivMemberSelected.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    if(model.selected){
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    }else{
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}