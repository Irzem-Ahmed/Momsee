package com.momsee.app.di

import com.momsee.app.data.UserPreferencesRepository
import com.momsee.app.data.dataStore
import com.momsee.app.ui.PregnancyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { androidContext().dataStore }
    single { UserPreferencesRepository(get()) }
    viewModelOf(::PregnancyViewModel)
}
