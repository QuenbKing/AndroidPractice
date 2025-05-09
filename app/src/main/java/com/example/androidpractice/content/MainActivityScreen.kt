package com.example.androidpractice.content


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidpractice.R
import com.example.androidpractice.ui.theme.AndroidPracticeTheme
import com.example.androidpractice.ui.theme.Typography

@Composable
fun MainActivityScreen() {
    val navController = rememberNavController()

    AndroidPracticeTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    backgroundColor = Color.DarkGray
                ) {
                    BottomNavigationItem(
                        icon = getIcon(
                            icon = Icons.Filled.List,
                            contentDescription = "List"
                        ),
                        label = getLabel(stringResource(R.string.movie_list_label)),
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.List.route)
                        }
                    )

                    BottomNavigationItem(
                        icon = getIcon(
                            icon = Icons.Filled.Favorite,
                            contentDescription = "Favorites"
                        ),
                        label = getLabel(stringResource(R.string.favorites_label)),
                        selected = false,
                        onClick = {
                            navController.navigate(Screen.Favorites.route)
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.List.route,
                Modifier.padding(innerPadding)
            ) {
                composable(route = Screen.Favorites.route) { FavoritesScreen(navController) }
                composable(route = Screen.List.route) { ListScreen(navController) }
                composable(route = Screen.Details.route) { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 0
                    DetailsScreen(navController, movieId)
                }
            }
        }
    }
}

@Composable
fun getIcon(icon: ImageVector, contentDescription: String): @Composable () -> Unit {
    return {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}

@Composable
fun getLabel(label: String): @Composable () -> Unit {
    return {
        Text(
            text = label,
            color = Color.White,
            style = Typography.labelSmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}