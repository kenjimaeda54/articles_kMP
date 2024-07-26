package com.example.articleskmp.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(FormatStringsInDatetimeFormats::class)
fun formatDate(value: String): String { val second = 1
    val minute = 60 * second
    val hour = 60 * minute
    val day = 24 * hour
    val month = 30 * day
    val year = 12 * month
    val today = Clock.System.now()
    val todayMilesSeconds = today.toEpochMilliseconds()
    val date = Instant.parse(value)
    val dateMilesSeconds = date.toEpochMilliseconds()
   // val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    return try {

        val diff = (todayMilesSeconds - dateMilesSeconds) / 1000


        return when {
            diff < minute -> "Agora"
            diff < 60 * minute -> "Um minuto atrás"
            diff < 2 * hour -> "Uma hora atrás"
            diff < 24 * hour -> "${diff / hour} horas atrás"
            diff < 30 * day -> "${diff / day} dias atrás"
            diff < 2 * month -> "Um mes atrás"
            diff < 2 * year -> "Um ano atrás"
            else -> "${diff / year} anos atrás"
        }

    } catch (exception: Exception) {
        "Error ao formatar data"
    }

}
