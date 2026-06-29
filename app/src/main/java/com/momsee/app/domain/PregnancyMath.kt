package com.momsee.app.domain

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object PregnancyMath {
    const val TOTAL_PREGNANCY_DAYS = 280L // 40 weeks

    fun calculateDaysPassed(lmpDate: LocalDate, today: LocalDate = LocalDate.now()): Long {
        return ChronoUnit.DAYS.between(lmpDate, today)
    }

    fun calculateWeeksPassed(daysPassed: Long): Int {
        return (daysPassed / 7).toInt()
    }

    fun calculateExtraDaysPassed(daysPassed: Long): Int {
        return (daysPassed % 7).toInt()
    }

    fun calculateCurrentWeek(daysPassed: Long): Int {
        return (daysPassed / 7).toInt() + 1
    }

    fun calculateDaysRemaining(daysPassed: Long): Long {
        return (TOTAL_PREGNANCY_DAYS - daysPassed).coerceAtLeast(0)
    }

    fun calculateProgress(daysPassed: Long): Float {
        return (daysPassed.toFloat() / TOTAL_PREGNANCY_DAYS.toFloat()).coerceIn(0f, 1f)
    }

    fun calculateDueDate(lmpDate: LocalDate): LocalDate {
        return lmpDate.plusDays(TOTAL_PREGNANCY_DAYS)
    }

    fun getTrimester(week: Int): Int {
        return when {
            week <= 13 -> 1
            week <= 27 -> 2
            else -> 3
        }
    }
}
