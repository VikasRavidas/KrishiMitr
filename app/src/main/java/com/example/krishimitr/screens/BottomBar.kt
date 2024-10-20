package com.example.krishimitr.screens

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.krishimitr.Screen


@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute:String?
) {
    val items = listOf("Home", "Profile", "Contact","History")
    val icons = listOf(Icons.Default.Home, Icons.Default.People, Icons.Default.Call, Icons.Default.Cached)
    val selectedIndex = when (currentRoute) {
        Screen.Home.route -> 0
        Screen.Profile.route -> 1
        Screen.Contact.route -> 2
      Screen.History.route -> 3
        else -> 0 // Default to "Home" if route is unknown
    }

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(120.dp)
    ) {

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item)
                },
                label = {
                    Text(
                        text = item,
                        style = TextStyle(
                            fontSize = 9.sp,
                            // color = Color.Black
                        )
                    )
                },
                selected = selectedIndex == index,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Black
                ),

//                selectedContentColor = LocalContentColor.current,
//              unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                onClick = {
                    when (item) {
                        "Home" -> navController.navigate(Screen.Home.route)
//                        {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
                        "Profile" -> navController.navigate(Screen.Profile.route)
//                        {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
                        "Contact" -> navController.navigate(Screen.Contact.route)
//                        {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
                       "History" -> navController.navigate(Screen.History.route)
//                        {
//                            popUpTo(navController.graph.startDestinationId)
//                            launchSingleTop = true
//                        }
                    }

                }
            )
        }
    }
}

