package com.momsee.app.ui.screens

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
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun HomeScreen(lmpDateString: String?) {
    val lmpDate = lmpDateString?.let { LocalDate.parse(it) }
    val today = LocalDate.now()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header and Greeting
        Text(
            text = stringResource(R.string.nav_home),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 24.dp)
                .align(Alignment.CenterHorizontally),
        )
        
        Text(
            text = stringResource(R.string.home_greeting),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 32.dp),
        )

        if (lmpDate != null) {
            val totalDays = 280L // Standard 40 weeks
            val daysPassed = ChronoUnit.DAYS.between(lmpDate, today)
            val daysRemaining = totalDays - daysPassed
            
            val weeksPassed = (daysPassed / 7).toInt()
            val extraDaysPassed = (daysPassed % 7).toInt()
            
            val progress = (daysPassed.toFloat() / totalDays.toFloat()).coerceIn(0f, 1f)

            // Progress Section
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.home_you_are),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                
                Text(
                    text = stringResource(R.string.home_weeks_and_days, weeksPassed, extraDaysPassed),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                
                Text(
                    text = stringResource(R.string.home_pregnant),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Visual Progress Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            stringResource(R.string.home_conception),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        Text(
                            stringResource(R.string.home_due_date_label),
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Countdown Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.home_countdown_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            CountdownItem(
                                daysRemaining.toString(),
                                stringResource(R.string.home_days),
                            )
                            CountdownItem(
                                (daysRemaining / 7).toString(),
                                stringResource(R.string.home_weeks),
                            )
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.home_no_date))
            }
        }
    }
}

@Composable
fun CountdownItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}
