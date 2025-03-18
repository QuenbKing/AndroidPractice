package com.example.androidpractice.content


import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                        icon = {
                            Icon(
                                Icons.Filled.List,
                                contentDescription = "List",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Список фильмов",
                                color = Color.White,
                                style = Typography.labelSmall
                            )

                        },
                        selected = false,
                        onClick = {
                            navController.navigate("list")
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "list",
                Modifier.padding(innerPadding)
            ) {
                composable("list") { ListScreen(navController) }
                composable("details/{movieId}") { backStackEntry ->
                    val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: 0
                    DetailsScreen(navController, movieId)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}