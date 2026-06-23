package com.momsee.app.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momsee.app.R
import com.momsee.app.ui.components.DatePickerButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(
    lmpDateString: String?,
    onDateSelected: (LocalDate) -> Unit,
    darkModeOverride: Boolean?,
    onToggleDarkMode: (Boolean?) -> Unit,
) {
    val lmpDate = lmpDateString?.let { LocalDate.parse(it) }
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    val isSystemDark = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.nav_settings),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp),
        )

        // Pregnancy Data Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.settings_pregnancy_data), 
                    fontWeight = FontWeight.Bold, 
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.settings_lmp_label), 
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = lmpDate?.format(formatter) ?: stringResource(R.string.settings_not_set),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                DatePickerButton(stringResource(R.string.settings_change_date), onDateSelected)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Appearance Section
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.settings_appearance), 
                    fontWeight = FontWeight.Bold, 
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.settings_dark_mode), 
                            fontWeight = FontWeight.Medium,
                        )
                        Text(
                            text = if (darkModeOverride == null) {
                                val systemMode = if (isSystemDark) "Dark" else "Light"
                                stringResource(R.string.settings_system_mode, systemMode)
                            } else stringResource(R.string.settings_forced_mode),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = darkModeOverride ?: isSystemDark,
                        onCheckedChange = { onToggleDarkMode(it) },
                    )
                }

                if (darkModeOverride != null) {
                    TextButton(
                        onClick = { onToggleDarkMode(null) },
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Text(stringResource(R.string.settings_reset_theme))
                    }
                }
            }
        }
    }
}
