package dev.nelson.mot.main.data.room.model.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Query("SELECT * FROM ${CategoryTable.TABLE_NAME}")
    fun getAllCategories(): Flowable<List<CategoryEntity>>

    @Insert
    fun addCategories(categorise: List<CategoryEntity>)
}
