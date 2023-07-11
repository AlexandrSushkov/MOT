package dev.nelson.mot.db

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.nelson.mot.db.model.category.CategoryTable
import dev.nelson.mot.db.model.payment.PaymentTable
import dev.nelson.mot.db.model.payment_tag.PaymentTagTable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * version 1 to version 2 migration is migration from plain SQLite to room implementation.
 */
//val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        val tempTableName = "temp_table"
//
//        fun migrateCategoriesTable(database: SupportSQLiteDatabase) {
//            with(database) {
//                execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS $tempTableName (
//                        ${CategoryTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//                        ${CategoryTable.NAME} TEXT NOT NULL,
//                        ${CategoryTable.FAVORITE} INTEGER NOT NULL DEFAULT 0
//                    )
//                """.trimIndent()
//                )
//                execSQL(
//                    """
//                    INSERT INTO $tempTableName (${CategoryTable.ID}, ${CategoryTable.NAME})
//                    SELECT ${CategoryTableV1.ID}, ${CategoryTableV1.NAME} FROM ${CategoryTableV1.TABLE_NAME}
//                """.trimIndent()
//                )
//                execSQL("DROP TABLE ${CategoryTableV1.TABLE_NAME}")
//                execSQL("ALTER TABLE $tempTableName RENAME TO ${CategoryTable.TABLE_NAME}")
//            }
//        }
//
//        fun migratePaymentTable(database: SupportSQLiteDatabase) {
//            with(database) {
//                //migrate payments table
//                execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS $tempTableName (
//                        ${PaymentTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//                        ${PaymentTable.TITLE} TEXT NOT NULL,
//                        ${PaymentTable.CATEGORY_ID_KEY} INTEGER REFERENCES ${CategoryTable.TABLE_NAME}(${CategoryTable.ID}) ON DELETE SET NULL,
//                        ${PaymentTable.COST} INTEGER NOT NULL,
//                        ${PaymentTable.DATE} TEXT,
//                        ${PaymentTable.DATE_IN_MILLISECONDS} INTEGER,
//                        ${PaymentTable.SUMMARY} TEXT
//                    )
//                """.trimIndent()
//                )
//
//                execSQL(
//                    """
//                    INSERT INTO $tempTableName (
//                        ${PaymentTable.ID},
//                        ${PaymentTable.TITLE},
//                        ${PaymentTable.CATEGORY_ID_KEY},
//                        ${PaymentTable.COST},
//                        ${PaymentTable.DATE},
//                        ${PaymentTable.DATE_IN_MILLISECONDS},
//                        ${PaymentTable.SUMMARY}
//                    )
//                    SELECT
//                        ${PaymentTableV1.ID},
//                        ${PaymentTableV1.TITLE},
//                        ${PaymentTableV1.CATEGORY_ID},
//                        ${PaymentTableV1.COST},
//                        ${PaymentTableV1.DATE},
//                        ${PaymentTableV1.SUMMARY}
//                    FROM ${PaymentTableV1.TABLE_NAME}
//                """.trimIndent()
//                )
//
//                //migrate date
//                val cursor =
//                    query("SELECT ${PaymentTableV1.ID}, ${PaymentTableV1.DATE} FROM ${PaymentTableV1.TABLE_NAME}")
//                if (cursor.count > 0) {
//                    cursor.moveToFirst()
//                    do {
//                        val id = cursor.getLong(cursor.getColumnIndex(PaymentTableV1.ID))
//                        val date = cursor.getString(cursor.getColumnIndex(PaymentTableV1.DATE))
//
//                        val dateFormat =
//                            SimpleDateFormat(MotDbV1Constants.DATE_FORMAT, Locale.getDefault())
//                        val parsedDate: Date = dateFormat.parse(date)
//                        val dateInMilliseconds = parsedDate.time
//
//                        val contentValues = ContentValues().apply {
//                            put(PaymentTable.DATE_IN_MILLISECONDS, dateInMilliseconds)
//                            put(PaymentTable.DATE, date)
//                        }
//
//                        update(
//                            tempTableName,
//                            SQLiteDatabase.CONFLICT_NONE,
//                            contentValues,
//                            "${PaymentTable.ID}=?",
//                            listOf(id).toTypedArray()
//                        )
//                    } while (cursor.moveToNext())
//                }
//                cursor.close()
//
//                execSQL("DROP TABLE ${PaymentTableV1.TABLE_NAME}")
//                execSQL("ALTER TABLE $tempTableName RENAME TO ${PaymentTable.TABLE_NAME}")
//            }
//        }
//
//        fun addPaymentTagTable(database: SupportSQLiteDatabase) {
//            with(database) {
//                execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS ${PaymentTagTable.TABLE_NAME} (
//                        ${PaymentTagTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//                        ${PaymentTagTable.TITLE} TEXT NOT NULL
//                    )
//                """.trimIndent()
//                )
//            }
//        }
//
//        fun addPaymentTagToPaymentTable(database: SupportSQLiteDatabase) {
//            with(database) {
//                execSQL(
//                    """
//                    CREATE TABLE IF NOT EXISTS ${PaymentTagTable.TABLE_NAME} (
//                        ${PaymentTagTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
//                        ${PaymentTagTable.TITLE} TEXT NOT NULL
//                    )
//                """.trimIndent()
//                )
//            }
//        }
//
//        database.apply {
//            migrateCategoriesTable(this)
//            migratePaymentTable(this)
//            addPaymentTagTable(this)
//        }
//    }
//}
//
////DB v1
//private object MotDbV1Constants {
//    const val DATE_FORMAT = "yyyy-MM-dd"
//}
//
//private object PaymentTableV1 {
//    const val TABLE_NAME = "payments"
//
//    const val ID = "_id"
//    const val TITLE = "title"
//    const val SUMMARY = "summary"
//    const val CATEGORY_ID = "category_id"
//    const val DATE = "date"
//    const val COST = "cost"
//}
//
//private object CategoryTableV1 {
//    const val TABLE_NAME = "categories"
//
//    const val ID = "_id"
//    const val NAME = "category_name"
//}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val tempTableName = "temp_table"

        fun migrateCategoriesTable(database: SupportSQLiteDatabase) {
            with(database) {
                execSQL("CREATE TABLE IF NOT EXISTS $tempTableName (${CategoryTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${CategoryTable.NAME} TEXT NOT NULL, ${CategoryTable.FAVORITE} INTEGER NOT NULL DEFAULT 0)")
                execSQL("INSERT INTO $tempTableName (${CategoryTable.ID}, ${CategoryTable.NAME}) SELECT ${CategoryTableV1.ID}, ${CategoryTableV1.NAME} FROM ${CategoryTableV1.TABLE_NAME}")
                execSQL("DROP TABLE ${CategoryTableV1.TABLE_NAME}")
                execSQL("ALTER TABLE $tempTableName RENAME TO ${CategoryTable.TABLE_NAME}")
            }
        }

        fun migratePaymentTable(database: SupportSQLiteDatabase) {
            with(database) {
                //migrate payments table
                execSQL("CREATE TABLE IF NOT EXISTS $tempTableName (${PaymentTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${PaymentTable.TITLE} TEXT NOT NULL, ${PaymentTable.CATEGORY_ID_KEY} INTEGER REFERENCES categories(${CategoryTable.ID}) ON DELETE SET NULL, ${PaymentTable.COST} INTEGER NOT NULL, ${PaymentTable.DATE} TEXT, ${PaymentTable.DATE_IN_MILLISECONDS} INTEGER, ${PaymentTable.SUMMARY} TEXT)")
                execSQL("INSERT INTO $tempTableName (${PaymentTable.ID}, ${PaymentTable.TITLE}, ${PaymentTable.CATEGORY_ID_KEY}, ${PaymentTable.COST}, ${PaymentTable.SUMMARY}) SELECT ${PaymentTableV1.ID}, ${PaymentTableV1.TITLE}, ${PaymentTableV1.CATEGORY_ID}, ${PaymentTableV1.COST}, ${PaymentTableV1.SUMMARY} FROM ${PaymentTableV1.TABLE_NAME}")

                //migrate date
                val cursor = query("SELECT ${PaymentTableV1.ID}, ${PaymentTableV1.DATE} FROM ${PaymentTableV1.TABLE_NAME}")
                if (cursor.count > 0) {
                    cursor.moveToFirst()
                    do {
                        val id = cursor.getLong(cursor.getColumnIndex(PaymentTableV1.ID))
                        val date = cursor.getString(cursor.getColumnIndex(PaymentTableV1.DATE))

                        val dateFormat = SimpleDateFormat(MotDbV1Constants.DATE_FORMAT, Locale.getDefault())
                        val parsedDate: Date? = dateFormat.parse(date)
                        val dateInMilliseconds = parsedDate?.time ?: System.currentTimeMillis()

                        val contentValues = ContentValues().apply {
                            put(PaymentTable.DATE_IN_MILLISECONDS, dateInMilliseconds)
                            put(PaymentTable.DATE, date)
                        }

                        update(
                            tempTableName,
                            SQLiteDatabase.CONFLICT_NONE,
                            contentValues,
                            "${PaymentTable.ID}=?",
                            listOf(id).toTypedArray()
                        )
                    } while (cursor.moveToNext())
                }
                cursor.close()

                execSQL("DROP TABLE ${PaymentTableV1.TABLE_NAME}")
                execSQL("ALTER TABLE $tempTableName RENAME TO ${PaymentTable.TABLE_NAME}")
            }
        }

        fun addPaymentTagTable(database: SupportSQLiteDatabase){
            with(database) {
                execSQL("CREATE TABLE IF NOT EXISTS ${PaymentTagTable.TABLE_NAME} (${PaymentTagTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${PaymentTagTable.TITLE} TEXT NOT NULL)")
            }
        }

        fun addPaymentTagToPaymentTable(database: SupportSQLiteDatabase){
            with(database) {
                execSQL("CREATE TABLE IF NOT EXISTS ${PaymentTagTable.TABLE_NAME} (${PaymentTagTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${PaymentTagTable.TITLE} TEXT NOT NULL)")
            }
        }

        database.apply {
            migrateCategoriesTable(this)
            migratePaymentTable(this)
            addPaymentTagTable(this)
        }
    }
}

//DB v1
private object MotDbV1Constants {
    const val DATE_FORMAT = "yyyy-MM-dd"
}

private object PaymentTableV1 {
    const val TABLE_NAME = "payments"

    const val ID = "_id"
    const val TITLE = "title"
    const val SUMMARY = "summary"
    const val CATEGORY_ID = "category_id"
    const val DATE = "date"
    const val COST = "cost"
}

private object CategoryTableV1 {
    const val TABLE_NAME = "categories"

    const val ID = "_id"
    const val NAME = "category_name"
}