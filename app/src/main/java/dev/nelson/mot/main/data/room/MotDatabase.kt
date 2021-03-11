package dev.nelson.mot.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.data.room.model.category.CategoryDao
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.payment.PaymentDao

@Database(entities = [(PaymentEntity::class), (CategoryEntity::class)], version = MotDatabaseInfo.VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun paymentDao(): PaymentDao
}
