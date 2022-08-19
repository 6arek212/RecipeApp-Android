package com.example.recipe2.repository

import com.example.recipe2.domain.model.Recipe
import com.example.recipe2.domain.util.DomainMapper
import com.example.recipe2.network.RecipeService
import com.example.recipe2.network.model.RecipeDto
import com.example.recipe2.network.model.RecipeDtoMapper
import javax.inject.Inject

class RecipeRepositoryImpl
@Inject
constructor(
    private val recipeService: RecipeService,
    private val mapper: RecipeDtoMapper
) : RecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        return mapper.toDomainList(recipeService.search(token, page, query).recipes)
    }

    override suspend fun get(token: String, id: Int): Recipe {
        return mapper.mapToDomainModel(recipeService.get(token, id))
    }
}