package com.example.titkov.domain.model

data class FilmPreview(
    val id: Int = 0,
    val name: String = "",
    val bannerUrl: String = "",
    val year: String = "",
    val genre: String = "",
    val isInFavorites: Boolean = false,
) {
    fun matchesQuery(query: String): Boolean {
        return name.lowercase().contains(query.lowercase())
    }
}