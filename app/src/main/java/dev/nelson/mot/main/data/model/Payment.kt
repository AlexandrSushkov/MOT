package dev.nelson.mot.main.data.model

import android.os.Parcel
import android.os.Parcelable
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory

data class Payment(
    val name: String,
    val cost: Int,
    val id: Long? = null,
    val date: String? = null,
    val dateInMills: Long? = null,
    val category: Category? = null,


    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(Category::class.java.classLoader)
    )

    override fun toString(): String {
        return """
            name: $name
            cost: $cost
            date: $date
            id: $id
            category: $category
        """
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(cost)
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

fun Payment.copyWith(name:String, cost: Int): Payment =
    Payment(
        name,
        cost,
        id = id,
        date = date,
        dateInMills = dateInMills,
        category = category
    )

fun Payment.toPaymentEntity(): PaymentEntity =
    PaymentEntity(
        name,
        cost,
        id = id,
        date = date,
        dateInMilliseconds = dateInMills,
        categoryIdKey = category?.id
    )

fun PaymentWithCategory.toPayment(): Payment {
    val paymentEntity = this.paymentEntity
    val categoryEntity = this.categoryEntity
    return with(paymentEntity) {
        Payment(title, cost, id, date, dateInMilliseconds, categoryEntity?.toCategory())
    }
}

fun List<PaymentWithCategory>.toPaymentList(): List<Payment> = this.map { it.toPayment() }
