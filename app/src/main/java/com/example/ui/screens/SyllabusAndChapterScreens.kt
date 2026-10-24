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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChapterEntity
import com.example.data.model.ChapterStatus
import com.example.data.model.SubjectEntity
import com.example.data.model.TopicEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.PrimaryButton
import com.example.ui.components.StudyHubCard
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealDark

@Composable
fun SyllabusScreen(
    currentLevel: String,
    subjects: List<SubjectEntity>,
    chapters: List<ChapterEntity>,
    onOpenChapter: (chapterId: String, subjectId: String, subjectName: String) -> Unit,
    onAddCustomSubject: (name: String, level: String, colorHex: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedLevel by remember { mutableStateOf(currentLevel) }
    val filteredSubjects = subjects.filter { it.level.equals(selectedLevel, ignoreCase = true) }

    var selectedSubjectId by remember(filteredSubjects) {
        mutableStateOf(filteredSubjects.firstOrNull()?.id ?: "")
    }

    // Fallback if current selected subject is not in list
    val activeSubject = filteredSubjects.firstOrNull { it.id == selectedSubjectId } ?: filteredSubjects.firstOrNull()
    val subjectChapters = chapters.filter { it.subjectId == activeSubject?.id }
    val completedChapters = subjectChapters.count { it.status == ChapterStatus.COMPLETED }
    val totalChapters = subjectChapters.size
    val progressPercent = if (totalChapters > 0) (completedChapters * 100) / totalChapters else 0

    var showAddSubjectDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title Header
            item {
                Text(
                    text = "Syllabus Tracker",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            // Academic Level Filter Pills (SSC, HSC, Admission)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("SSC", "HSC", "Admission").forEach { level ->
                        val isSelected = selectedLevel.equals(level, ignoreCase = true)
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    selectedLevel = level
                                    val newSubs = subjects.filter { it.level.equals(level, ignoreCase = true) }
                                    selectedSubjectId = newSubs.firstOrNull()?.id ?: ""
                                }
                                .testTag("syllabus_level_tab_$level"),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(20.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            Text(
                                text = level,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            // Subject Chips Row
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredSubjects) { sub ->
                        val isSelected = sub.id == activeSubject?.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSubjectId = sub.id },
                            label = { Text(sub.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.testTag("syllabus_subject_${sub.name}")
                        )
                    }

                    item {
                        FilterChip(
                            selected = false,
                            onClick = { showAddSubjectDialog = true },
                            label = { Text("+ Custom") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = false,
                                borderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }

            // Subject Progress Overview Card
            if (activeSubject != null) {
                item {
                    StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = activeSubject.name,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "$completedChapters/$totalChapters completed",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { if (totalChapters > 0) completedChapters.toFloat() / totalChapters else 0f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }

                // Chapter List Header
                item {
                    Text(
                        text = "Chapters",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                if (subjectChapters.isEmpty()) {
                    item {
                        EmptyStateView(
                            icon = Icons.Default.Check,
                            title = "No chapters yet",
                            subtitle = "Chapters for ${activeSubject.name} will appear here."
                        )
                    }
                } else {
                    // Chapters List
                    items(subjectChapters, key = { it.id }) { ch ->
                        ChapterCardItem(
                            chapter = ch,
                            onClick = {
                                onOpenChapter(ch.id, ch.subjectId, activeSubject.name)
                            }
                        )
                    }
                }
            } else {
                item {
                    EmptyStateView(
                        icon = Icons.Default.Add,
                        title = "No subjects found",
                        subtitle = "Select another academic level or create a custom subject.",
                        ctaText = "+ Add Subject",
                        onCtaClick = { showAddSubjectDialog = true }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Add Custom Subject Dialog
    if (showAddSubjectDialog) {
        var newSubName by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddSubjectDialog = false },
            title = { Text("Add Custom Subject") },
            text = {
                OutlinedTextField(
                    value = newSubName,
                    onValueChange = { newSubName = it },
                    label = { Text("Subject Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSubName.isNotBlank()) {
                            onAddCustomSubject(newSubName, selectedLevel, "#087F8C")
                            showAddSubjectDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubjectDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ChapterCardItem(
    chapter: ChapterEntity,
    onClick: () -> Unit
) {
    StudyHubCard(
        onClick = onClick,
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.testTag("chapter_card_${chapter.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Icon
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when (chapter.status) {
                            ChapterStatus.COMPLETED -> StudySuccess
                            ChapterStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            ChapterStatus.NOT_STARTED -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                when (chapter.status) {
                    ChapterStatus.COMPLETED -> Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    ChapterStatus.IN_PROGRESS -> Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    ChapterStatus.NOT_STARTED -> Icon(
                        imageVector = Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Not Started",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (chapter.status) {
                        ChapterStatus.COMPLETED -> "Completed"
                        ChapterStatus.IN_PROGRESS -> "In Progress"
                        ChapterStatus.NOT_STARTED -> "Not Started"
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = when (chapter.status) {
                            ChapterStatus.COMPLETED -> StudySuccess
                            ChapterStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
                            ChapterStatus.NOT_STARTED -> MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun ChapterDetailScreen(
    chapter: ChapterEntity?,
    subjectName: String,
    topics: List<TopicEntity>,
    onToggleTopic: (topicId: String, isCompleted: Boolean) -> Unit,
    onToggleWeak: (topicId: String, isWeak: Boolean) -> Unit,
    onMarkChapterComplete: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Topics, 1: Resources
    val totalTopics = topics.size
    val completedCount = topics.count { it.isCompleted }
    val pendingCount = totalTopics - completedCount
    val progressPercent = if (totalTopics > 0) (completedCount * 100) / totalTopics else 0

    var showCompleteConfirmDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                modifier = Modifier.testTag("chapter_detail_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Chapter Details",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Teal Chapter Header Banner (matching mockup)
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = StudyTealDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF0D3B43),
                                        Color(0xFF065A64),
                                        Color(0xFF087F8C)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(
                                text = subjectName.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    letterSpacing = 1.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = chapter?.title ?: "Chapter Details",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Stats Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                    StatItem(number = "$totalTopics", label = "Total")
                                    StatItem(number = "$completedCount", label = "Completed")
                                    StatItem(number = "$pendingCount", label = "Pending")
                                }

                                Text(
                                    text = "$progressPercent%",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Tabs: Topics, Resources
            item {
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
                        text = {
                            Text(
                                "Topics",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Resources",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    )
                }
            }

            if (selectedTab == 0) {
                // Topic items checklist
                items(topics, key = { it.id }) { topic ->
                    StudyHubCard(contentPadding = PaddingValues(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = topic.isCompleted,
                                onCheckedChange = { isChecked ->
                                    onToggleTopic(topic.id, isChecked)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.testTag("topic_checkbox_${topic.id}")
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = topic.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (topic.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (topic.isCompleted) FontWeight.Normal else FontWeight.Medium
                                ),
                                modifier = Modifier.weight(1f)
                            )

                            // Weak / Revisit flag icon
                            IconButton(
                                onClick = { onToggleWeak(topic.id, !topic.isWeak) }
                            ) {
                                Icon(
                                    imageVector = if (topic.isWeak) Icons.Filled.Flag else Icons.Outlined.Flag,
                                    contentDescription = "Flag weak area",
                                    tint = if (topic.isWeak) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                // Mark Chapter Complete Action
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Mark Chapter Complete",
                        onClick = { showCompleteConfirmDialog = true },
                        testTag = "mark_chapter_complete_button"
                    )
                }
            } else {
                // Resources Tab
                item {
                    StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                        Column {
                            Text(
                                text = "Recommended Study Resources",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• NCTB Official Textbook\n• Previous 5 Years Board Questions (Dhaka, Rajshahi, Chittagong)\n• Formula Revision Sheet (Available in Tools > Notes)",
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
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showCompleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showCompleteConfirmDialog = false },
            title = { Text("Complete Chapter?") },
            text = { Text("This will mark all topics in this chapter as completed.") },
            confirmButton = {
                Button(
                    onClick = {
                        onMarkChapterComplete()
                        showCompleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatItem(number: String, label: String) {
    Column {
        Text(
            text = number,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.White.copy(alpha = 0.75f),
                fontSize = 11.sp
            )
        )
    }
}
