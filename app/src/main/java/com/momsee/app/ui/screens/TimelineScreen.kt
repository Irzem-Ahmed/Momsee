package com.momsee.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momsee.app.R
import com.momsee.app.ui.PregnancyUiState
import com.momsee.app.ui.components.TimelineCard
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(uiState: PregnancyUiState) {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    val weekFormatter = DateTimeFormatter.ofPattern("EEE, MMM dd")

    var selectedTrimester by remember { mutableStateOf<Int?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(value = false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.nav_timeline),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp),
        )

        if (uiState.lmpDate != null) {
            val secondTrimesterStart = uiState.lmpDate.plusDays(91) // 13 weeks
            val thirdTrimesterStart = uiState.lmpDate.plusDays(189) // 27 weeks

            TimelineCard(
                title = stringResource(R.string.milestones_first_trimester),
                dateInfo = stringResource(R.string.timeline_first_trimester_info, uiState.lmpDate.format(formatter)),
            ) {
                selectedTrimester = 1
                showSheet = true
            }
            Spacer(modifier = Modifier.height(16.dp))
            TimelineCard(
                title = stringResource(R.string.milestones_second_trimester),
                dateInfo = stringResource(R.string.timeline_second_trimester_info, secondTrimesterStart.format(formatter)),
            ) {
                selectedTrimester = 2
                showSheet = true
            }
            Spacer(modifier = Modifier.height(16.dp))
            TimelineCard(
                title = stringResource(R.string.milestones_third_trimester),
                dateInfo = stringResource(R.string.timeline_third_trimester_info, thirdTrimesterStart.format(formatter)),
            ) {
                selectedTrimester = 3
                showSheet = true
            }
            Spacer(modifier = Modifier.height(32.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.timeline_due_date_card),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = uiState.dueDate?.format(formatter) ?: "",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        } else {
            Text(stringResource(R.string.timeline_no_date))
        }
    }

    if (((showSheet && (selectedTrimester != null)) && (uiState.lmpDate != null))) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                selectedTrimester = null
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
            ) {
                val title = when (selectedTrimester) {
                    1 -> stringResource(R.string.timeline_sheet_first)
                    2 -> stringResource(R.string.timeline_sheet_second)
                    else -> stringResource(R.string.timeline_sheet_third)
                }
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp),
                )

                val weeksRange = when (selectedTrimester) {
                    1 -> 1..13
                    2 -> 14..27
                    else -> 28..42
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(weeksRange.toList()) { weekNumber ->
                        val startDaysToAdd = (weekNumber - 1) * 7L
                        val endDaysToAdd = startDaysToAdd + 6L
                        val weekStartDate = uiState.lmpDate.plusDays(startDaysToAdd)
                        val weekEndDate = uiState.lmpDate.plusDays(endDaysToAdd)
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(R.string.timeline_week_label, weekNumber),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Text(
                                text = "${weekStartDate.format(weekFormatter)} – ${weekEndDate.format(weekFormatter)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (weekNumber != weeksRange.last) {
                            HorizontalDivider(
                                modifier = Modifier.padding(top = 12.dp),
                                thickness = 0.5.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}
