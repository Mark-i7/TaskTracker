package com.example.trelloclone.ui.fragment

import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentCardDetailBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.Card
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class CardDetailFragment : Fragment() {

    private var _binding: FragmentCardDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var uploadImageView: ImageView
    private lateinit var cardDetailImage: ImageView
    private lateinit var cardName: TextView
    private lateinit var cardDetails: TextInputLayout
    private lateinit var startDateButton: Button
    private lateinit var dueDateButton: Button
    private lateinit var startTimeButton: Button
    private lateinit var dueTimeButton: Button
    private lateinit var startDateAndTime: TextView
    private lateinit var dueDateAndTime: TextView
    private var startDate = ""
    private var startTime = ""
    private var dueDate = ""
    private var dueTime = ""
    private lateinit var card: Card

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setListeners()
        return root
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        cardDetailImage.setImageURI(it)
    }


    private fun initializeElements() {
        uploadImageView = binding.ivUploadImage
        cardDetailImage = binding.ivAddCardDetailImage
        cardName = binding.tvCardName
        cardDetails = binding.tvCardDetails
        startDateButton = binding.buttonStartDate
        startTimeButton = binding.buttonStartTime
        dueDateButton = binding.buttonDueDate
        dueTimeButton = binding.buttonDueTime
        startDateAndTime = binding.tvStartDate
        dueDateAndTime = binding.tvDueDate
        Firestore().getCardDetails(this, "SG8YUj3AQ3s4CcdedHOV")
    }

    fun getDetailsFromDB(card: Card) {
        this.card = card
    }

    private fun setListeners() {
        uploadImageView.setOnClickListener {
            getImage.launch("image/*")
            uploadImageView.visibility = View.INVISIBLE
        }

        startDateButton.setOnClickListener {
            openDatePicker(startDateAndTime, "Start date: ", 1)
        }

        startTimeButton.setOnClickListener {
            openTimePicker(startDateAndTime, "Start date: ", 1)
        }

        dueDateButton.setOnClickListener {
            openDatePicker(dueDateAndTime, "Due date: ", 2)
        }

        dueTimeButton.setOnClickListener {
            openTimePicker(dueDateAndTime, "Due date: ", 2)
        }
    }

    private fun openTimePicker(textView: TextView, text: String, type: Int) {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Choose a date")
            .build()
        timePicker.show(childFragmentManager, "tag")

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val min = timePicker.minute
            if (type == 1) {
                startTime = "$hour:$min"
                textView.text = getString(R.string.time, text, startDate, hour.toString(), min.toString())
            } else {
                dueTime = "$hour:$min"
                textView.text = getString(R.string.time, text, dueDate, hour.toString(), min.toString())
            }
        }
    }

    private fun openDatePicker(textView: TextView, text: String, type: Int) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select a date")
                .build()
        datePicker.show(childFragmentManager, "tag")

        datePicker.addOnPositiveButtonClickListener {
            if (type == 1) {
                startDate = datePicker.headerText
                textView.text = getString(R.string.date, text, datePicker.headerText, startTime)
            } else {
                dueDate = datePicker.headerText
                textView.text = getString(R.string.date, text, datePicker.headerText, dueTime)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun cardUpdatedSuccess() {
        Toast.makeText(requireContext(), "Card successfully updated", Toast.LENGTH_SHORT).show()
    }
}