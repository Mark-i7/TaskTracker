package com.example.trelloclone.ui.cards

import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentCardDetailBinding
import com.example.trelloclone.models.Card
import com.example.trelloclone.utils.AppLevelFunctions
import com.example.trelloclone.viewmodels.SharedViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.common.io.Files
import com.google.firebase.storage.FirebaseStorage

class CardDetailFragment : Fragment() {

    private var _binding: FragmentCardDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var currentCard: Card
    private lateinit var uploadImageView: ImageView
    private lateinit var cardDetailImage: ImageView
    private lateinit var cardName: TextView
    private lateinit var cardDetails: TextInputEditText
    private lateinit var startDateButton: Button
    private lateinit var dueDateButton: Button
    private lateinit var startTimeButton: Button
    private lateinit var dueTimeButton: Button
    private lateinit var startDateAndTime: TextView
    private lateinit var dueDateAndTime: TextView
    private lateinit var updateButton: Button
    private lateinit var listDropdown: AutoCompleteTextView
    private var startDate = ""
    private var startTime = ""
    private var dueDate = ""
    private var dueTime = ""
    private var selectedImageUri: Uri? = null
    private var cardImageUrl = ""
    private var selectList = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        currentCard = sharedViewModel.getCurrentCard()
        sharedViewModel.getListsForBoard(currentCard.boardId)
        startDate = currentCard.startDate
        startTime = currentCard.startTime
        dueDate = currentCard.dueDate
        dueTime = currentCard.dueTime
        selectList = if (sharedViewModel.taskLists.value?.map { it -> it.listName } != null) {
            sharedViewModel.taskLists.value?.map { it -> it.listName } as ArrayList<String>
        } else {
            arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        setListeners()
        setValues()
        return root
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        cardDetailImage.setImageURI(it)
        selectedImageUri = it
        currentCard.image = it.toString()
        Glide
            .with(requireContext())
            .load(currentCard.image)
            .centerCrop()
            .placeholder(R.drawable.frame)
            .into(cardDetailImage)
    }


    private fun initializeElements() {
        uploadImageView = binding.ivUploadImage
        cardDetailImage = binding.ivAddCardDetailImage
        cardName = binding.tvCardName
        cardDetails = binding.tvCardDetails1
        startDateButton = binding.buttonStartDate
        startTimeButton = binding.buttonStartTime
        dueDateButton = binding.buttonDueDate
        dueTimeButton = binding.buttonDueTime
        startDateAndTime = binding.tvStartDate
        dueDateAndTime = binding.tvDueDate
        updateButton = binding.updateButton
        listDropdown = binding.autoCompleteTextView1
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

        updateButton.setOnClickListener {
            currentCard.listName = listDropdown.text.toString()
            uploadCardImage()
            sharedViewModel.updateCardDetails(
                this, Card(
                    currentCard.id,
                    currentCard.boardId,
                    currentCard.listId,
                    currentCard.listName,
                    currentCard.createdBy,
                    currentCard.assignedTo,
                    cardName.text.toString(),
                    startDate,
                    startTime,
                    dueDate,
                    dueTime,
                    currentCard.image,
                    currentCard.details,
                    cardDetails.text.toString(),
                    currentCard.viewType
                )
            )
        }
    }

    private fun setValues() {
        cardName.text = currentCard.cardTitle
        cardDetails.setText(currentCard.details)
        startDateAndTime.text = "${currentCard.startDate} at ${currentCard.startTime}"
        dueDateAndTime.text = "${currentCard.dueDate} at ${currentCard.dueTime}"
        listDropdown.setText(currentCard.listName)

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, selectList)
        listDropdown.setAdapter(adapter)
        Glide
            .with(requireContext())
            .load(currentCard.image)
            .centerCrop()
            .placeholder(R.drawable.frame)
            .into(cardDetailImage)
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
                textView.text =
                    getString(R.string.time, text, startDate, hour.toString(), min.toString())
            } else {
                dueTime = "$hour:$min"
                textView.text =
                    getString(R.string.time, text, dueDate, hour.toString(), min.toString())
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

    private fun uploadCardImage() {
        if (selectedImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child(
                "CARD_IMAGE" + System.currentTimeMillis() + "." + Files.getFileExtension(
                    selectedImageUri.toString()
                )
            )

            storageRef.putFile(selectedImageUri!!)
                .addOnSuccessListener {
                        taskSnapshot ->
                    Log.i("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            uri ->
                        Log.i("Downloadable Image File", uri.toString())
                        cardImageUrl = uri.toString()

                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Upload error", exception.message.toString())
                    AppLevelFunctions.showToast(exception.message.toString(), requireContext())
                }
        }
    }
}