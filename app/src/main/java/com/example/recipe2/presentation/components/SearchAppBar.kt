package com.example.recipe2.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recipe2.presentation.ui.recipe_list.FoodCategory
import com.example.recipe2.presentation.ui.recipe_list.RecipeListEvents
import kotlinx.coroutines.launch


@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onTriggerEvent: (RecipeListEvents) -> Unit,
    categoryScrollPosition: Int,
    selectedCategory: FoodCategory?,
    onSelectedCategoryChange: (String) -> Unit,
    onChangeCategoryScrollPosition: (Int) -> Unit,
    listState: LazyListState

) {
    val categoryScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.primary,
        elevation = 8.dp,
    ) {


        Column() {


            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .padding(8.dp),
                    value = query,
                    onValueChange = { newValue ->
                        onQueryChanged(newValue)
                    },
                    label = {
                        Text(text = "Search")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = ""
                        )
                    },
                    keyboardActions = KeyboardActions(onSearch = {
                        onTriggerEvent(RecipeListEvents.NewSearch())
                        focusManager.clearFocus()
                    }),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        textColor = MaterialTheme.colors.onSurface,
                    ),
                )
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(categoryScrollState)
                    .padding(start = 8.dp, bottom = 8.dp),
            ) {
                coroutineScope.launch {
                    categoryScrollState.animateScrollTo(categoryScrollPosition)
                }

                for (category in FoodCategory.values()) {
                    FoodCategoryChip(
                        category = category.value,
                        onExecuteSearch = {
                            onTriggerEvent(RecipeListEvents.NewSearch())
                            coroutineScope.launch {
                                listState.animateScrollToItem(index = 0)
                            }
                        },
                        onSelectedCategoryChanged = {
                            onSelectedCategoryChange(it)
                            onChangeCategoryScrollPosition(categoryScrollState.value)
                        },
                        isSelected = selectedCategory == category
                    )
                }
            }

        }
    }


}