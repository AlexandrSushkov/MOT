package dev.nelson.mot.main.data.repository

import android.content.Context
import dev.nelson.mot.main.data.room.model.payment.Payment

class DBTransferRepository(var context: Context)  {

    private val payments: MutableList<Payment> = ArrayList()

    /**
     * transfer payments data to new database
     */
//    fun transferPayments() {
//        val helper = dev.nelson.mot.legacy.db.SQLiteOpenHelperImpl(context)
//        val db = helper.readableDatabase
//        val rawQuery = ("select *" + " from " + PaymentsProvider.TABLE_NAME)
//
//        val cursor: Cursor = db.rawQuery(rawQuery, null)
//
//        if (cursor.count > 0) {
//            cursor.moveToFirst()
//            do {
//                val title = cursor.getString(cursor.getColumnIndex(PaymentsProvider.Columns.TITLE))
//                val categoryId = cursor.getInt(cursor.getColumnIndex(PaymentsProvider.Columns.CATEGORY_ID))
//                val cost = cursor.getLong(cursor.getColumnIndex(PaymentsProvider.Columns.COST))
//                payments.add(Payment(title = title, categoryId =  categoryId, cost = cost))
//            } while (cursor.moveToNext())
//        }
//        cursor.close()
//        Timber.e("payments: $payments")
////        categoryDao.addCategories(categories)
//    }

}
