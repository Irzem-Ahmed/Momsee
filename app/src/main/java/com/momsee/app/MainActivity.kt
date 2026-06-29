package com.momsee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.momsee.app.ui.PregnancyViewModel
import com.momsee.app.ui.navigation.Screen
import com.momsee.app.ui.screens.HomeScreen
import com.momsee.app.ui.screens.LandingScreen
import com.momsee.app.ui.screens.MilestonesScreen
import com.momsee.app.ui.screens.SettingsScreen
import com.momsee.app.ui.screens.TimelineScreen
import com.momsee.app.ui.theme.MomseeTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: PregnancyViewModel = koinViewModel()
            val uiState by viewModel.uiState.collectAsState()

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                MomseeTheme(darkTheme = uiState.darkModeOverride ?: androidx.compose.foundation.isSystemInDarkTheme()) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        MomseeApp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun MomseeApp(viewModel: PregnancyViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val startDestination = if (uiState.lmpDate == null) Screen.Landing else Screen.Home
    val animationSpec = tween<androidx.compose.ui.unit.IntOffset>(durationMillis = 220)
    val fadeSpec = tween<Float>(durationMillis = 220)

    val navigationOrder = listOf(
        Screen.Landing::class,
        Screen.Home::class,
        Screen.Timeline::class,
        Screen.Milestones::class,
        Screen.Settings::class
    )

    fun getTransitionDirection(
        initial: NavBackStackEntry?,
        target: NavBackStackEntry?
    ): Boolean {
        val initialRoute = initial?.destination?.route?.substringBefore("?") ?: return true
        val targetRoute = target?.destination?.route?.substringBefore("?") ?: return true

        val initialIndex = navigationOrder.indexOfFirst { 
            it.qualifiedName == initialRoute || it.simpleName == initialRoute.substringAfterLast(".")
        }
        val targetIndex = navigationOrder.indexOfFirst { 
            it.qualifiedName == targetRoute || it.simpleName == targetRoute.substringAfterLast(".")
        }
        
        if (initialIndex == -1 || targetIndex == -1) return true
        
        return initialIndex <= targetIndex
    }

    Scaffold(
        bottomBar = {
            if (currentDestination?.hasRoute<Screen.Landing>() == false) {
                NavigationBar {
                    val items = listOf(
                        NavigationItem(stringResource(R.string.nav_home), Icons.Default.Home, Screen.Home),
                        NavigationItem(stringResource(R.string.nav_timeline), Icons.Default.DateRange, Screen.Timeline),
                        NavigationItem(stringResource(R.string.nav_milestones), Icons.Default.Star, Screen.Milestones),
                        NavigationItem(stringResource(R.string.nav_settings), Icons.Default.Settings, Screen.Settings),
                    )
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { 
                                Text(
                                    text = item.label,
                                    fontWeight = if (currentDestination.hierarchy.any { it.hasRoute(item.screen::class) }) {
                                        androidx.compose.ui.text.font.FontWeight.Bold
                                    } else {
                                        androidx.compose.ui.text.font.FontWeight.Normal
                                    }
                                ) 
                            },
                            selected = currentDestination.hierarchy.any { it.hasRoute(item.screen::class) },
                            onClick = {
                                navController.navigate(item.screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                val isForward = getTransitionDirection(initialState, targetState)
                if (isForward) {
                    slideInHorizontally(animationSpec) { it } + fadeIn(fadeSpec)
                } else {
                    slideInHorizontally(animationSpec) { -it / 3 } + fadeIn(fadeSpec)
                }
            },
            exitTransition = {
                val isForward = getTransitionDirection(initialState, targetState)
                if (isForward) {
                    slideOutHorizontally(animationSpec) { -it / 3 } + fadeOut(fadeSpec)
                } else {
                    slideOutHorizontally(animationSpec) { it } + fadeOut(fadeSpec)
                }
            },
            popEnterTransition = {
                slideInHorizontally(animationSpec) { -it / 3 } + fadeIn(fadeSpec)
            },
            popExitTransition = {
                slideOutHorizontally(animationSpec) { it } + fadeOut(fadeSpec)
            }
        ) {
            composable<Screen.Landing> {
                LandingScreen(onDateSelected = { 
                    viewModel.updateLmpDate(it)
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Landing) { inclusive = true }
                    }
                })
            }
            composable<Screen.Home> {
                HomeScreen(uiState)
            }
            composable<Screen.Timeline> {
                TimelineScreen(uiState)
            }
            composable<Screen.Milestones> {
                MilestonesScreen(uiState)
            }
            composable<Screen.Settings> {
                SettingsScreen(
                    uiState = uiState,
                    onDateSelected = { viewModel.updateLmpDate(it) },
                    onToggleDarkMode = { viewModel.updateDarkMode(it) },
                )
            }
        }
    }
}

private data class NavigationItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val screen: Screen
)
