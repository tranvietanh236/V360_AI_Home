package com.homedesign.interiordesign.aihome.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Year
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Calendar
import java.util.Locale

object DateUtils {

    private val zone = ZoneId.systemDefault()

    fun nowMillis() = System.currentTimeMillis()

    fun today(): String =
        LocalDate.now(zone).toString() // yyyy-MM-dd

    fun currentWeek(): String {
        val now = LocalDate.now(zone)
        val week = now.get(WeekFields.ISO.weekOfWeekBasedYear())
        return "${now.year}-$week"
    }

    fun currentMonth(): String =
        YearMonth.now(zone).toString() // yyyy-MM

    fun currentYear(): Int =
        Year.now(zone).value

    fun getCurrentDate(): String =
        LocalDateTime.now(zone)
            .format(
                DateTimeFormatter.ofPattern(
                    "EEEE, d MMM yyyy",
                    Locale.ENGLISH
                )
            )

    fun Long.formatTimestamp(): String {
        return Instant.ofEpochMilli(this)
            .atZone(zone)
            .format(
                DateTimeFormatter.ofPattern(
                    "MMM d, yyyy | HH:mm",
                    Locale.ENGLISH
                )
            )
    }

    fun Long.formatTodayTime(): String {
        val now = LocalDate.now(zone)
        val time = Instant.ofEpochMilli(this).atZone(zone)
        val recordDate = time.toLocalDate()

        return if (recordDate == now) {
            // Nếu là hôm nay
            "Today, " + time.format(DateTimeFormatter.ofPattern("HH:mm"))
        } else {
            // Nếu không phải hôm nay, hiển thị ngày đầy đủ
            time.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm", Locale.ENGLISH))
        }
    }

    /**
     * Format timestamp to time only (HH:mm)
     * Example: "15:30"
     */
    fun Long.formatTimeOnly(): String {
        return Instant.ofEpochMilli(this)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH))
    }

    /**
     * Format timestamp to full date with time
     * Example: "Monday, Dec 22 2025 | 15:30"
     */
    fun Long.formatFullDateTime(): String {
        return Instant.ofEpochMilli(this)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy | HH:mm", Locale.ENGLISH))
    }

    /**
     * Format timestamp to date with day of week
     * Example: "Monday, Dec 21"
     */
    fun Long.formatDateWithDayOfWeek(): String {
        return Instant.ofEpochMilli(this)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH))
    }

    /**
     * Format timestamp to short date
     * Example: "Dec 22"
     */
    fun Long.formatShortDate(): String {
        return Instant.ofEpochMilli(this)
            .atZone(zone)
            .format(DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH))
    }

    /**
     * Format LocalDate to date with day of week
     * Example: "Monday, Dec 21"
     */
    fun LocalDate.formatDateWithDayOfWeek(): String {
        return this.format(DateTimeFormatter.ofPattern("EEEE, MMM dd", Locale.ENGLISH))
    }

    /**
     * Format LocalDate to short date
     * Example: "Dec 22"
     */
    fun LocalDate.formatShortDate(): String {
        return this.format(DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH))
    }

    /**
     * Format LocalDate to full date without time
     * Example: "Monday, Dec 22 2025"
     */
    fun LocalDate.formatFullDate(): String {
        return this.format(DateTimeFormatter.ofPattern("EEEE, MMM dd yyyy", Locale.ENGLISH))
    }

    /**
     * Format LocalDate to month and year
     * Example: "December 2025"
     */
    fun LocalDate.formatMonthYear(): String {
        return this.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH))
    }

    /**
     * Format LocalDate to short date with year
     * Example: "Dec 22, 2025"
     */
    fun LocalDate.formatShortDateWithYear(): String {
        return this.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH))
    }

    /**
     * Format week range from Calendar (Monday start of week)
     * @param weekStart Calendar pointing to Monday of the week
     * @return Formatted string like "Dec 01 - Dec 07" or "Nov 28 - Dec 04" if spans months
     */
    fun formatWeekRange(weekStart: Calendar): String {
        val formatter = DateTimeFormatter.ofPattern("MMM dd", Locale.ENGLISH)

        // Convert Calendar to LocalDate for Monday (start of week)
        val monday = LocalDate.of(
            weekStart.get(Calendar.YEAR),
            weekStart.get(Calendar.MONTH) + 1,
            weekStart.get(Calendar.DAY_OF_MONTH)
        )

        // Sunday is 6 days after Monday
        val sunday = monday.plusDays(6)

        val startFormatted = monday.format(formatter)
        val endFormatted = sunday.format(formatter)

        return "$startFormatted - $endFormatted"
    }
}
