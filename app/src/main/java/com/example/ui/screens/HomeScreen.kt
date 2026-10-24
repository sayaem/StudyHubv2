package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.TopicEntity
import com.example.data.model.UserProfile
import com.example.ui.components.CircularProgressGauge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.NavTab
import com.example.ui.components.StudentAvatarBadge
import com.example.ui.components.StudyHubCard
import com.example.ui.components.TaskCardItem
import com.example.ui.theme.StudySuccess
import java.util.Calendar

@Composable
fun HomeScreen(
    profile: UserProfile?,
    tasks: List<StudyTaskEntity>,
    sessions: List<StudySessionEntity>,
    topics: List<TopicEntity>,
    onToggleTaskComplete: (taskId: String, isCompleted: Boolean) -> Unit,
    onNavigateTab: (NavTab) -> Unit,
    onAddTask: () -> Unit,
    onStartFocus: () -> Unit,
    onOpenTask: (StudyTaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    // Real calculation of days left to exam
    val now = System.currentTimeMillis()
    val examDate = profile?.targetExamDate ?: (now + 240L * 86400000L)
    val daysLeft = ((examDate - now) / 86400000L).coerceAtLeast(0L).toInt()

    // Real study time today from saved session records
    val todayStart = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val todaySessions = sessions.filter { it.startedAt >= todayStart }
    val todayStudySeconds = todaySessions.sumOf { it.focusSeconds }
    val todayHours = (todayStudySeconds / 3600).toInt()
    val todayMinutes = ((todayStudySeconds % 3600) / 60).toInt()
    val dailyGoalMinutes = profile?.dailyGoalMinutes ?: 180
    val dailyGoalHours = (dailyGoalMinutes / 60).coerceAtLeast(1)

    // Real syllabus progress from completed topics
    val totalTopics = topics.size
    val completedTopics = topics.count { it.isCompleted }
    val syllabusPercent = if (totalTopics > 0) (completedTopics * 100) / totalTopics else 0

    // Filter today's tasks
    val todayEnd = todayStart + 86400000L
    val todayTasks = tasks.filter { it.dueDate in todayStart until todayEnd }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Greeting Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hello, ${profile?.displayName?.ifBlank { "Student" } ?: "Student"} 👋",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${profile?.level ?: "HSC"} Candidate • ${profile?.mainGoal ?: "Exam Preparation"}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }

                // Student Avatar
                StudentAvatarBadge(
                    avatarId = profile?.avatarId ?: 1,
                    name = profile?.displayName ?: "SH",
                    size = 46.dp
                )
            }
        }

        // Exam Countdown Card
        item {
            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                text = "Exam Target",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = profile?.targetExamName ?: "HSC 2026",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$daysLeft",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 32.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Days Left",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    // Progress ring
                    CircularProgressGauge(
                        progress = (1f - (daysLeft / 365f)).coerceIn(0.1f, 1f),
                        modifier = Modifier.size(68.dp),
                        strokeWidth = 7.dp,
                        progressColor = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        // Two Mini Stats Cards: Syllabus Progress & Today's Study Time
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Syllabus Progress Card
                StudyHubCard(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(14.dp),
                    onClick = { onNavigateTab(NavTab.TRACK) }
                ) {
                    Column {
                        Text(
                            text = "Syllabus Progress",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$syllabusPercent%",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { (syllabusPercent / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }

                // Today's Study Time Card
                StudyHubCard(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(14.dp),
                    onClick = { onStartFocus() }
                ) {
                    Column {
                        Text(
                            text = "Today's Focus Time",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${todayHours}h ${todayMinutes}m",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "/ ${dailyGoalHours}h",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { if (dailyGoalMinutes > 0) (todayStudySeconds / (dailyGoalMinutes * 60f)).coerceIn(0f, 1f) else 0f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = StudySuccess,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }
        }

        // Quick Actions Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onAddTask,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("home_add_task_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Add Task", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onStartFocus,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("home_start_focus_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Start Focus", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Section: Today's Plan Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Plan",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                TextButton(
                    onClick = { onNavigateTab(NavTab.PLAN) },
                    modifier = Modifier.testTag("home_view_all_tasks_button")
                ) {
                    Text(
                        text = "View Planner",
                        style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                }
            }
        }

        // Task Items or Genuine Empty State
        if (todayTasks.isEmpty()) {
            item {
                EmptyStateView(
                    icon = Icons.Outlined.EventNote,
                    title = "Your study plan starts here",
                    subtitle = "Plan your topics for today to build daily momentum.",
                    ctaText = "+ Add First Task",
                    onCtaClick = onAddTask
                )
            }
        } else {
            items(todayTasks, key = { it.id }) { task ->
                TaskCardItem(
                    task = task,
                    onToggleComplete = { onToggleTaskComplete(task.id, !task.isCompleted) },
                    onClick = { onOpenTask(task) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
