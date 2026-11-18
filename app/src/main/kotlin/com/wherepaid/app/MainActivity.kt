package com.wherepaid.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import com.wherepaid.app.core.designsystem.theme.WherePaidTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wherepaid.app.feature.history.HistoryScreen
import com.wherepaid.app.feature.map.MapScreen
import com.wherepaid.app.feature.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WherePaidTheme {
                WherePaidNav(appContainer)
            }
        }
    }
}

/** 하단 탭. 화면을 추가하면 여기와 [WherePaidNav]의 NavHost에 등록한다. */
private enum class TopLevelDestination(val route: String, val label: String, val icon: ImageVector) {
    MAP("map", "지도", Icons.Filled.Place),
    HISTORY("history", "내역", Icons.AutoMirrored.Filled.List),
    SETTINGS("settings", "설정", Icons.Filled.Settings),
}

@Composable
private fun WherePaidNav(container: AppContainer) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                TopLevelDestination.entries.forEach { destination ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                        onClick = {
                            navController.navigate(destination.route) {
                                // 탭을 오갈 때 백스택이 쌓이지 않게 하고 각 탭의 상태를 유지한다.
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(destination.icon, contentDescription = null) },
                        label = { Text(destination.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = TopLevelDestination.MAP.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(TopLevelDestination.MAP.route) {
                MapScreen(repository = container.transactionRepository)
            }
            composable(TopLevelDestination.HISTORY.route) {
                HistoryScreen(repository = container.transactionRepository)
            }
            composable(TopLevelDestination.SETTINGS.route) {
                SettingsScreen(dataSourceLabel = if (container.usesFakeData) "가짜 데이터" else "기기 DB (Room)")
            }
        }
    }
}
