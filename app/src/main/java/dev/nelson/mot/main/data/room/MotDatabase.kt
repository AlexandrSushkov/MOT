package dev.nelson.mot.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.nelson.mot.main.data.room.model.category.Category
import dev.nelson.mot.main.data.room.model.category.CategoryDao
import dev.nelson.mot.main.data.room.model.payment.Payment
import dev.nelson.mot.main.data.room.model.payment.PaymentDao

@Database(entities = [
    (Payment::class),
    (Category::class)], version = MotDatabaseInfo.VERSION, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun paymentDao(): PaymentDao
}
