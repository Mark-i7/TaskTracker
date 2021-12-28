package com.example.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

class Card(
    id: String,
    var createdBy: String, // the id of the current user who executed the creation
    var assignedTo: ArrayList<String>,
    var cardTitle: String?,
    var startDate: String,
    var dueDate: String,
    var imageId: Int,
    var details: String,
    var description: String,
    viewType: Int
) : BaseClass(id, viewType), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(createdBy)
        parcel.writeStringList(assignedTo)
        parcel.writeString(cardTitle)
        parcel.writeString(startDate)
        parcel.writeString(dueDate)
        parcel.writeInt(imageId)
        parcel.writeString(details)
        parcel.writeString(description)
        parcel.writeInt(viewType)
    }

    override fun describeContents(): Int {
        return 0
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
