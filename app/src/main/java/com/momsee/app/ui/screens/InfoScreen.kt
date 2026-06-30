package com.momsee.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class PregnancyTerm(
    val term: String,
    val fullName: String,
    val definition: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(onBack: () -> Unit) {
    val terms = listOf(
        PregnancyTerm(
            "LMP",
            "Last Menstrual Period",
            "The first day of your last period, used as the starting point to calculate your estimated due date and gestational age."
        ),
        PregnancyTerm(
            "GA",
            "Gestational Age",
            "How far along the pregnancy is, measured in weeks and days from the first day of the LMP."
        ),
        PregnancyTerm(
            "EDD",
            "Estimated Due Date",
            "The date that spontaneous onset of labor is expected to occur, typically 280 days (40 weeks) after the LMP."
        ),
        PregnancyTerm(
            "Amenorrhea",
            "Secondary Amenorrhea",
            "The absence of menstrual periods. In pregnancy, this is the physiological absence of menstruation."
        ),
        PregnancyTerm(
            "Trimester",
            "Pregnancy Stages",
            "One of three periods (approximately 13–14 weeks each) into which a human pregnancy is divided."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pregnancy Terms", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(terms) { item ->
                TermCard(item)
            }
        }
    }
}

@Composable
fun TermCard(term: PregnancyTerm) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = term.term,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = term.fullName,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = term.definition,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
