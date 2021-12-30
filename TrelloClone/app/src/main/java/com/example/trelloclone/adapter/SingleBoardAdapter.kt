package com.example.trelloclone.adapter

import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.example.trelloclone.R
import com.example.trelloclone.models.Card
import com.example.trelloclone.models.BoardList
import com.example.trelloclone.utils.AppLevelFunctions

class SingleBoardAdapter(
    private var list: ArrayList<BoardList>,
    private var cardList: ArrayList<Card>
) : RecyclerView.Adapter<SingleBoardAdapter.SingleBoardViewHolder>() {

    private val viewPool = RecycledViewPool()

    // 1. user defined ViewHolder type - Embedded class!
    inner class SingleBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnKeyListener {
        val textView: TextView = itemView.findViewById(R.id.tv_edit_board_item)
        val cardNameTextView: TextView = itemView.findViewById(R.id.et_add_card)
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view_edit_board_item)
        val button: Button = itemView.findViewById(R.id.btn_add_card_board_item)

        init {
            button.setOnClickListener(this)
            cardNameTextView.setOnKeyListener(this)
        }

        override fun onClick(v: View?) {
            Log.i("click", v?.id.toString())
            when (v?.id) {
                button.id -> cardNameTextView.visibility = View.VISIBLE
            }
        }

        override fun onKey(view: View?, i: Int, keyEvent: KeyEvent?): Boolean {
            when (view?.id) {
                cardNameTextView.id -> {
                    if (keyEvent!!.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                        val currentPosition = this.adapterPosition
                        /** Create a card with the title specified by user */
                        val card = Card("", "", list[currentPosition].listName, AppLevelFunctions.getCurrentUserID(), ArrayList<String>(), cardNameTextView.text.toString(),
                            "", "", "","", 0, "", "", 0)
                        cardList.add(card)
                        notifyDataSetChanged()
                        cardNameTextView.text = ""
                        cardNameTextView.visibility = View.GONE
                        return true
                    }
                }
            }
            return false
        }
    }

    // 2. Called only a few times = number of items on screen + a few more ones
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleBoardViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.board_item, parent, false)
        return SingleBoardViewHolder(itemView)
    }

    // 3. Called many times, when we scroll the list
    override fun onBindViewHolder(holder: SingleBoardViewHolder, position: Int) {
        val currentItem = list[position]
        holder.textView.text = currentItem.listName
        holder.recyclerView.setHasFixedSize(true)
        val listNamesList = ArrayList<String?>()
        cardList.filter { it.listName == currentItem.listName }.forEach { listNamesList.add(it.cardTitle) }
        holder.recyclerView.adapter = ListAdapter(listNamesList)
        holder.recyclerView.setRecycledViewPool(viewPool)
    }

    override fun getItemCount() = list.size
}