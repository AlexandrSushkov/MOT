package dev.nelson.mot.main.domain.use_case.date_and_time

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatTimeUseCase @Inject constructor() {

    /**
     * Transform epoch time in milliseconds to string
     *
     * @param time epoch time in milliseconds
     * @return formatted time as string
     */
    suspend fun execute(time: Long?): String {
        return time?.let {
            val systemTZ = TimeZone.currentSystemDefault()
            val instant: Instant = Instant.fromEpochMilliseconds(time)
            val date: LocalDateTime = instant.toLocalDateTime(systemTZ)
            val javaDate = date.toJavaLocalDateTime()
            javaDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }.orEmpty()
    }
}