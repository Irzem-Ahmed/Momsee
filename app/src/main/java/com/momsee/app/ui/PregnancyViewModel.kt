package com.momsee.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momsee.app.data.DoctorVisit
import com.momsee.app.data.UserPreferencesRepository
import com.momsee.app.domain.PregnancyMath
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

data class PregnancyUiState(
    val lmpDate: LocalDate? = null,
    val darkModeOverride: Boolean? = null,
    val doctorVisits: List<DoctorVisit> = emptyList(),
    val daysPassed: Long = 0,
    val weeksPassed: Int = 0,
    val extraDaysPassed: Int = 0,
    val currentWeek: Int = 1,
    val daysRemaining: Long = 0,
    val progress: Float = 0f,
    val dueDate: LocalDate? = null,
    val isLoading: Boolean = true,
)

class PregnancyViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<PregnancyUiState> = combine(
        repository.lmpDate,
        repository.darkModeOverride,
        repository.doctorVisits
    ) { lmpDate, darkMode, visits ->
        if (lmpDate == null) {
            PregnancyUiState(
                lmpDate = null, 
                darkModeOverride = darkMode, 
                doctorVisits = visits,
                isLoading = false
            )
        } else {
            val daysPassed = PregnancyMath.calculateDaysPassed(lmpDate)
            PregnancyUiState(
                lmpDate = lmpDate,
                darkModeOverride = darkMode,
                doctorVisits = visits.sortedByDescending { it.date },
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

    fun addDoctorVisit(name: String, date: LocalDate, description: String) {
        viewModelScope.launch {
            val visit = DoctorVisit(
                id = UUID.randomUUID().toString(),
                name = name,
                date = date.toString(),
                description = description
            )
            repository.addDoctorVisit(visit)
        }
    }

    fun deleteDoctorVisit(visitId: String) {
        viewModelScope.launch {
            repository.deleteDoctorVisit(visitId)
        }
    }
}
