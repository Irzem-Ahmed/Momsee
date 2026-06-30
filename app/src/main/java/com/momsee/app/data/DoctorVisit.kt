package com.momsee.app.data

import kotlinx.serialization.Serializable

@Serializable
data class DoctorVisit(
    val id: String,
    val name: String,
    val date: String, // ISO-8601 format (e.g., "2024-06-30")
    val description: String
)
