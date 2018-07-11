package dev.nelson.mot.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import dev.nelson.mot.room.model.category.Category
import dev.nelson.mot.room.model.category.CategoryDao

@Database(entities = [(Category::class)], version = MotDatabaseInfo.VERSION, exportSchema = false)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}
