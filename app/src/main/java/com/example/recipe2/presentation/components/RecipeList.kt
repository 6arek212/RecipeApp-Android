package com.example.recipe2.presentation.components

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.recipe2.R
import com.example.recipe2.domain.model.Recipe
import com.example.recipe2.presentation.ui.recipe_list.PAGE_SIZE
import com.example.recipe2.presentation.ui.recipe_list.RecipeListEvent


@Composable
fun RecipeList(
    isLoading: Boolean,
    recipes: List<Recipe>,
    page: Int,
    onChangeRecipeScrollPos: (Int) -> Unit,
    onNextPage: () -> Unit,
    listState: LazyListState,
    navController: NavController
) {

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        LazyColumn(state = listState) {
            itemsIndexed(
                items = recipes
            ) { index, recipe ->
                onChangeRecipeScrollPos(index)

                if (index + 1 >= page * PAGE_SIZE && !isLoading) {
                    onNextPage()
                }
                RecipeCard(recipe = recipe, onClick = {
                    recipe.id?.let {
                        val bundle = Bundle()
                        bundle.putInt("recipeId", recipe.id)
                        navController.navigate(
                            R.id.action_recipeListFragment_to_recipeFragment,
                            bundle
                        )
                    }
                })
            }
        }

        CircularProgressBar(isDisplayed = isLoading)
    }


}