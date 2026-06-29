package com.momsee.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momsee.app.data.UserPreferencesRepository
import com.momsee.app.domain.PregnancyMath
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

data class PregnancyUiState(
    val lmpDate: LocalDate? = null,
    val darkModeOverride: Boolean? = null,
    val daysPassed: Long = 0,
    val weeksPassed: Int = 0,
    val extraDaysPassed: Int = 0,
    val currentWeek: Int = 1,
    val daysRemaining: Long = 0,
    val progress: Float = 0f,
    val dueDate: LocalDate? = null,
    val isLoading: Boolean = true
)

class PregnancyViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<PregnancyUiState> = combine(
        repository.lmpDate,
        repository.darkModeOverride
    ) { lmpDate, darkMode ->
        if (lmpDate == null) {
            PregnancyUiState(lmpDate = null, darkModeOverride = darkMode, isLoading = false)
        } else {
            val daysPassed = PregnancyMath.calculateDaysPassed(lmpDate)
            PregnancyUiState(
                lmpDate = lmpDate,
                darkModeOverride = darkMode,
                daysPassed = daysPassed,
                weeksPassed = PregnancyMath.calculateWeeksPassed(daysPassed),
                extraDaysPassed = PregnancyMath.calculateExtraDaysPassed(daysPassed),
                currentWeek = PregnancyMath.calculateCurrentWeek(daysPassed),
                daysRemaining = PregnancyMath.calculateDaysRemaining(daysPassed),
                progress = PregnancyMath.calculateProgress(daysPassed),
                dueDate = PregnancyMath.calculateDueDate(lmpDate),
                isLoading = false
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PregnancyUiState()
    )

    fun updateLmpDate(date: LocalDate) {
        viewModelScope.launch {
            repository.updateLmpDate(date)
        }
    }

    fun updateDarkMode(isDark: Boolean?) {
        viewModelScope.launch {
            repository.updateDarkMode(isDark)
        }
    }
}
