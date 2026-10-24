package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SessionMode
import com.example.data.model.StudySessionEntity
import com.example.ui.components.CircularProgressGauge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.PrimaryButton
import com.example.ui.components.StudyHubCard
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight
import com.example.ui.viewmodel.TimerState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FocusTimerScreen(
    timerState: TimerState,
    remainingSeconds: Long,
    totalSeconds: Long,
    mode: SessionMode,
    durationMinutes: Int,
    breakMinutes: Int,
    selectedSubject: String,
    availableSubjects: List<String>,
    showSaveDialog: Boolean,
    onSetMode: (SessionMode) -> Unit,
    onSetDuration: (Int) -> Unit,
    onSetBreak: (Int) -> Unit,
    onSetSubject: (String) -> Unit,
    onStartTimer: () -> Unit,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onEndTimer: () -> Unit,
    onSaveSession: (notes: String) -> Unit,
    onDiscardSession: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sessionNotes by remember { mutableStateOf("") }
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timeFormatted = String.format(Locale.US, "%02d:%02d", minutes, seconds)
    val progress = if (totalSeconds > 0) (remainingSeconds.toFloat() / totalSeconds.toFloat()) else 1f

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("focus_timer_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = StudyNavyPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Focus Timer",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudyNavyPrimary
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mode Toggles (Pomodoro 25/5 vs Custom)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, StudyBorder),
                modifier = Modifier.padding(4.dp)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (mode == SessionMode.POMODORO) StudyTealAction else Color.Transparent,
                        modifier = Modifier
                            .clickable { onSetMode(SessionMode.POMODORO) }
                            .testTag("timer_mode_pomodoro")
                    ) {
                        Text(
                            text = "Pomodoro 25/5",
                            color = if (mode == SessionMode.POMODORO) Color.White else StudySecondaryText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (mode == SessionMode.CUSTOM) StudyTealAction else Color.Transparent,
                        modifier = Modifier
                            .clickable { onSetMode(SessionMode.CUSTOM) }
                            .testTag("timer_mode_custom")
                    ) {
                        Text(
                            text = "Custom",
                            color = if (mode == SessionMode.CUSTOM) Color.White else StudySecondaryText,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subject selector chip row
        Text(
            text = "Studying: $selectedSubject",
            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(availableSubjects) { sub ->
                val isSelected = sub == selectedSubject
                FilterChip(
                    selected = isSelected,
                    onClick = { onSetSubject(sub) },
                    label = { Text(sub) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudyTealLight,
                        selectedLabelColor = StudyTealAction,
                        containerColor = Color.White,
                        labelColor = StudySecondaryText
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) StudyTealAction else StudyBorder
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Large Circular Countdown Display (matching mockup!)
        CircularProgressGauge(
            progress = progress,
            modifier = Modifier.size(240.dp),
            strokeWidth = 14.dp,
            progressColor = StudyTealAction,
            trackColor = StudyAccentSky
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 44.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = StudyNavyPrimary,
                        letterSpacing = (-1).sp
                    ),
                    modifier = Modifier.testTag("timer_countdown_display")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (timerState) {
                        TimerState.RUNNING -> "Focus Time"
                        TimerState.PAUSED -> "Paused"
                        TimerState.COMPLETED -> "Finished!"
                        TimerState.IDLE -> "Ready"
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = StudySecondaryText,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Custom Mode Sliders
        if (mode == SessionMode.CUSTOM && timerState == TimerState.IDLE) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text(
                    text = "Focus Duration: $durationMinutes min",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Slider(
                    value = durationMinutes.toFloat(),
                    onValueChange = { onSetDuration(it.toInt()) },
                    valueRange = 5f..120f,
                    steps = 22,
                    colors = SliderDefaults.colors(
                        thumbColor = StudyTealAction,
                        activeTrackColor = StudyTealAction
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Action Buttons: Start / Pause / Resume / Stop / Reset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (timerState) {
                TimerState.IDLE -> {
                    Button(
                        onClick = onStartTimer,
                        modifier = Modifier
                            .height(56.dp)
                            .width(180.dp)
                            .testTag("timer_start_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Focus", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                TimerState.RUNNING -> {
                    IconButton(
                        onClick = onEndTimer,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White, CircleShape)
                            .testTag("timer_stop_button")
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "End", tint = Color(0xFFD14343))
                    }
                    Spacer(modifier = Modifier.width(24.dp))
                    Button(
                        onClick = onPauseTimer,
                        modifier = Modifier
                            .height(56.dp)
                            .width(150.dp)
                            .testTag("timer_pause_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyNavyPrimary)
                    ) {
                        Icon(imageVector = Icons.Default.Pause, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pause", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
                TimerState.PAUSED -> {
                    IconButton(
                        onClick = onResetTimer,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White, CircleShape)
                            .testTag("timer_reset_button")
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", tint = StudySecondaryText)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = onResumeTimer,
                        modifier = Modifier
                            .height(56.dp)
                            .width(140.dp)
                            .testTag("timer_resume_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Resume", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    IconButton(
                        onClick = onEndTimer,
                        modifier = Modifier
                            .size(52.dp)
                            .background(Color.White, CircleShape)
                            .testTag("timer_finish_button")
                    ) {
                        Icon(imageVector = Icons.Default.Stop, contentDescription = "Finish", tint = Color(0xFFD14343))
                    }
                }
                TimerState.COMPLETED -> {
                    Button(
                        onClick = onResetTimer,
                        modifier = Modifier
                            .height(56.dp)
                            .width(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                    ) {
                        Text("Start Another", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Save Session Summary Dialog (PRD 6.11)
    if (showSaveDialog) {
        val studiedSecs = (totalSeconds - remainingSeconds).coerceAtLeast(60L)
        val studiedMins = studiedSecs / 60
        AlertDialog(
            onDismissRequest = { /* force explicit user choice */ },
            title = {
                Text(
                    text = "Session Completed! 🎉",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudyNavyPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Great job! You focused on $selectedSubject for $studiedMins minutes.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    OutlinedTextField(
                        value = sessionNotes,
                        onValueChange = { sessionNotes = it },
                        label = { Text("What did you accomplish?") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveSession(sessionNotes)
                        sessionNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction),
                    modifier = Modifier.testTag("timer_save_session_button")
                ) {
                    Text("Save to Log")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDiscardSession()
                        sessionNotes = ""
                    }
                ) {
                    Text("Discard", color = StudySecondaryText)
                }
            }
        )
    }
}

@Composable
fun SessionLogScreen(
    sessions: List<StudySessionEntity>,
    onDeleteSession: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Today") } // Today, Week, Month
    var sessionToDelete by remember { mutableStateOf<StudySessionEntity?>(null) }

    val totalSeconds = sessions.sumOf { it.focusSeconds }
    val totalHours = totalSeconds / 3600
    val totalMinutes = (totalSeconds % 3600) / 60

    // Group by subject for distribution bars
    val subjectTotals = sessions.groupBy { it.subjectName }
        .mapValues { entry -> entry.value.sumOf { it.focusSeconds } }

    val dateFormatter = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // App Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("session_log_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = StudyNavyPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Study Session Log",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudyNavyPrimary
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Filter tabs: Today, Week, Month
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudyBorder)
                    ) {
                        Row(modifier = Modifier.padding(4.dp)) {
                            listOf("Today", "Week", "Month").forEach { tab ->
                                val isSelected = selectedFilter == tab
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) StudyTealAction else Color.Transparent,
                                    modifier = Modifier.clickable { selectedFilter = tab }
                                ) {
                                    Text(
                                        text = tab,
                                        color = if (isSelected) Color.White else StudySecondaryText,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Summary Metrics Card matching mockup
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                        Column {
                            Text(
                                text = "Total Study Time",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${totalHours}h ${totalMinutes}m",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudyNavyPrimary
                                )
                            )
                        }
                    }

                    StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                        Column {
                            Text(
                                text = "Focus Sessions",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${sessions.size}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudyNavyPrimary
                                )
                            )
                        }
                    }
                }
            }

            // Subject Distribution Section
            item {
                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Subject Distribution",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        subjectTotals.forEach { (sub, secs) ->
                            val hours = secs / 3600
                            val mins = (secs % 3600) / 60
                            val fraction = if (totalSeconds > 0) secs.toFloat() / totalSeconds.toFloat() else 0f
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = sub, style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        text = "${hours}h ${mins}m",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = StudySecondaryText,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { fraction },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = when (sub) {
                                        "Physics" -> Color(0xFF087F8C)
                                        "Chemistry" -> Color(0xFFE05638)
                                        "Mathematics" -> Color(0xFF2563EB)
                                        "Biology" -> Color(0xFF10B981)
                                        else -> StudyTealAction
                                    },
                                    trackColor = StudyAccentSky
                                )
                            }
                        }
                    }
                }
            }

            // Session History Section
            item {
                Text(
                    text = "Session History",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudyNavyPrimary
                    )
                )
            }

            if (sessions.isEmpty()) {
                item {
                    Text(
                        text = "No focus sessions recorded yet.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText)
                    )
                }
            } else {
                items(sessions, key = { it.id }) { s ->
                    val mins = s.focusSeconds / 60
                    StudyHubCard(contentPadding = PaddingValues(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = s.subjectName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StudyNavyPrimary
                                    )
                                )
                                if (!s.notes.isNullOrBlank()) {
                                    Text(
                                        text = s.notes,
                                        style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                                    )
                                }
                                Text(
                                    text = dateFormatter.format(Date(s.startedAt)),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudySecondaryText,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Text(
                                text = "${mins}m",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudyTealAction
                                )
                            )

                            IconButton(
                                onClick = { sessionToDelete = s }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Session",
                                    tint = StudySecondaryText.copy(alpha = 0.6f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    sessionToDelete?.let { session ->
        AlertDialog(
            onDismissRequest = { sessionToDelete = null },
            title = { Text("Delete Session?") },
            text = { Text("Are you sure you want to remove this ${session.subjectName} session?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSession(session.id)
                        sessionToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD14343))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { sessionToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
