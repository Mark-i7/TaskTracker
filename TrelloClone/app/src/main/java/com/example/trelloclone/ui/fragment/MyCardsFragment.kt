package com.example.trelloclone.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.adapter.RecyclerViewAdapter
import com.example.trelloclone.databinding.FragmentMyCardsBinding
import com.example.trelloclone.model.BaseClass
import com.example.trelloclone.model.Board
import com.example.trelloclone.model.Card

class MyCardsFragment : Fragment() {

    private var _binding: FragmentMyCardsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCardsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        return root
    }

    private fun initializeElements(){
        recyclerView = binding.recyclerView
        val board1 = Board("Szoftver Projekt", 1, 1)
        val board2 = Board("Test", 2, 1)
        val card1 = Card("First commit","12 Dec",1, 0)
        val card2 = Card("Splash screen","13 Oct",2, 0)
        val card3 = Card("TestCard","01 Oct",3, 0)
        val list = mutableListOf<BaseClass>()
        list.add(board1)
        list.add(card1)
        list.add(board2)
        list.add(card2)
        list.add(card3)
        recyclerView.adapter = RecyclerViewAdapter(list)
        recyclerView.setHasFixedSize(true)
    }
}