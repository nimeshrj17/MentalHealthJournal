package com.example.mentalhealthjournal.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mentalhealthjournal.navigation.NavigationRoutes
import kotlinx.coroutines.launch

data class DrawerItem(val label: String, val icon: ImageVector, val route: String)

val drawerItems = listOf(
    DrawerItem("Journal", Icons.Default.Article, NavigationRoutes.JOURNAL),
    DrawerItem("Add Entry", Icons.Default.Edit, "add_entry"),
    DrawerItem("Settings", Icons.Default.Settings, "settings"),
    DrawerItem("Logout", Icons.Default.ExitToApp, "logout")
)

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        drawerItems.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            drawerState.close()
                            if (item.route == "logout") {
                                // Special case: clear backstack and go to login
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(item.icon, contentDescription = item.label)
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = item.label, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
