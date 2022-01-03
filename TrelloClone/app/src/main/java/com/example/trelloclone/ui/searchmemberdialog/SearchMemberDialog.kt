package com.example.trelloclone.ui.searchmemberdialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.MemberListAdapter
import com.example.trelloclone.models.User

abstract class SearchMemberDialog(
    context: Context,
    private var list: ArrayList<User>,
    private val title: String = ""
) : Dialog(context) {

    private var adapter: MemberListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState ?: Bundle())

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_search_member, null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)
    }

    private fun setUpRecyclerView(view: View) {
        view.findViewById<TextView>(R.id.tvTitle).text = title

        if (list.size > 0) {
            val recyclerView = view.findViewById<RecyclerView>(R.id.rvList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = MemberListAdapter(context, list)
            recyclerView.adapter = adapter

        }
    }

    protected abstract fun onItemSelected(user: User, action: String)

}