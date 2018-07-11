package dev.nelson.mot.data.repository

import android.content.Context
import android.database.Cursor
import dev.nelson.mot.db.SQLiteOpenHelperImpl
import dev.nelson.mot.db.model.CategoriesProvider
import dev.nelson.mot.db.model.PaymentsProvider
import dev.nelson.mot.room.model.category.Category
import dev.nelson.mot.room.model.category.CategoryDao
import dev.nelson.mot.room.model.payment.Payment
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class RoomTestRepository @Inject constructor(private val context: Context,
                                             private val categoryDao: CategoryDao) {

    private val categories: MutableList<Category> = ArrayList()
    private val payments: MutableList<Payment> = ArrayList()
    private lateinit var cursor: Cursor

    fun testFun(): Single<String> = Single.just("test")

    fun getCategories(): Flowable<List<Category>> = categoryDao.getAllCategories()

    fun transferCategories() : Completable = Completable.fromAction {
        val helper = SQLiteOpenHelperImpl(context)
        val db = helper.readableDatabase
        val provider = CategoriesProvider()
        val rawQuery = ("select *" + " from " + CategoriesProvider.TABLE_NAME)

        val cursor: Cursor = db.rawQuery(rawQuery, null)

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val title = cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME))
                categories.add(Category(title))
            } while (cursor.moveToNext())
        }
        cursor.close()
        Timber.e(categories.toString())
        categoryDao.addCategories(categories)
    }

    fun transferPayments() : Completable = Completable.fromAction {
        //todo transfer payments data to new database
        val helper = SQLiteOpenHelperImpl(context)
        val db = helper.readableDatabase
        val rawQuery = ("select *" + " from " + PaymentsProvider.TABLE_NAME)

        val cursor: Cursor = db.rawQuery(rawQuery, null)

        if (cursor != null && cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val title = cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME))
                categories.add(Category(title))
            } while (cursor.moveToNext())
        }
        cursor.close()
        Timber.e(categories.toString())
        categoryDao.addCategories(categories)
    }

}
