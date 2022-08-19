package com.example.recipe2.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.recipe2.presentation.BaseApplication
import com.example.recipe2.presentation.components.CircularProgressBar
import com.example.recipe2.presentation.components.RecipeCard
import com.example.recipe2.presentation.components.RecipeList
import com.example.recipe2.presentation.components.SearchAppBar
import com.example.recipe2.presentation.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "RecipeListFragment"

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(darkTheme = application.isDark.value) {

                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value
                    val selectedCategory = viewModel.selectedCategory.value
                    val isLoading = viewModel.loading.value

                    val listState = rememberLazyListState()
                    val page = viewModel.page.value


                    Column(
                        modifier = Modifier.background(color = MaterialTheme.colors.background)
                    ) {

                        SearchAppBar(
                            query = query,
                            onQueryChanged = viewModel::onQueryChanged,
                            onNewSearch = { viewModel.onTriggerEvent(RecipeListEvent.NewSearch) },
                            categoryScrollPosition = viewModel.categoryScrollPosition,
                            selectedCategory = selectedCategory,
                            onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                            onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                            listState = listState,
                            onToggleTheme = {
                                application.toggleTheme()
                            }
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        RecipeList(
                            isLoading = isLoading,
                            recipes = recipes,
                            page = page,
                            onChangeRecipeScrollPos = viewModel::onChangeRecipeScrollPos,
                            onNextPage = { viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent) },
                            listState = listState,
                            navController = findNavController()
                        )


                    }

                }
            }
        }


    }


}