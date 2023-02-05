package com.example.titkov.domain.model

data class Film(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val bannerUrl: String = "",
    val year: String = "",
    val genres: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val isInFavorites: Boolean = false,
)