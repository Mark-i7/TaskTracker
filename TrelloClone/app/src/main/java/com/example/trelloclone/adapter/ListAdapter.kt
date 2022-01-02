package com.example.trelloclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.ui.board.EditBoardFragment

class ListAdapter(
    private var list: ArrayList<Pair<String, String?>>,
    private val listener: EditBoardFragment
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    // 1. user defined ViewHolder type - Embedded class!
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        val textView: TextView = itemView.findViewById(R.id.text_view_list_item)

        override fun onClick(v: View?) {
            val currentPos = adapterPosition
            listener.onItemClick(list[currentPos].first)
        }
    }

    // 2. Called only a few times = number of items on screen + a few more ones
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(itemView)
    }

    // 3. Called many times, when we scroll the list
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = list[position]
        holder.textView.text = currentItem.second
    }

    override fun getItemCount() = list.size

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }
}