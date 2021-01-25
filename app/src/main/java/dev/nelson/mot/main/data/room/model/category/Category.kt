package dev.nelson.mot.main.data.room.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryTable.TABLE_NAME)
class Category(
    @ColumnInfo(name = CategoryTable.NAME_COLUMN_NAME) val categoryName: String,
    @PrimaryKey(autoGenerate = true) val id: Int? = null
)
