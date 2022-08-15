package dev.nelson.mot.main.data.model

import android.os.Parcel
import android.os.Parcelable

data class Payment(
    val name: String,
    val cost: Int,
    val message: String = "",
    val id: Long? = null,
    val date: String? = null,
    val dateInMills: Long? = null,
    val category: Category? = null,
    val isExpanded: Boolean = false


    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(Category::class.java.classLoader)
    )

    override fun toString(): String {
        return """
            name: $name
            cost: $cost
            message: $message
            date: $date
            id: $id
            category: $category
        """
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(cost)
        parcel.writeString(message)
        parcel.writeValue(id)
        parcel.writeString(date)
        parcel.writeValue(dateInMills)
        parcel.writeParcelable(category, flags)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Payment> {
        override fun createFromParcel(parcel: Parcel): Payment = Payment(parcel)

        override fun newArray(size: Int): Array<Payment?> = arrayOfNulls(size)
    }
}
