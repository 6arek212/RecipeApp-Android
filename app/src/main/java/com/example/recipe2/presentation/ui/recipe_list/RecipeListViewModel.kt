package com.example.recipe2.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe2.domain.model.Recipe
import com.example.recipe2.repository.RecipeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


const val PAGE_SIZE = 30

private const val TAG = "RecipeListViewModel"

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    @Named("auth_token") private val token: String,
   private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())

    val query = mutableStateOf("")

    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)

    var categoryScrollPosition: Int = 0

    val loading = mutableStateOf(false)

    val page = mutableStateOf(1)

    private var recipeListScrollPos = 0


    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }

        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }

        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }

        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if (recipeListScrollPos != 0) {
            onTriggerEvent(RecipeListEvent.RestoreStateEvent)
        } else {
            onTriggerEvent(RecipeListEvent.NewSearch)
        }

    }


    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {

            when (event) {
                is RecipeListEvent.NewSearch -> {
                    Log.d(TAG, "onTriggerEvent: ${Thread.currentThread().name}")
                    newSearch()
                }

                is RecipeListEvent.NextPageEvent -> {
                    nextPage()
                }

                is RecipeListEvent.RestoreStateEvent -> {
                    restoreState()
                }

            }
        }
    }

    private suspend fun restoreState() {
        loading.value = true
        val results: MutableList<Recipe> = mutableListOf()
        for (p in 1..page.value) {
            val result = repository.search(
                token,
                p,
                query.value
            )

            results.addAll(result)
        }

        recipes.value = results
        loading.value = false
    }


    private suspend fun newSearch() {
        loading.value = true
        resetSearchState()
        val result = repository.search(
            token = token,
            page = 1,
            query = query.value
        )
        recipes.value = result
        loading.value = false
    }


    private suspend fun nextPage() {
        viewModelScope.launch {
            if (recipeListScrollPos + 1 >= (page.value * PAGE_SIZE)) {
                loading.value = true
                incrementPage()

                if (page.value > 1) {
                    val result = repository.search(
                        token = token,
                        page = page.value,
                        query = query.value
                    )
                    appendRecipes(result)
                }

                loading.value = false
            }
        }
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipesList: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipesList)
        this.recipes.value = current
    }


    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPos(pos: Int) {
        setListScrollPosition(pos = pos)
    }


    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPos(0)
        if (selectedCategory.value?.value != query.value)
            clearSelectedCategory()
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChange(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(pos: Int) {
        this.categoryScrollPosition = pos
    }


    private fun setListScrollPosition(pos: Int) {
        recipeListScrollPos = pos
        savedStateHandle.set(STATE_KEY_LIST_POSITION, pos)
    }


    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(q: String) {
        this.query.value = q
        savedStateHandle.set(STATE_KEY_QUERY, q)
    }

}