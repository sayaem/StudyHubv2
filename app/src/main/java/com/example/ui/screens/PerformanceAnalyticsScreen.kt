package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.StudySessionEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.ui.components.CircularProgressGauge
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StudyHubCard
import com.example.ui.theme.StudySuccess

@Composable
fun PerformanceAnalyticsScreen(
    tasks: List<StudyTaskEntity>,
    sessions: List<StudySessionEntity>,
    subjects: List<SubjectEntity>,
    chapters: List<ChapterEntity>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Overview, 1: Subjects, 2: Tests

    // Real analytics calculated exclusively from user data
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val taskCompletionRate = if (totalTasks > 0) (completedTasks * 100) / totalTasks else 0

    val totalStudySeconds = sessions.sumOf { it.focusSeconds }
    val studyHours = totalStudySeconds / 3600
    val studyMinutes = (totalStudySeconds % 3600) / 60

    val totalChapters = chapters.size
    val completedChapters = chapters.count { it.status == ChapterStatus.COMPLETED }
    val syllabusRate = if (totalChapters > 0) (completedChapters * 100) / totalChapters else 0

    val overallScore = if (totalTasks == 0 && totalChapters == 0) 0 else ((taskCompletionRate * 0.5f) + (syllabusRate * 0.5f)).toInt().coerceIn(0, 100)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("analytics_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Performance Analytics",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // Tabs: Overview, Subjects, Tests
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Overview", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Subjects", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Tests", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium) }
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedTab == 0) {
                // Large Overall Performance Gauge
                item {
                    StudyHubCard(contentPadding = PaddingValues(24.dp)) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Overall Consistency Score",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CircularProgressGauge(
                                progress = overallScore / 100f,
                                modifier = Modifier.size(160.dp),
                                strokeWidth = 14.dp,
                                progressColor = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "$overallScore%",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (totalTasks == 0 && sessions.isEmpty()) "Complete study tasks and log focus sessions to increase your score." else "Computed from your real task completion and chapter progress.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Two Stats Cards: Study Hours & Tasks Completed
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                            Column {
                                Text(
                                    text = "Total Focus Time",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${studyHours}h ${studyMinutes}m",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${sessions.size} logged sessions",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }

                        StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                            Column {
                                Text(
                                    text = "Tasks Completed",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$completedTasks / $totalTasks",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "$taskCompletionRate% completion",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = StudySuccess,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }

                // Subject-wise Performance Section
                item {
                    StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "Subject-wise Chapter Mastery",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            if (subjects.isEmpty()) {
                                Text(
                                    text = "No subjects configured yet.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            } else {
                                subjects.take(6).forEach { sub ->
                                    val subCh = chapters.filter { it.subjectId == sub.id }
                                    val done = subCh.count { it.status == ChapterStatus.COMPLETED }
                                    val total = subCh.size
                                    val pct = if (total > 0) (done * 100) / total else 0
                                    SubjectScoreBar(
                                        subject = sub.name,
                                        percent = pct,
                                        barColor = try { Color(android.graphics.Color.parseColor(sub.colorHex)) } catch (_: Exception) { MaterialTheme.colorScheme.primary }
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (selectedTab == 1) {
                // Subject detailed metrics
                item {
                    Text(
                        text = "Curriculum Topic Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                items(subjects, key = { it.id }) { sub ->
                    val subCh = chapters.filter { it.subjectId == sub.id }
                    val done = subCh.count { it.status == ChapterStatus.COMPLETED }
                    val total = subCh.size
                    val pct = if (total > 0) (done * 100) / total else 0

                    StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = sub.name,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "$done / $total chapters ($pct%)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { pct / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            } else {
                // Model Tests Tab
                item {
                    StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Model Tests & Exam Results",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Track your college pre-test and coaching mock exam scores to monitor your preparation for the final board or admission test.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun SubjectScoreBar(subject: String, percent: Int, barColor: Color) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subject,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percent / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}
