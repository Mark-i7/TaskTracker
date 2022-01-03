package com.example.trelloclone.ui.members

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapter.MemberListAdapter
import com.example.trelloclone.databinding.FragmentMembersBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.Board
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.viewmodels.SharedViewModel


class MembersFragment : Fragment() {

    lateinit var mBoardDetails: Board
    private var mAssignedMembersList: ArrayList<User> = arrayListOf()
    private var _binding: FragmentMembersBinding? = null
    private val binding get() = _binding!!
    lateinit var sharedViewModel:SharedViewModel
    lateinit var rvMembers: RecyclerView
    lateinit var ivAddMember: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMembersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        Log.d("members: ", mAssignedMembersList.toString())
        setupMembersList(mAssignedMembersList)

        ivAddMember.setOnClickListener {
            dialogSearchMember()
        }

        return root
    }

    private fun initializeElements() {
        rvMembers = binding.rvMembersList
        ivAddMember = binding.addMemberButton
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        mBoardDetails = sharedViewModel.getCurrentBoard()
        mAssignedMembersList = sharedViewModel.currentAssignedMembersList
    }

    fun setupMembersList(list: ArrayList<User>) {
        mAssignedMembersList = list
        rvMembers.layoutManager = LinearLayoutManager(requireContext())
        rvMembers.setHasFixedSize(true)

        val adapter = MemberListAdapter(requireContext(), list)
        rvMembers.adapter = adapter
    }

    fun memberDetails(user: User) {
        mBoardDetails.members.add(user.id!!)
        Firestore().assignMemberToBoard(this, mBoardDetails, user)
    }

    fun memberAssignSuccess(user: User) {
        mAssignedMembersList.add(user)
        setupMembersList(mAssignedMembersList)
    }

    private fun dialogSearchMember() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_search_member)
        dialog.findViewById<TextView>(R.id.tv_add).setOnClickListener {
            val email = dialog.findViewById<TextView>(R.id.et_email_search_member).text.toString()
            if (email.isNotEmpty()) {
                dialog.dismiss()
                Firestore().getMemberDetails(this, email)
            } else {
                AppLevelFunctions.showToast( "Please members enter email address", requireContext())
            }
        }

        dialog.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}