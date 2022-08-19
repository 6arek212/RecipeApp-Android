package com.example.recipe2.network.responses

import com.example.recipe2.network.model.RecipeDto
import com.google.gson.annotations.SerializedName

class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,

    @SerializedName("results")
    var recipes: List<RecipeDto>
) {
}