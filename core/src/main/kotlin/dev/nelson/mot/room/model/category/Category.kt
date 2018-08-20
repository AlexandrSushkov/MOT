package dev.nelson.mot.room.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryTable.TABLE_NAME)
class Category(@ColumnInfo(name = CategoryTable.CATEGORY_NAME) var categoryName: String) {

    @ColumnInfo(name = CategoryTable.CATEGORY_ID)
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
