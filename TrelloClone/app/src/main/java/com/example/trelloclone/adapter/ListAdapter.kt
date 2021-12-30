package com.example.trelloclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R

class ListAdapter(
    private var list: ArrayList<String?>
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    // 1. user defined ViewHolder type - Embedded class!
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text_view_list_item)
    }

    // 2. Called only a few times = number of items on screen + a few more ones
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(itemView)
    }

    // 3. Called many times, when we scroll the list
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = list[position]
        holder.textView.text = currentItem
    }

    override fun getItemCount() = list.size
}