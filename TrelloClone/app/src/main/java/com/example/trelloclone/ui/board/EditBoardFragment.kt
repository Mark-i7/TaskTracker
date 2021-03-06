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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.ListAdapter
import com.example.trelloclone.adapter.SingleBoardAdapter
import com.example.trelloclone.databinding.FragmentEditBoardBinding
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.TaskList
import com.example.trelloclone.models.Card
import com.example.trelloclone.viewmodels.SharedViewModel
import de.hdodenhof.circleimageview.CircleImageView

class EditBoardFragment : Fragment(), ListAdapter.OnItemClickListener {

    private var _binding: FragmentEditBoardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var currentBoard: Board
    private lateinit var recyclerView: RecyclerView
    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var actionButton: CircleImageView
    private lateinit var adapter: SingleBoardAdapter
    private var listForNewTaskListItems = ArrayList<TaskList>()
    private var listForNewCardItems = ArrayList<Card>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        currentBoard = sharedViewModel.getCurrentBoard()
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
        sharedViewModel.taskLists.observe(viewLifecycleOwner) {
            adapter.setTaskListData(sharedViewModel.taskLists.value!!)
            adapter.notifyDataSetChanged()
        }
        sharedViewModel.cards.observe(viewLifecycleOwner) {
            adapter.setCardData(sharedViewModel.cards.value!!.filter { it.boardId == currentBoard.id } as ArrayList<Card>)
            adapter.notifyDataSetChanged()
        }
        return root
    }

    private fun initializeElements() {
        recyclerView = binding.recyclerViewEditBoard
        button = binding.btnAddList
        editText = binding.etAddList
        actionButton = binding.viewMembersButton!!
    }

    private fun setupRecyclerview() {
        adapter = SingleBoardAdapter(ArrayList<TaskList>(), ArrayList<Card>(), listForNewCardItems, this)
        recyclerView.adapter = adapter
    }

    private fun setListeners() {
        button.setOnClickListener {
            editText.visibility = View.VISIBLE
            editText.setText("")
        }

        editText.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                /** Create a list with the title specified by user */
                val taskList = TaskList("", currentBoard.id, editText.text.toString())
                sharedViewModel.taskLists.value!!.add(taskList)
                listForNewTaskListItems.add(taskList)
                button.visibility = View.VISIBLE
                editText.visibility = View.GONE
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }

        actionButton.setOnClickListener {
            findNavController().navigate(R.id.action_editBoardFragment_to_membersFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.i("onPause", "onPause")
        Log.i("cardList", listForNewCardItems.toString())
        Log.i("taskLists", listForNewTaskListItems.toString())

        /** Add taskLists to db */
        listForNewTaskListItems.forEach { sharedViewModel.addList(it) }

        sharedViewModel.getListsForBoard(currentBoard.id)
        sharedViewModel.taskLists.observe(viewLifecycleOwner) {
            /** set task list id for cards*/
            sharedViewModel.taskLists.value!!.forEach { taskList ->
                listForNewCardItems.filter { card -> card.listName == taskList.listName }
                    .forEach { it.listId = taskList.id }
            }

            listForNewCardItems.forEach {
                /** Set board id for cards*/
                it.boardId = currentBoard.id
                /** Add cards to db */
                sharedViewModel.addCard(it)
            }
        }
    }

    override fun onItemClick(id: String) {
        sharedViewModel.currentCardId = id
        findNavController().navigate(R.id.action_editBoardFragment_to_cardDetailFragment)
    }
}