package com.momsee.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momsee.app.R
import com.momsee.app.ui.PregnancyUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Milestone(
    val titleRes: Int,
    val descriptionRes: Int,
    val startWeek: Int,
    val endWeek: Int? = null,
    val startDayOffset: Int = 0,
    val endDayOffset: Int = 0,
)

@Composable
fun MilestonesScreen(uiState: PregnancyUiState) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    
    var startAnimation by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(800),
        label = "Alpha"
    )
    
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val milestones = listOf(
        Milestone(R.string.milestone_conceived_title, R.string.milestone_conceived_desc, 2, 3),
        Milestone(R.string.milestone_heartbeat_title, R.string.milestone_heartbeat_desc, 6, 7),
        Milestone(R.string.milestone_movements_title, R.string.milestone_movements_desc, 16, 25),
        Milestone(R.string.milestone_gender_title, R.string.milestone_gender_desc, 18, 22),
        Milestone(R.string.milestone_midway_title, R.string.milestone_midway_desc, 20),
        Milestone(R.string.milestone_survival_title, R.string.milestone_survival_desc, 22, 24),
        Milestone(R.string.milestone_breathe_title, R.string.milestone_breathe_desc, 26, 37),
        Milestone(R.string.milestone_full_term_title, R.string.milestone_full_term_desc, 39, 40, 0, 6)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .graphicsLayer(alpha = alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.nav_milestones),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 24.dp),
            color = MaterialTheme.colorScheme.primary
        )

        if (uiState.lmpDate != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                item { 
                    TrimesterHeader(
                        stringResource(R.string.milestones_first_trimester), 
                        stringResource(R.string.milestones_first_trimester_weeks),
                    ) 
                }
                items(milestones.filter { it.startWeek <= 13 }) { milestone ->
                    MilestoneCard(milestone, uiState.lmpDate, formatter)
                }

                item { 
                    Spacer(modifier = Modifier.height(8.dp))
                    TrimesterHeader(
                        stringResource(R.string.milestones_second_trimester), 
                        stringResource(R.string.milestones_second_trimester_weeks),
                    ) 
                }
                items(milestones.filter { it.startWeek in 14..27 }) { milestone ->
                    MilestoneCard(milestone, uiState.lmpDate, formatter)
                }

                item { 
                    Spacer(modifier = Modifier.height(8.dp))
                    TrimesterHeader(
                        stringResource(R.string.milestones_third_trimester), 
                        stringResource(R.string.milestones_third_trimester_weeks),
                    ) 
                }
                items(milestones.filter { it.startWeek >= 28 }) { milestone ->
                    MilestoneCard(milestone, uiState.lmpDate, formatter)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.milestones_no_date))
            }
        }
    }
}

@Composable
fun TrimesterHeader(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
    }
}

@Composable
fun MilestoneCard(milestone: Milestone, lmpDate: LocalDate, formatter: DateTimeFormatter) {
    val startDate = lmpDate.plusWeeks(milestone.startWeek.toLong() - 1).plusDays(milestone.startDayOffset.toLong())
    val endDate = milestone.endWeek?.let { 
        lmpDate.plusWeeks(it.toLong() - 1).plusDays(milestone.endDayOffset.toLong()) 
    }

    val dateRangeText = if (endDate != null && endDate != startDate) {
        "${startDate.format(formatter)} - ${endDate.format(formatter)}"
    } else {
        startDate.format(formatter)
    }

    val weeksText = if (milestone.endWeek != null && milestone.endWeek != milestone.startWeek) {
        stringResource(R.string.timeline_weeks_range, milestone.startWeek, milestone.endWeek)
    } else {
        stringResource(R.string.timeline_week_label, milestone.startWeek)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(milestone.titleRes),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = weeksText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateRangeText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(milestone.descriptionRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}
