package com.example.trelloclone.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.navigation.fragment.findNavController
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentHomeBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.viewmodels.SharedViewModel
import com.example.trelloclone.viewmodels.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var button: FloatingActionButton

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var imageView : ImageView
    private lateinit var cardButton : Button
    private lateinit var boardButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(Firestore())
        sharedViewModel = ViewModelProvider(requireActivity(), factory).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        root.apply { 
            button = findViewById(R.id.fab)
        }

        button.setOnClickListener {
            view->view.findNavController().navigate(R.id.nav_create_board)
        }
        
        initializeElements()
        setListeners()
        return root
    }

    private fun initializeElements() {
        imageView = binding.imageView
        cardButton = binding.btnStartWithCards
        boardButton = binding.btnStartWithBoards
    }

    private fun setListeners() {
        cardButton.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_nav_my_card)
        }

        boardButton.setOnClickListener{
            //TODO
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}