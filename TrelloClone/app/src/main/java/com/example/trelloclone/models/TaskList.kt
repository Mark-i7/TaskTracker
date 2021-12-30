package com.example.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

class TaskList(
    var id: String,
    var boardId: String, // board which is part of
    var listName: String
) : Parcelable {

    constructor() : this(
        "", "", ""
    )

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(boardId)
        parcel.writeString(listName)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "TaskList(id='$id', boardId='$boardId', listName='$listName')"
    }

    companion object CREATOR : Parcelable.Creator<TaskList> {
        override fun createFromParcel(parcel: Parcel): TaskList {
            return TaskList(parcel)
        }

        override fun newArray(size: Int): Array<TaskList?> {
            return arrayOfNulls(size)
        }
    }
}
