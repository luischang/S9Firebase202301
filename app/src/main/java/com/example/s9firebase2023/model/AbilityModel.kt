package com.example.s9firebase2023.model

import com.google.gson.annotations.SerializedName

data class AbilityModel(
    @SerializedName("ability")
    val ability: AbilityNameModel
)