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

    // TODO: add "Pick date format" to settings.
    /**
     * @param time epoch time in milliseconds
     * @param timeZone time zone. If null, default system time zone is used
     * @param dateTimeFormatter date formatter, If null, [DateTimeFormatter.ISO_LOCAL_DATE] format is used
     * @return formatted time as string
     * @see [TimeZone]
     * @see [DateTimeFormatter]
     */
    fun execute(
        time: Long,
        timeZone: TimeZone? = null,
        dateTimeFormatter: DateTimeFormatter? = null
    ): String? {
        return Instant.fromEpochMilliseconds(time)
            .toLocalDateTime(timeZone ?: TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()
            .format(dateTimeFormatter ?: DateTimeFormatter.ISO_LOCAL_DATE)
    }
}
