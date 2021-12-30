package com.example.trelloclone.ui.board

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.adapter.SingleBoardAdapter
import com.example.trelloclone.databinding.FragmentEditBoardBinding
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.BoardList
import com.example.trelloclone.models.Card
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.viewmodels.SharedViewModel

class EditBoardFragment : Fragment() {

    private var _binding: FragmentEditBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var currentBoard: Board //TODO
    private lateinit var recyclerView: RecyclerView
    private lateinit var button : Button
    private lateinit var editText: EditText
    private lateinit var adapter: SingleBoardAdapter
    private var boardListList = ArrayList<BoardList>()
    private var cardList = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditBoardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setListeners()
        setupRecyclerview()
        setValues()
        return root
    }

    private fun initializeElements(){
        recyclerView = binding.recyclerViewEditBoard
        button = binding.btnAddList
        editText = binding.etAddList
    }

    private fun setupRecyclerview(){
        adapter = SingleBoardAdapter(boardListList, cardList)
        recyclerView.adapter = adapter
    }

    private fun setListeners() {
        button.setOnClickListener {
            editText.visibility = View.VISIBLE
            editText.setText("")
        }

        editText.setOnKeyListener { view, i, keyEvent ->
            if(keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                /** Create a list with the title specified by user */
                boardListList.add(BoardList("", editText.text.toString(), "currentBoardId", AppLevelFunctions.getCurrentUserID())) //TODO
                adapter.notifyDataSetChanged()
                button.visibility = View.VISIBLE
                editText.visibility = View.GONE
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    private fun setValues() {
    }

    override fun onPause() {
        super.onPause()
        Log.i("cardList", cardList.toString())
        Log.i("boardLists", boardListList.toString())
    }

}