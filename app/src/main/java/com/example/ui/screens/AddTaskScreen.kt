package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.ChapterEntity
import com.example.data.model.StudyTaskEntity
import com.example.data.model.SubjectEntity
import com.example.data.model.TaskPriority
import com.example.ui.components.PrimaryButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    initialDate: Long,
    subjects: List<SubjectEntity>,
    chapters: List<ChapterEntity>,
    existingTask: StudyTaskEntity? = null,
    onBack: () -> Unit,
    onSave: (
        title: String,
        subjectName: String,
        chapterName: String,
        dueDate: Long,
        dueTimeString: String,
        estimatedMinutes: Int,
        priority: TaskPriority,
        notes: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var selectedSubjectName by remember {
        mutableStateOf(existingTask?.subjectName ?: (subjects.firstOrNull()?.name ?: "Physics"))
    }
    var subjectExpanded by remember { mutableStateOf(false) }

    val currentSubjectId = subjects.firstOrNull { it.name == selectedSubjectName }?.id
    val relevantChapters = chapters.filter { it.subjectId == currentSubjectId }
    var selectedChapterName by remember {
        mutableStateOf(existingTask?.chapterName ?: (relevantChapters.firstOrNull()?.title ?: ""))
    }
    var chapterExpanded by remember { mutableStateOf(false) }

    var selectedPriority by remember { mutableStateOf(existingTask?.priority ?: TaskPriority.MEDIUM) }
    var priorityExpanded by remember { mutableStateOf(false) }

    var estimatedMinutes by remember { mutableIntStateOf(existingTask?.estimatedMinutes ?: 60) }
    var durationExpanded by remember { mutableStateOf(false) }

    var notes by remember { mutableStateOf(existingTask?.notes ?: "") }
    var titleError by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("add_task_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (existingTask != null) "Edit Study Task" else "New Study Task",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subject selector
        ExposedDropdownMenuBox(
            expanded = subjectExpanded,
            onExpandedChange = { subjectExpanded = !subjectExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedSubjectName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Subject") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = subjectExpanded) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .testTag("add_task_subject_dropdown"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            ExposedDropdownMenu(
                expanded = subjectExpanded,
                onDismissRequest = { subjectExpanded = false }
            ) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject.name) },
                        onClick = {
                            selectedSubjectName = subject.name
                            val newChapters = chapters.filter { it.subjectId == subject.id }
                            selectedChapterName = newChapters.firstOrNull()?.title ?: ""
                            subjectExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chapter selector
        if (relevantChapters.isNotEmpty()) {
            ExposedDropdownMenuBox(
                expanded = chapterExpanded,
                onExpandedChange = { chapterExpanded = !chapterExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedChapterName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chapter") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = chapterExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .testTag("add_task_chapter_dropdown"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                ExposedDropdownMenu(
                    expanded = chapterExpanded,
                    onDismissRequest = { chapterExpanded = false }
                ) {
                    relevantChapters.forEach { ch ->
                        DropdownMenuItem(
                            text = { Text(ch.title) },
                            onClick = {
                                selectedChapterName = ch.title
                                chapterExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Task Title
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (it.isNotBlank()) titleError = false
            },
            label = { Text("Task Title *") },
            placeholder = { Text("e.g. Read Vector Laws, Solve MCQ #1-20") },
            isError = titleError,
            supportingText = if (titleError) {
                { Text("Please enter a title for the task", color = MaterialTheme.colorScheme.error) }
            } else null,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Title, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_task_title_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Date Display
        OutlinedTextField(
            value = dateFormatter.format(Date(initialDate)),
            onValueChange = {},
            readOnly = true,
            label = { Text("Scheduled Date") },
            leadingIcon = {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Priority Dropdown
        ExposedDropdownMenuBox(
            expanded = priorityExpanded,
            onExpandedChange = { priorityExpanded = !priorityExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedPriority.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Priority") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Flag, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .testTag("add_task_priority_dropdown"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            ExposedDropdownMenu(
                expanded = priorityExpanded,
                onDismissRequest = { priorityExpanded = false }
            ) {
                TaskPriority.entries.forEach { p ->
                    DropdownMenuItem(
                        text = { Text(p.label) },
                        onClick = {
                            selectedPriority = p
                            priorityExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Estimated Duration Dropdown
        ExposedDropdownMenuBox(
            expanded = durationExpanded,
            onExpandedChange = { durationExpanded = !durationExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = "${estimatedMinutes / 60}h ${estimatedMinutes % 60}m",
                onValueChange = {},
                readOnly = true,
                label = { Text("Estimated Duration") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = durationExpanded) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .testTag("add_task_duration_dropdown"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )
            ExposedDropdownMenu(
                expanded = durationExpanded,
                onDismissRequest = { durationExpanded = false }
            ) {
                listOf(25, 45, 60, 90, 120, 180).forEach { mins ->
                    DropdownMenuItem(
                        text = { Text("${mins / 60}h ${mins % 60}m") },
                        onClick = {
                            estimatedMinutes = mins
                            durationExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Notes
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes & Key Equations (optional)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .testTag("add_task_notes_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (existingTask != null) "Update Task" else "Save Task",
            onClick = {
                if (title.isBlank()) {
                    titleError = true
                } else {
                    onSave(
                        title,
                        selectedSubjectName,
                        selectedChapterName,
                        initialDate,
                        "10:00 AM",
                        estimatedMinutes,
                        selectedPriority,
                        notes
                    )
                }
            },
            testTag = "save_task_button"
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
