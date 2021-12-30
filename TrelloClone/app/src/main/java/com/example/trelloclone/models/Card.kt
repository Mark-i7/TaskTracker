package com.example.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

class Card(
    id: String,
    var boardId: String, // the d of the board it's related to
    var listId: String, // the list the task is currently part of
    var createdBy: String, // the id of the current user who executed the creation
    var assignedTo: ArrayList<String>,
    var cardTitle: String?,
    var startDate: String,
    var startTime: String,
    var dueDate: String,
    var dueTime: String,
    var image: String,
    var details: String,
    var description: String,
    viewType: Int
) : BaseClass(id, viewType), Parcelable {

    constructor() : this(
        "", "", "", "", arrayListOf(""), null, "", "",
        "", "", "0", "", "", 0
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(boardId)
        parcel.writeString(listId)
        parcel.writeString(createdBy)
        parcel.writeStringList(assignedTo)
        parcel.writeString(cardTitle)
        parcel.writeString(startDate)
        parcel.writeString(startTime)
        parcel.writeString(dueDate)
        parcel.writeString(dueTime)
        parcel.writeString(image)
        parcel.writeString(details)
        parcel.writeString(description)
        parcel.writeInt(viewType)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "Card(createdBy='$createdBy', assignedTo=$assignedTo, cardTitle=$cardTitle, startDate='$startDate', dueDate='$dueDate', image=$image, details='$details', description='$description')"
    }

    companion object CREATOR : Parcelable.Creator<Card> {
        override fun createFromParcel(parcel: Parcel): Card {
            return Card(parcel)
        }

        override fun newArray(size: Int): Array<Card?> {
            return arrayOfNulls(size)
        }
    }


}
