package dev.nelson.mot.main.util.constant


object Constants {

    const val DEFAULT_ANIMATION_DELAY = 200L
    const val DEFAULT_ANIMATION_DURATION = 500
    const val SWIPE_TO_DISMISS_THRESHOLD = 125

    const val DATA_STORE_FILE_NAME = "mot_preferences"

    //intent bundle/extras keys
    const val NO_CATEGORY_KEY = "no_category"
    const val PAYMENT_ID_KEY = "payment_id"
    const val CATEGORY_ID_KEY = "category_id"
    const val IS_OPENED_FROM_WIDGET_KEY = "opened_from_widget"

    const val NO_CATEGORY_CATEGORY_ID = -1
    const val PRICE_EXAMPLE = 999999 // 9,999.99
    // format patterns
    const val FILE_PICKER_FORMAT: String = "*/*"
    const val DAY_SHORT_MONTH_YEAR_DATE_PATTERN = "d MMM yyyy"
    const val MONTH_SHORT_YEAR_DATE_PATTERN = "MMM yyyy"
    const val MONTH_YEAR_DATE_PATTERN = "MMMM yyyy"
    const val YEAR_MONTH_DATE_PATTERN = "yyyy MMMM"
}