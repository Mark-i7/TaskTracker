package com.example.trelloclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.model.BaseClass
import com.example.trelloclone.model.Board
import com.example.trelloclone.model.Card

class RecyclerViewAdapter(private val list : List<BaseClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardName: TextView = itemView.findViewById(R.id.tv_card_name)
        private val dueDate : TextView = itemView.findViewById(R.id.tv_due_date)
        private val image : ImageView = itemView.findViewById(R.id.user_profile_image)
        fun bindCard(card: Card) {
            cardName.text = card.cardName
            dueDate.text = card.dueDate
        }
    }

    inner class BoardTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val boardImageView: ImageView = itemView.findViewById(R.id.board_image_view)
        private val boardName : TextView = itemView.findViewById(R.id.tv_board_name)
        fun bindBoard(board: Board) {
            boardName.text = board.boardName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        lateinit var holder: RecyclerView.ViewHolder

        if(viewType == 0){
            holder = CardHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false))
        }

        if(viewType == 1){
            holder = BoardTitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.board_title_item, parent, false))
        }

        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CardHolder){
            val currentItem = list[position] as Card
            holder.bindCard(currentItem)
        }

        if(holder is BoardTitleHolder){
            val currentItem = list[position] as Board
            holder.bindBoard(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }
}