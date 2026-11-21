package com.example.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.viewmodel.KnowledgeViewModel

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Explore : Screen("explore", "Explore", Icons.Default.Search)
    object Categories : Screen("categories", "Categories", Icons.Default.Category)
    object Create : Screen("create?articleId={articleId}", "Create", Icons.Default.AddCircle) {
        fun createRoute(articleId: Long? = null) = if (articleId != null) "create?articleId=$articleId" else "create"
    }
    object Saved : Screen("saved", "Saved", Icons.Default.Bookmark)
    object Contributions : Screen("contributions", "Contributions", Icons.Default.Person)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
    object ArticleDetail : Screen("article/{articleId}", "Article Detail", Icons.Default.Article) {
        fun createRoute(articleId: Long) = "article/$articleId"
    }
    object CategoryArticles : Screen("category_articles/{categoryName}", "Category Articles", Icons.Default.Folder) {
        fun createRoute(categoryName: String) = "category_articles/$categoryName"
    }
    object RevisionHistory : Screen("revisions/{articleId}", "Revision History", Icons.Default.History) {
        fun createRoute(articleId: Long) = "revisions/$articleId"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeNavGraph(viewModel: KnowledgeViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Categories,
        Screen.Saved,
        Screen.Contributions
    )

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onArticleClick = { articleId -> navController.navigate(Screen.ArticleDetail.createRoute(articleId)) },
                    onCategoryClick = { cat -> navController.navigate(Screen.CategoryArticles.createRoute(cat)) },
                    onExploreClick = { navController.navigate(Screen.Explore.route) },
                    onCreateClick = { navController.navigate(Screen.Create.createRoute()) }
                )
            }
            composable(Screen.Explore.route) {
                ExploreScreen(
                    viewModel = viewModel,
                    onArticleClick = { articleId -> navController.navigate(Screen.ArticleDetail.createRoute(articleId)) }
                )
            }
            composable(Screen.Categories.route) {
                CategoriesScreen(
                    onCategoryClick = { cat -> navController.navigate(Screen.CategoryArticles.createRoute(cat)) }
                )
            }
            composable(
                route = Screen.CategoryArticles.route,
                arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                CategoryArticlesScreen(
                    categoryName = categoryName,
                    viewModel = viewModel,
                    onArticleClick = { articleId -> navController.navigate(Screen.ArticleDetail.createRoute(articleId)) },
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(
                route = Screen.ArticleDetail.route,
                arguments = listOf(navArgument("articleId") { type = NavType.LongType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getLong("articleId") ?: 0L
                ArticleDetailScreen(
                    articleId = articleId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { id -> navController.navigate(Screen.Create.createRoute(id)) },
                    onHistoryClick = { id -> navController.navigate(Screen.RevisionHistory.createRoute(id)) }
                )
            }
            composable(
                route = Screen.Create.route,
                arguments = listOf(navArgument("articleId") {
                    type = NavType.LongType
                    defaultValue = -1L
                })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getLong("articleId") ?: -1L
                CreateEditArticleScreen(
                    articleId = if (articleId == -1L) null else articleId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onArticlePublished = { id ->
                        navController.navigate(Screen.ArticleDetail.createRoute(id)) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
            composable(Screen.Saved.route) {
                SavedArticlesScreen(
                    viewModel = viewModel,
                    onArticleClick = { articleId -> navController.navigate(Screen.ArticleDetail.createRoute(articleId)) }
                )
            }
            composable(Screen.Contributions.route) {
                ContributionsScreen(
                    viewModel = viewModel,
                    onArticleClick = { articleId -> navController.navigate(Screen.ArticleDetail.createRoute(articleId)) },
                    onSettingsClick = { navController.navigate(Screen.Settings.route) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(onBackClick = { navController.popBackStack() })
            }
            composable(
                route = Screen.RevisionHistory.route,
                arguments = listOf(navArgument("articleId") { type = NavType.LongType })
            ) { backStackEntry ->
                val articleId = backStackEntry.arguments?.getLong("articleId") ?: 0L
                RevisionHistoryScreen(
                    articleId = articleId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
