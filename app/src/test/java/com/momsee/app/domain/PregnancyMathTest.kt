package com.momsee.app.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class PregnancyMathTest {

    @Test
    fun `calculateDaysPassed returns correct days`() {
        val lmp = LocalDate.of(2024, 1, 1)
        val today = LocalDate.of(2024, 1, 15)
        assertEquals(14L, PregnancyMath.calculateDaysPassed(lmp, today))
    }

    @Test
    fun `calculateWeeksPassed returns correct weeks`() {
        assertEquals(2, PregnancyMath.calculateWeeksPassed(14))
        assertEquals(2, PregnancyMath.calculateWeeksPassed(20))
        assertEquals(3, PregnancyMath.calculateWeeksPassed(21))
    }

    @Test
    fun `calculateExtraDaysPassed returns correct extra days`() {
        assertEquals(0, PregnancyMath.calculateExtraDaysPassed(14))
        assertEquals(6, PregnancyMath.calculateExtraDaysPassed(20))
    }

    @Test
    fun `calculateCurrentWeek returns correct week`() {
        assertEquals(1, PregnancyMath.calculateCurrentWeek(0))
        assertEquals(1, PregnancyMath.calculateCurrentWeek(6))
        assertEquals(2, PregnancyMath.calculateCurrentWeek(7))
        assertEquals(14, PregnancyMath.calculateCurrentWeek(96)) // 13w 5d -> 14th week
    }

    @Test
    fun `calculateDueDate returns exactly 280 days after LMP`() {
        val lmp = LocalDate.of(2024, 1, 1)
        val expected = lmp.plusDays(280)
        assertEquals(expected, PregnancyMath.calculateDueDate(lmp))
    }

    @Test
    fun `getTrimester returns correct trimester`() {
        assertEquals(1, PregnancyMath.getTrimester(1))
        assertEquals(1, PregnancyMath.getTrimester(13))
        assertEquals(2, PregnancyMath.getTrimester(14))
        assertEquals(2, PregnancyMath.getTrimester(27))
        assertEquals(3, PregnancyMath.getTrimester(28))
        assertEquals(3, PregnancyMath.getTrimester(40))
    }
}
