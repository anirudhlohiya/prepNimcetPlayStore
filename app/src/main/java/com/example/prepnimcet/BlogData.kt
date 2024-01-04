package com.example.prepnimcet

import android.os.Parcel
import android.os.Parcelable

data class BlogData(val title:String,val detail:String):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(detail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BlogData> {
        override fun createFromParcel(parcel: Parcel): BlogData {
            return BlogData(parcel)
        }

        override fun newArray(size: Int): Array<BlogData?> {
            return arrayOfNulls(size)
        }
    }
}
