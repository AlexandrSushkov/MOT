package dev.nelson.mot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.nelson.mot.db.model.category.CategoryDao
import dev.nelson.mot.db.model.category.CategoryEntity
import dev.nelson.mot.db.model.payment.PaymentDao
import dev.nelson.mot.db.model.payment.PaymentEntity

@Database(
    entities = [(PaymentEntity::class), (CategoryEntity::class)],
    version = MotDatabaseInfo.VERSION,
    exportSchema = false
)
abstract class MotDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

    abstract fun paymentDao(): PaymentDao
}
