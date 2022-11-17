package dev.nelson.mot.main.domain.use_case.date_and_time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Transform epoch mills into string date according to system time zone
 */
class FormatTimeUseCase @Inject constructor() {

    /**
     * @param time epoch time in milliseconds
     * @return formatted time as string
     */
    fun execute(time: Long?): String? { // TODO: add "Pick date format" to settings.
        return time?.let {
            val timeZone = TimeZone.currentSystemDefault()
            val instant: Instant = Instant.fromEpochMilliseconds(time)
            instant.toLocalDateTime(timeZone)
                .toJavaLocalDateTime()
                .format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
    }
}
