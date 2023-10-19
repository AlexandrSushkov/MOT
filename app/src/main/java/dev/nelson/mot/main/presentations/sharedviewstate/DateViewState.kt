package dev.nelson.mot.main.presentations.sharedviewstate

import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.formatMillsToDateText

data class DateViewState(
    val mills: Long = DateUtils.getCurrentDate().time,
    val text: String = mills.formatMillsToDateText(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)
)
