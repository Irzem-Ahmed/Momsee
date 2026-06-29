package com.momsee.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Landing : Screen
    @Serializable
    data object Home : Screen
    @Serializable
    data object Timeline : Screen
    @Serializable
    data object Milestones : Screen
    @Serializable
    data object Settings : Screen
}
