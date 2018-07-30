package dev.nelson.mot.room.model.payment

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey
import dev.nelson.mot.room.model.category.Category
import dev.nelson.mot.room.model.category.CategoryTable
import java.time.OffsetDateTime

@Entity(tableName = PaymentTable.TABLE_NAME,
        foreignKeys = [(ForeignKey(entity = Category::class, parentColumns = arrayOf(CategoryTable.CATEGORY_ID), childColumns = arrayOf(PaymentTable.CATEGORY_ID), onDelete = ForeignKey.SET_NULL))])
data class Payment(@PrimaryKey(autoGenerate = true)
                   @ColumnInfo(name = CategoryTable.CATEGORY_ID) val id: Long? = null,
                   @ColumnInfo(name = PaymentTable.TITLE) var title: String,
                   @ColumnInfo(name = PaymentTable.SUMMARY) var summary: String,
                   @ColumnInfo(name = PaymentTable.CATEGORY_ID) val categoryId: Int,
                   @ColumnInfo(name = PaymentTable.DATE) val date: OffsetDateTime,
                   @ColumnInfo(name = PaymentTable.COST) val cost: Long)

