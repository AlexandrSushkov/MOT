package dev.nelson.mot.main.data.room.model.payment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.nelson.mot.main.data.room.model.category.Category
import dev.nelson.mot.main.data.room.model.category.CategoryTable
import java.sql.Date

@Entity(tableName = PaymentTable.TABLE_NAME,
    foreignKeys = [(ForeignKey(entity = Category::class,
        parentColumns = arrayOf(CategoryTable.ID_COLUMN_NAME),
        childColumns = arrayOf(PaymentTable.CATEGORY_ID_COLUMN_NAME),
        onDelete = ForeignKey.SET_NULL))]
)
data class Payment(
    @ColumnInfo(name = PaymentTable.TITLE_COLUMN_NAME) var title: String,
    @ColumnInfo(name = PaymentTable.COST_COLUMN_NAME) val cost: Int,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = PaymentTable.ID_COLUMN_NAME) val id: Int? = null,
    @ColumnInfo(name = PaymentTable.SUMMARY_COLUMN_NAME) var summary: String? = null,
    @ColumnInfo(name = PaymentTable.CATEGORY_ID_COLUMN_NAME) val categoryId: Int? = null,
    @ColumnInfo(name = PaymentTable.DATE_COLUMN_NAME) val date: String? = null,
    @ColumnInfo(name = PaymentTable.DATE_IN_MILLISECONDS) val dateInMilliseconds: Int? = null
)

