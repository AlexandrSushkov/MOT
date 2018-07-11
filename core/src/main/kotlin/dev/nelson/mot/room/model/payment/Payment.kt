package dev.nelson.mot.room.model.payment

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import dev.nelson.mot.room.model.category.CategoryTable

@Entity(tableName = PaymentTable.TABLE_NAME)
class Payment(@ColumnInfo(name = PaymentTable.TITLE) var title: String,
              @ColumnInfo(name = PaymentTable.SUMMARY) var summary: String,
              @ColumnInfo(name = PaymentTable.CATEGORY_ID) val categoryId: Int,
              //todo add date as column
              @ColumnInfo(name = PaymentTable.COST) val cost: Long) {

    @ColumnInfo(name = CategoryTable.CATEGORY_ID)
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
