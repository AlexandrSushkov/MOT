package dev.nelson.mot.room.model.category

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = CategoryTable.TABLE_NAME)
class Category(@ColumnInfo(name = CategoryTable.CATEGORY_NAME) var categoryName: String) {

    @ColumnInfo(name = CategoryTable.CATEGORY_ID)
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
