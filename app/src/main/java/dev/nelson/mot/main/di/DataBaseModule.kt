package dev.nelson.mot.main.di

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.nelson.mot.main.data.room.MotDatabase
import dev.nelson.mot.main.data.room.MotDatabaseInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object DataBaseModule {

    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            val categoriesOldTableName = "categories"
            val categoriesTempTableName = "categories_temp"
            val paymentsOldTableName = "payments"
            val paymentsTempTableName = "payments_temp"

            //migrate categories
            // Create the new table
            database.execSQL("CREATE TABLE IF NOT EXISTS $categoriesTempTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)")
            // Copy the data
            database.execSQL("INSERT INTO $categoriesTempTableName (id, name) SELECT _id, category_name FROM $categoriesOldTableName")
            // Remove the old table
            database.execSQL("DROP TABLE $categoriesOldTableName")
            //Change the table name to the correct one
            database.execSQL("ALTER TABLE $categoriesTempTableName RENAME TO $categoriesOldTableName")

            //migrate payments
//            database.execSQL("CREATE TABLE IF NOT EXISTS $paymentsTempTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, summary TEXT, category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL, date DATE DEFAULT CURRENT_DATE, cost INTEGER NOT NULL)")
            database.execSQL("CREATE TABLE IF NOT EXISTS $paymentsTempTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, summary TEXT, category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL, date TEXT, date_in_milliseconds INTEGER, cost INTEGER NOT NULL)")
//            database.execSQL("CREATE TABLE IF NOT EXISTS $paymentsTempTableName (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, summary TEXT, category_id INTEGER REFERENCES categories(id) ON DELETE SET NULL, cost INTEGER NOT NULL)")

//            database.execSQL("INSERT INTO $paymentsTempTableName (id, title, summary, category_id, date, cost) SELECT _id, title, summary, category_id, date, cost FROM $paymentsOldTableName")
            database.execSQL("INSERT INTO $paymentsTempTableName (id, title, summary, category_id, date, cost) SELECT _id, title, summary, category_id, date, cost FROM $paymentsOldTableName")
//            database.execSQL("INSERT INTO $paymentsTempTableName (id, title, summary, category_id, cost) SELECT _id, title, summary, category_id, cost FROM $paymentsOldTableName")
            database.execSQL("DROP TABLE $paymentsOldTableName")
            database.execSQL("ALTER TABLE $paymentsTempTableName RENAME TO $paymentsOldTableName")

            //
            val cursor = database.query("SELECT id, date FROM $paymentsOldTableName")
            if (cursor.count > 0) {
                cursor.moveToFirst()
                do {
                    val id = cursor.getLong(cursor.getColumnIndex("id"))
                    val date = cursor.getString(cursor.getColumnIndex("date"))

                    var dateInMilliseconds = 0L
                    val f = SimpleDateFormat("yyyy-MM-dd")
                    try {
                        val d: Date = f.parse(date)
                        dateInMilliseconds = d.time
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    val cv = ContentValues()
                    cv.put("date_in_milliseconds", dateInMilliseconds)

                    database.update(paymentsOldTableName, SQLiteDatabase.CONFLICT_NONE, cv, "id=?", listOf(id).toTypedArray())
//                    "UPDATE $paymentsOldTableName SET date_in_milliseconds=1 WHERE id=$id"
                } while (cursor.moveToNext())
            }
            cursor.close()

        }
    }

    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): MotDatabase =
        Room.databaseBuilder(context, MotDatabase::class.java, MotDatabaseInfo.NAME)
            .createFromAsset("mot.db")
            .addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()

//    @Provides
//    fun provideLegacyDb(@ApplicationContext context: Context): SQLiteDatabase {
//        val helper = dev.nelson.mot.legacy.db.SQLiteOpenHelperImpl(context)
//        return helper.readableDatabase
//    }

}

