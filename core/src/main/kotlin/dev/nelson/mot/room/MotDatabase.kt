package dev.nelson.mot.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.nelson.mot.room.model.category.Category
import dev.nelson.mot.room.model.category.CategoryDao

@Database(entities = [(Category::class)], version = MotDatabaseInfo.VERSION, exportSchema = false)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}
