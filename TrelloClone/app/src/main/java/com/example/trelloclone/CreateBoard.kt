package com.example.trelloclone

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trelloclone.model.Board


class CreateBoard : Fragment() {

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserName:  String
    private lateinit var mBoardDetails: Board
    private var mBoardImageURL: String = ""
    private var isBoardUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_board, container, false)

    }

}