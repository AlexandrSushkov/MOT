package dev.nelson.mot.db.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryTable.TABLE_NAME)
data class CategoryEntity(
    @ColumnInfo(name = CategoryTable.NAME) val name: String,
    @ColumnInfo(name = CategoryTable.FAVORITE, defaultValue = "0" ) val isFavorite: Int = 0, // 1 - true, 0 - false
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = CategoryTable.ID) val id: Int? = null
)
