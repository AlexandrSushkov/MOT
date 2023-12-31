package dev.nelson.mot.db.model.payment_tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.nelson.mot.db.model.payment.PaymentTable

@Entity(tableName = PaymentTagTable.TABLE_NAME)
data class PaymentTagEntity(
    @ColumnInfo(name = PaymentTable.TITLE) var title: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PaymentTable.ID) val id: Long? = null,
)
