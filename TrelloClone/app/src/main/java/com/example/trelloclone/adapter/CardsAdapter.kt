package com.example.trelloclone.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.models.BaseClass
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.ui.board.MyBoardsFragment
import com.example.trelloclone.ui.cards.MyCardsFragment

class CardsAdapter(private var list: List<BaseClass>,
                   private val listener: Fragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(id: String)
    }

    inner class CardHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val cardName: TextView = itemView.findViewById(R.id.tv_card_name)
        private val dueDate : TextView = itemView.findViewById(R.id.tv_due_date)
        private val image : ImageView = itemView.findViewById(R.id.user_profile_image)
        fun bindCard(card: Card) {
            cardName.text = card.cardTitle
            dueDate.text = card.dueDate
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val currentPosition = this.adapterPosition
            if(listener is MyCardsFragment){
                listener.onItemClick(list[currentPosition].id)
            }
        }
    }

    inner class BoardTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val boardImageView: ImageView = itemView.findViewById(R.id.board_image_view)
        private val boardName : TextView = itemView.findViewById(R.id.tv_board_name)
        fun bindBoard(board: Board) {
            boardName.text = board.boardName
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val currentPosition = this.adapterPosition
            if(listener is MyBoardsFragment){
                listener.onItemClick(list[currentPosition].id)
            }
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

    // Update the list
    fun setData(newlist: ArrayList<BaseClass>){
        list = newlist
    }
}