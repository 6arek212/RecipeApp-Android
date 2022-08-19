package com.example.recipe2.presentation.ui.recipe_list

sealed class RecipeListEvent {
    object NewSearch : RecipeListEvent()
    object NextPageEvent : RecipeListEvent()
    object RestoreStateEvent : RecipeListEvent()
}