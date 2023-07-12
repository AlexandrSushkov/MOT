package dev.nelson.mot.main.data.model

import android.os.Parcel
import android.os.Parcelable
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants

data class Category(
    val name: String,
    val isFavorite: Boolean = false,
    val id: Int? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: StringUtils.EMPTY,
        parcel.readBoolean(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeBoolean(isFavorite)
        parcel.writeValue(id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category = Category(parcel)

        override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)

        fun empty() = Category(StringUtils.EMPTY)
    }
}
