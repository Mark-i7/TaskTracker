package com.example.trelloclone.ui.cards

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.CardsAdapter
import com.example.trelloclone.databinding.FragmentMyCardsBinding
import com.example.trelloclone.adapter.CardsAdapter.OnItemClickListener
import com.example.trelloclone.models.BaseClass
import com.example.trelloclone.viewmodels.SharedViewModel

class MyCardsFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentMyCardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyCardsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setupRecyclerView()
        sharedViewModel.cards.observe(viewLifecycleOwner){
            adapter.setData(sharedViewModel.cards.value as ArrayList<BaseClass>)
            adapter.notifyDataSetChanged()
        }
        return root
    }

    private fun initializeElements() {
        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
    }

    private fun setupRecyclerView(){
        adapter = CardsAdapter(ArrayList<BaseClass>(), this)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(id: String) {
        sharedViewModel.currentCardId = id
        findNavController().navigate(R.id.action_nav_my_card_to_cardDetailFragment)
    }

    fun cardAddedSuccess() {
        Toast.makeText(requireContext(), "Card successfully added", Toast.LENGTH_SHORT).show()
    }
}