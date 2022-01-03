package com.example.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

class Card(
    id: String,
    var boardId: String, // the d of the board it's related to
    var listId: String, // the d of the board it's related to
    var listName: String,
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
        "", "", "", "", "", arrayListOf(""), null, "", "",
        "", "", "0", "", "", 0
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
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
        parcel.writeString(listName)
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Card

        if (boardId != other.boardId) return false
        if (listId != other.listId) return false
        if (listName != other.listName) return false
        if (createdBy != other.createdBy) return false
        if (assignedTo != other.assignedTo) return false
        if (cardTitle != other.cardTitle) return false
        if (startDate != other.startDate) return false
        if (startTime != other.startTime) return false
        if (dueDate != other.dueDate) return false
        if (dueTime != other.dueTime) return false
        if (image != other.image) return false
        if (details != other.details) return false
        if (description != other.description) return false

        return true
    }

    override fun hashCode(): Int {
        var result = boardId.hashCode()
        result = 31 * result + listId.hashCode()
        result = 31 * result + listName.hashCode()
        result = 31 * result + createdBy.hashCode()
        result = 31 * result + assignedTo.hashCode()
        result = 31 * result + (cardTitle?.hashCode() ?: 0)
        result = 31 * result + startDate.hashCode()
        result = 31 * result + startTime.hashCode()
        result = 31 * result + dueDate.hashCode()
        result = 31 * result + dueTime.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + details.hashCode()
        result = 31 * result + description.hashCode()
        return result
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
