package dev.nelson.mot.main.data.room.model.category

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flowable<List<Category>>

    @Insert
    fun addCategories(categorise: List<Category>)
}
