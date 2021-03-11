package dev.nelson.mot.main.data.room.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryTable.TABLE_NAME)
class CategoryEntity(
    @ColumnInfo(name = CategoryTable.NAME) val name: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CategoryTable.ID) val id: Int? = null
)
