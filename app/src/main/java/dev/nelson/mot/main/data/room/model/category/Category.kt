package dev.nelson.mot.main.data.room.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.nelson.mot.main.data.room.model.category.CategoryTable

@Entity(tableName = CategoryTable.TABLE_NAME)
class Category(
    @ColumnInfo(name = CategoryTable.CATEGORY_ID) @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = CategoryTable.CATEGORY_NAME) var categoryName: String
) {

//    @ColumnInfo(name = CategoryTable.CATEGORY_ID)
//    @PrimaryKey(autoGenerate = true)
//    var id: Long = 0
}
