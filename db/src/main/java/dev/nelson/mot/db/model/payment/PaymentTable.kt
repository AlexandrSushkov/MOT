package dev.nelson.mot.db.model.payment

object PaymentTable {
    const val TABLE_NAME = "payments"

    const val ID = "payment_id"
    const val TITLE = "payment_name"
    const val SUMMARY = "payment_summary"
    const val CATEGORY_ID_KEY = "payment_category_id_key"
    const val DATE = "payment_date"
    const val DATE_IN_MILLISECONDS = "payment_date_in_milliseconds"
    const val COST = "payment_cost"
}
