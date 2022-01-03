package com.example.trelloclone.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentCreateBoardBinding
import com.example.trelloclone.models.Board
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.viewmodels.SharedViewModel
import com.google.android.material.textfield.TextInputLayout

class CreateBoard : Fragment() {

    private var _binding: FragmentCreateBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var boardName : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBoardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setListeners()
        return root
    }

    private fun initializeElements() {
        boardName = binding.etBoardName
    }

    private fun setListeners(){
        binding.btnCreate.setOnClickListener{
            val list = ArrayList<String>()
            list.add(AppLevelFunctions.getCurrentUserID())
            sharedViewModel.addBoard(Board("", boardName.editText!!.text.toString(), 0, list, 1))
            sharedViewModel.getAllBoardsCreatedByUser()
            findNavController().navigate(R.id.action_nav_create_board_to_myBoardsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}