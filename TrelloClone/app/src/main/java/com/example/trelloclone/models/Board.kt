package com.example.trelloclone.models

import android.os.Parcel
import android.os.Parcelable

class Board(
    id: String,
    var boardName: String,
    var imageId: Int,
    var cardList: ArrayList<Card>,
    viewType: Int
) : BaseClass(id, viewType), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.createTypedArrayList(Card.CREATOR)!!,
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(boardName)
        parcel.writeInt(imageId)
        parcel.writeTypedList(cardList)
        parcel.writeInt(viewType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Board> {
        override fun createFromParcel(parcel: Parcel): Board {
            return Board(parcel)
        }

        override fun newArray(size: Int): Array<Board?> {
            return arrayOfNulls(size)
        }
    }
}