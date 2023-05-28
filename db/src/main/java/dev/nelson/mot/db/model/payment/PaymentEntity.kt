package dev.nelson.mot.db.model.payment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.db.model.category.CategoryTable

@Entity(
    tableName = PaymentTable.TABLE_NAME,
    foreignKeys = [(ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = arrayOf(CategoryTable.ID),
        childColumns = arrayOf(PaymentTable.CATEGORY_ID_KEY),
        onDelete = ForeignKey.SET_NULL
    ))],
)
data class PaymentEntity(
    @ColumnInfo(name = PaymentTable.TITLE) var title: String,
    @ColumnInfo(name = PaymentTable.COST) val cost: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PaymentTable.ID) val id: Int? = null,
    @ColumnInfo(name = PaymentTable.SUMMARY) var summary: String? = null,
    @ColumnInfo(name = PaymentTable.CATEGORY_ID_KEY) val categoryIdKey: Int? = null,
    @Deprecated(message = "store date in epoch milliseconds")
    @ColumnInfo(name = PaymentTable.DATE) val date: String? = null,
    @ColumnInfo(name = PaymentTable.DATE_IN_MILLISECONDS) val dateInMilliseconds: Long? = null
)

