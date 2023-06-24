package com.example.s9firebase2023.model

data class PokemonModelResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonModel>
)
