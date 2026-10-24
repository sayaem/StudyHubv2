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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyTaskEntity
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StudyHubCard
import com.example.ui.components.TaskCardItem
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PlanScreen(
    selectedDate: Long,
    tasks: List<StudyTaskEntity>,
    onSelectDate: (Long) -> Unit,
    onChangeDay: (Int) -> Unit,
    onToggleTaskComplete: (taskId: String, isCompleted: Boolean) -> Unit,
    onAddTask: () -> Unit,
    onEditTask: (StudyTaskEntity) -> Unit,
    onDeleteTask: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var taskFilter by remember { mutableStateOf("All") } // All, Pending, Completed
    var selectedTaskForDetails by remember { mutableStateOf<StudyTaskEntity?>(null) }
    var taskToDelete by remember { mutableStateOf<StudyTaskEntity?>(null) }

    // Calendar & Date formatting
    val dateFormatter = remember { SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()) }
    val dayNumberFormatter = remember { SimpleDateFormat("dd", Locale.getDefault()) }
    val dayNameFormatter = remember { SimpleDateFormat("EEE", Locale.getDefault()) }

    // Generate week days around selected date
    val weekDays = remember(selectedDate) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = selectedDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val days = mutableListOf<Long>()
        cal.add(Calendar.DAY_OF_YEAR, -3)
        for (i in 0..6) {
            days.add(cal.timeInMillis)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        days
    }

    // Filter tasks for the selected day
    val dayStart = selectedDate
    val dayEnd = selectedDate + 86400000L
    val filteredTasks = tasks.filter {
        it.dueDate in dayStart until dayEnd && when (taskFilter) {
            "Pending" -> !it.isCompleted
            "Completed" -> it.isCompleted
            else -> true
        }
    }

    Box(modifier = modifier.fillMaxSize().background(StudyBackground)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header / Date navigator (matching mockup!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Today's Schedule",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                        Text(
                            text = dateFormatter.format(Date(selectedDate)),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                    }

                    Row {
                        IconButton(
                            onClick = { onChangeDay(-1) },
                            modifier = Modifier.testTag("plan_prev_day_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Previous Day",
                                tint = StudyNavyPrimary
                            )
                        }
                        IconButton(
                            onClick = { onChangeDay(1) },
                            modifier = Modifier.testTag("plan_next_day_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Next Day",
                                tint = StudyNavyPrimary
                            )
                        }
                    }
                }
            }

            // Week Strip Horizontal Scroll (matching mockup!)
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(weekDays) { dayMillis ->
                        val isSelected = dayMillis == selectedDate
                        val isToday = dayMillis == com.example.ui.viewmodel.StudyViewModel.getStartOfDay(System.currentTimeMillis())

                        Column(
                            modifier = Modifier
                                .width(52.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isSelected) StudyTealAction else Color.White
                                )
                                .clickable { onSelectDate(dayMillis) }
                                .padding(vertical = 12.dp)
                                .testTag("date_chip_${dayNumberFormatter.format(Date(dayMillis))}"),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = dayNameFormatter.format(Date(dayMillis)),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else StudySecondaryText,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = dayNumberFormatter.format(Date(dayMillis)),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else StudyNavyPrimary,
                                    fontSize = 16.sp
                                )
                            )
                            if (isToday) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .background(
                                            if (isSelected) Color.White else StudyTealAction,
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }

            // Filter Chips: All, Pending, Completed
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("All", "Pending", "Completed").forEach { filter ->
                        val selected = taskFilter == filter
                        FilterChip(
                            selected = selected,
                            onClick = { taskFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudyTealLight,
                                selectedLabelColor = StudyTealAction,
                                containerColor = Color.White,
                                labelColor = StudySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = if (selected) StudyTealAction else StudyBorder
                            )
                        )
                    }
                }
            }

            // Tasks Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tasks (${filteredTasks.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudyNavyPrimary
                        )
                    )

                    Button(
                        onClick = onAddTask,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudyTealAction,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("plan_add_task_inline_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Add Task", fontSize = 12.sp)
                    }
                }
            }

            // Task List or Empty State
            if (filteredTasks.isEmpty()) {
                item {
                    EmptyStateView(
                        icon = Icons.Outlined.EventNote,
                        title = "No tasks found",
                        subtitle = "No tasks match your selected date and filter.",
                        ctaText = "+ Add Study Task",
                        onCtaClick = onAddTask
                    )
                }
            } else {
                items(filteredTasks, key = { it.id }) { task ->
                    TaskCardItem(
                        task = task,
                        onToggleComplete = { onToggleTaskComplete(task.id, !task.isCompleted) },
                        onClick = { selectedTaskForDetails = task }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Task Detail & Actions Dialog
    selectedTaskForDetails?.let { task ->
        AlertDialog(
            onDismissRequest = { selectedTaskForDetails = null },
            title = {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudyNavyPrimary
                    )
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Subject: ${task.subjectName}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                    if (task.chapterName.isNotBlank()) {
                        Text(
                            text = "Chapter: ${task.chapterName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Text(
                        text = "Estimated Time: ${task.estimatedMinutes / 60}h ${task.estimatedMinutes % 60}m",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Priority: ${task.priority.label}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    if (task.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Notes: ${task.notes}",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                    }
                }
            },
            confirmButton = {
                Row {
                    TextButton(
                        onClick = {
                            taskToDelete = task
                            selectedTaskForDetails = null
                        }
                    ) {
                        Text("Delete", color = Color(0xFFD14343))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val target = task
                            selectedTaskForDetails = null
                            onEditTask(target)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                    ) {
                        Text("Edit")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTaskForDetails = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Delete confirmation dialog
    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskToDelete = null },
            title = { Text("Delete Task?") },
            text = { Text("Are you sure you want to delete '${task.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTask(task.id)
                        taskToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD14343))
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { taskToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
