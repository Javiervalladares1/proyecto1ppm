package com.example.proyecto1ppm.data

data class Suggestion(
    val id: Int,
    val title: String,
    val description: String,
    val type: SuggestionType // Solo o Grupo
)

enum class SuggestionType {
    SOLO,
    GROUP
}
