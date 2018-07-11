package dev.nelson.mot.room.model.category

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flowable<List<Category>>

    @Insert
    fun addCategories(categorise: List<Category>)
}
