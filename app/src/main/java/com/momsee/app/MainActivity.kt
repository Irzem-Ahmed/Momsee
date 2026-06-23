package com.momsee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.edit
import com.momsee.app.data.UserPreferences
import com.momsee.app.data.dataStore
import com.momsee.app.ui.screens.HomeScreen
import com.momsee.app.ui.screens.LandingScreen
import com.momsee.app.ui.screens.MilestonesScreen
import com.momsee.app.ui.screens.SettingsScreen
import com.momsee.app.ui.screens.TimelineScreen
import com.momsee.app.ui.theme.MomseeTheme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            
            // Collect theme preference
            val darkModeOverride by remember(context) {
                context.dataStore.data
                    .map { preferences -> preferences[UserPreferences.DARK_MODE_KEY] }
            }.collectAsState(initial = null)

            // Final theme decision: preference or system
            val useDarkTheme = darkModeOverride ?: isSystemInDarkTheme()

            MomseeTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MomseeApp(darkModeOverride)
                }
            }
        }
    }
}

@Composable
fun MomseeApp(darkModeOverride: Boolean?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Collect LMP date
    val lmpDateString by remember(context) {
        context.dataStore.data
            .map { preferences -> preferences[UserPreferences.LMP_DATE_KEY] }
    }.collectAsState(initial = null)

    // Navigation state: "loading", "landing", "home", "timeline", "milestones", "settings"
    var currentScreen by remember { mutableStateOf("loading") }
    
    LaunchedEffect(lmpDateString) {
        if (currentScreen == "loading") {
            currentScreen = if (lmpDateString == null) "landing" else "home"
        }
    }

    val onDateSelected: (LocalDate) -> Unit = { date ->
        val dateStr = date.toString()
        scope.launch {
            context.dataStore.edit { preferences ->
                preferences[UserPreferences.LMP_DATE_KEY] = dateStr
            }
        }
        currentScreen = "home"
    }

    val onToggleDarkMode: (Boolean?) -> Unit = { isDark ->
        scope.launch {
            context.dataStore.edit { preferences ->
                if (isDark == null) {
                    preferences.remove(UserPreferences.DARK_MODE_KEY)
                } else {
                    preferences[UserPreferences.DARK_MODE_KEY] = isDark
                }
            }
        }
    }

    if (currentScreen == "loading") {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            bottomBar = {
                if (currentScreen != "landing") {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, contentDescription = stringResource(R.string.nav_home)) },
                            label = { Text(stringResource(R.string.nav_home)) },
                            selected = currentScreen == "home",
                            onClick = { currentScreen = "home" },
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.DateRange, contentDescription = stringResource(R.string.nav_timeline)) },
                            label = { Text(stringResource(R.string.nav_timeline)) },
                            selected = currentScreen == "timeline",
                            onClick = { currentScreen = "timeline" },
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Star, contentDescription = stringResource(R.string.nav_milestones)) },
                            label = { Text(stringResource(R.string.nav_milestones)) },
                            selected = currentScreen == "milestones",
                            onClick = { currentScreen = "milestones" },
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_settings)) },
                            label = { Text(stringResource(R.string.nav_settings)) },
                            selected = currentScreen == "settings",
                            onClick = { currentScreen = "settings" },
                        )
                    }
                }
            },
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                when (currentScreen) {
                    "landing" -> LandingScreen(onDateSelected)
                    "home" -> HomeScreen(lmpDateString)
                    "timeline" -> TimelineScreen(lmpDateString)
                    "milestones" -> MilestonesScreen(lmpDateString)
                    "settings" -> SettingsScreen(
                        lmpDateString = lmpDateString,
                        onDateSelected = onDateSelected,
                        darkModeOverride = darkModeOverride,
                        onToggleDarkMode = onToggleDarkMode,
                    )
                }
            }
        }
    }
}
