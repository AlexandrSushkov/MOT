package dev.nelson.mot.main.data.model

import android.os.Parcel
import android.os.Parcelable
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity

/**
 * Data class for [PaymentWithCategory] and [PaymentEntity] for presentation layer.
 *
 * @property name
 * @property cost
 * @property message
 * @property id
 * @property date formatted string representation of the mills
 * @property dateInMills date in epoch milliseconds
 * @property category
 * @property isExpanded used in payment list to show/hide message
 * @property isSelected used in payment list to show/hide selection checkbox
 * @constructor Create empty Payment
 */
data class Payment(
    val name: String,
    val cost: Int,
    val message: String = StringUtils.EMPTY,
    val id: Int? = null,
    val date: String? = null,
    val dateInMills: Long? = null,
    val category: Category? = null,
    var isExpanded: Boolean = false,
    var isSelected: Boolean = false,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(), // name
        parcel.readInt(), // cost
        parcel.readString().orEmpty(), // message
        parcel.readValue(Long::class.java.classLoader) as? Int, //id
        parcel.readString(), // date
        parcel.readValue(Long::class.java.classLoader) as? Long, // date in mills
        parcel.readParcelable(Category::class.java.classLoader), // category
        parcel.readBoolean(), // is expanded
        parcel.readBoolean() // is selected
    )

    override fun toString(): String {
        return """
            name: $name
            cost: $cost
            message: $message
            id: $id
            date: $date
            dateInMills: $dateInMills
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
        parcel.writeBoolean(isExpanded)
        parcel.writeBoolean(isSelected)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Payment> {
        override fun createFromParcel(parcel: Parcel): Payment = Payment(parcel)

        override fun newArray(size: Int): Array<Payment?> = arrayOfNulls(size)

        fun empty(): Payment {
            return Payment(StringUtils.EMPTY, 0)
        }
    }
}
