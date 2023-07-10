package dev.nelson.mot.main.presentations.shared_view_state

import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.convertMillisecondsToDate

data class DateViewState(
    val mills: Long = DateUtils.getCurrentDate().time,
    val text: String = mills.convertMillisecondsToDate(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)
)
