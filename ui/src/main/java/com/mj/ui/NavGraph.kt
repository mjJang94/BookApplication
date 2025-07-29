package com.mj.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mj.presentation.search.model.BookModel
import com.mj.presentation.search.search.BookSearchViewModel
import com.mj.ui.screen.SearchScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main_graph") {
        navigation(startDestination = "search", route = "main_graph") {
            composable("search") { backStackEntry ->

                val searchViewModel: BookSearchViewModel = hiltViewModel(
                    navController.getBackStackEntry("main_graph")
                )

                SearchScreen(viewModel = searchViewModel)
            }
        }
    }
}