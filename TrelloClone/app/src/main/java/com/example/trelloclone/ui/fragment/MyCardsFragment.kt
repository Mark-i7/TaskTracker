package com.example.trelloclone.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.RecyclerViewAdapter
import com.example.trelloclone.databinding.FragmentMyCardsBinding
import com.example.trelloclone.adapter.RecyclerViewAdapter.OnItemClickListener
import com.example.trelloclone.models.BaseClass
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.Card
import com.example.trelloclone.utils.Constants

class MyCardsFragment : Fragment(), OnItemClickListener {

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

    private fun initializeElements() {
        recyclerView = binding.recyclerView
        val card1 =
            Card(
                "1", Constants.CURRENT_USER_ID, ArrayList(), "First commit", "12 Dec",
                "16 Dec", 1, "Details", "Description", 0
            )
        val card2 =
            Card(
                "2", Constants.CURRENT_USER_ID, ArrayList(), "Splash screen", "13 Oct",
                "19 Dec", 2, "Details", "Description", 0
            )
        val card3 =
            Card(
                "3", Constants.CURRENT_USER_ID, ArrayList(), "TestCard", "01 Oct",
                "31 Dec", 3, "Details", "Description", 0
            )
        val board1 = Board("1", "Szoftver Projekt", 1, arrayListOf(card1),1)
        val board2 = Board("2", "Test", 2, arrayListOf(card2, card3), 1)
        val list = mutableListOf<BaseClass>()
        list.add(board1)
        list.add(card1)
        list.add(board2)
        list.add(card2)
        list.add(card3)
        recyclerView.adapter = RecyclerViewAdapter(list, this)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(id: String) {
        findNavController().navigate(R.id.action_nav_my_card_to_cardDetailFragment)
    }

    fun cardAddedSuccess() {
        Toast.makeText(requireContext(), "Card successfully added", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(id: Int) {
        TODO("It's a problem because onItemClick needs int id but we now have string")
    }
}