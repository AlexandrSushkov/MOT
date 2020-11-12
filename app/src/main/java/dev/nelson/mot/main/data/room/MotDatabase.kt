package dev.nelson.mot.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.nelson.mot.main.data.room.model.category.Category
import dev.nelson.mot.main.data.room.model.category.CategoryDao

@Database(entities = [(Category::class)], version = MotDatabaseInfo.VERSION, exportSchema = true)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
}
