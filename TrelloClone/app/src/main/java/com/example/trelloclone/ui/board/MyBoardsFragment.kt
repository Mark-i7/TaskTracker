package com.example.trelloclone.ui.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.CardsAdapter.OnItemClickListener
import com.example.trelloclone.adapter.CardsAdapter
import com.example.trelloclone.databinding.FragmentMyBoardsBinding
import com.example.trelloclone.models.BaseClass
import com.example.trelloclone.viewmodels.SharedViewModel

class MyBoardsFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentMyBoardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardsAdapter

    override fun onAttach(context : Context){
        super.onAttach(context)
        //callback
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_myBoardsFragment_to_nav_home)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBoardsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setupRecyclerView()
        sharedViewModel.boards.observe(viewLifecycleOwner){
            adapter.setData(sharedViewModel.boards.value as ArrayList<BaseClass>)
            adapter.notifyDataSetChanged()
        }
        setListeners()
        return root
    }

    private fun initializeElements() {
        recyclerView = binding.recyclerViewBoards
        recyclerView.setHasFixedSize(true)
    }

    private fun setupRecyclerView(){
        adapter = CardsAdapter(ArrayList<BaseClass>(), this, requireContext())
        recyclerView.adapter = adapter
    }

    private fun setListeners(){
        binding.addBoardButton.setOnClickListener{
            findNavController().navigate(R.id.action_myBoardsFragment_to_nav_create_board2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(id: String) {
        sharedViewModel.currentBoardId = id
        Log.i("currentBoardId", id)
        sharedViewModel.getListsForBoard(id)
        findNavController().navigate(R.id.action_myBoardsFragment_to_editBoardFragment)
    }
}