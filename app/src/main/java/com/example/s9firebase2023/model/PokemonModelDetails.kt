package com.example.s9firebase2023.model

import com.google.gson.annotations.SerializedName

data class PokemonModelDetails(
    @SerializedName("name")
    val name: String,

    @SerializedName("sprites")
    val sprites: Sprites,

    @SerializedName("abilities")
    val abilities: List<AbilityModel>

)
