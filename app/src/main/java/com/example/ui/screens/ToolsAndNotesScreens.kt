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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlashcardEntity
import com.example.data.model.FlashcardStatus
import com.example.data.model.NoteEntity
import com.example.data.model.ReminderEntity
import com.example.data.model.ReminderType
import com.example.ui.components.EmptyStateView
import com.example.ui.components.StudyHubCard
import com.example.ui.components.SubjectTag
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction

@Composable
fun ToolsHubScreen(
    onOpenFocusTimer: () -> Unit,
    onOpenSessionLog: () -> Unit,
    onOpenNotes: () -> Unit,
    onOpenFlashcards: () -> Unit,
    onOpenReminders: () -> Unit,
    onOpenAnalytics: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Study Tools",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Boost productivity and retain knowledge effectively",
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        item {
            ToolCard(
                title = "Focus Timer",
                subtitle = "Pomodoro 25/5 and custom timed study sessions",
                icon = Icons.Default.Timer,
                iconTint = MaterialTheme.colorScheme.primary,
                onClick = onOpenFocusTimer
            )
        }

        item {
            ToolCard(
                title = "Study Session Log",
                subtitle = "Review past sessions, time spent and subject breakdown",
                icon = Icons.Default.History,
                iconTint = Color(0xFF2563EB),
                onClick = onOpenSessionLog
            )
        }

        item {
            ToolCard(
                title = "Notes & Study Material",
                subtitle = "Subject-wise organized revision notes and formulas",
                icon = Icons.Default.Notes,
                iconTint = Color(0xFF10B981),
                onClick = onOpenNotes
            )
        }

        item {
            ToolCard(
                title = "Flashcard Decks",
                subtitle = "Interactive spaced-repetition card decks for formulas & definitions",
                icon = Icons.Default.Style,
                iconTint = Color(0xFFE05638),
                onClick = onOpenFlashcards
            )
        }

        item {
            ToolCard(
                title = "Reminders & Alerts",
                subtitle = "Daily study task alarms, revision reminders and exam alerts",
                icon = Icons.Default.Notifications,
                iconTint = Color(0xFF8B5CF6),
                onClick = onOpenReminders
            )
        }

        item {
            ToolCard(
                title = "Performance Analytics",
                subtitle = "Detailed subject-wise study metrics and consistency score",
                icon = Icons.Default.Analytics,
                iconTint = Color(0xFFD97706),
                onClick = onOpenAnalytics
            )
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@Composable
private fun ToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    StudyHubCard(onClick = onClick, contentPadding = PaddingValues(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = iconTint.copy(alpha = 0.15f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }
    }
}

@Composable
fun NotesAndFlashcardsScreen(
    initialTab: Int = 0,
    notes: List<NoteEntity>,
    flashcards: List<FlashcardEntity>,
    onAddNote: () -> Unit,
    onDeleteNote: (String) -> Unit,
    onUpdateFlashcard: (cardId: String, status: FlashcardStatus) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(initialTab) } // 0: Notes, 1: Flashcards
    var searchQuery by remember { mutableStateOf("") }
    var selectedSubjectFilter by remember { mutableStateOf("All") }

    val filteredNotes = notes.filter {
        (selectedSubjectFilter == "All" || it.subjectName.equals(selectedSubjectFilter, ignoreCase = true)) &&
                (searchQuery.isBlank() || it.title.contains(searchQuery, ignoreCase = true) || it.content.contains(searchQuery, ignoreCase = true))
    }

    // Flashcard deck states
    var selectedFlashcardSubject by remember { mutableStateOf("Physics") }
    val deckFlashcards = flashcards.filter { it.subjectName.equals(selectedFlashcardSubject, ignoreCase = true) }
    var currentCardIndex by remember(deckFlashcards) { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("notes_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Notes & Flashcards",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            // Tabs: Notes vs Flashcards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier
                                .clickable { selectedTab = 0 }
                                .testTag("tab_notes")
                        ) {
                            Text(
                                text = "Notes",
                                color = if (selectedTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent,
                            modifier = Modifier
                                .clickable { selectedTab = 1 }
                                .testTag("tab_flashcards")
                        ) {
                            Text(
                                text = "Flashcards",
                                color = if (selectedTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (selectedTab == 0) {
                // Notes View
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Search Bar
                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search notes...") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("notes_search_input"),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            ),
                            singleLine = true
                        )
                    }

                    // Subject Folder Chips
                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf("All", "Physics", "Chemistry", "Mathematics", "Biology", "English")) { sub ->
                                val isSelected = selectedSubjectFilter == sub
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedSubjectFilter = sub },
                                    label = { Text(sub) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = isSelected,
                                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                            }
                        }
                    }

                    if (filteredNotes.isEmpty()) {
                        item {
                            EmptyStateView(
                                icon = Icons.Default.Notes,
                                title = "No notes found",
                                subtitle = "Create your first revision note for formulas and key topics.",
                                ctaText = "+ Create Note",
                                onCtaClick = onAddNote
                            )
                        }
                    } else {
                        items(filteredNotes, key = { it.id }) { note ->
                            StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        SubjectTag(name = note.subjectName)
                                        IconButton(
                                            onClick = { onDeleteNote(note.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Delete Note",
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    if (note.chapterName.isNotBlank()) {
                                        Text(
                                            text = note.chapterName,
                                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = note.content,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            lineHeight = 22.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            } else {
                // Flashcards View
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Subject selector
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(listOf("Physics", "Chemistry", "Mathematics", "Biology")) { sub ->
                            val isSelected = selectedFlashcardSubject == sub
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedFlashcardSubject = sub
                                    currentCardIndex = 0
                                    isFlipped = false
                                },
                                label = { Text(sub) },
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
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (deckFlashcards.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.Style,
                            title = "No flashcards in this deck",
                            subtitle = "Cards will appear here for $selectedFlashcardSubject."
                        )
                    } else {
                        val currentCard = deckFlashcards.getOrNull(currentCardIndex) ?: deckFlashcards.first()

                        // Card counter
                        Text(
                            text = "${currentCardIndex + 1} / ${deckFlashcards.size}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interactive Flip Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(260.dp)
                                .clickable { isFlipped = !isFlipped }
                                .testTag("flashcard_container"),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isFlipped) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = if (isFlipped) "ANSWER" else "QUESTION",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isFlipped) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = if (isFlipped) currentCard.answerBack else currentCard.questionFront,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 24.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Text(
                                        text = if (isFlipped) "Tap to flip back" else "Tap to reveal answer",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Controls: Review again vs Know it
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onUpdateFlashcard(currentCard.id, FlashcardStatus.REVIEW_AGAIN)
                                    if (currentCardIndex < deckFlashcards.size - 1) {
                                        currentCardIndex++
                                        isFlipped = false
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("flashcard_review_again"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Review again", color = Color(0xFFD97706))
                            }

                            Button(
                                onClick = {
                                    onUpdateFlashcard(currentCard.id, FlashcardStatus.KNOW_IT)
                                    if (currentCardIndex < deckFlashcards.size - 1) {
                                        currentCardIndex++
                                        isFlipped = false
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("flashcard_know_it"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = StudySuccess)
                            ) {
                                Text("Know it")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Previous / Next Navigation
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    if (currentCardIndex > 0) {
                                        currentCardIndex--
                                        isFlipped = false
                                    }
                                },
                                enabled = currentCardIndex > 0
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous",
                                    tint = if (currentCardIndex > 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            }

                            IconButton(
                                onClick = {
                                    if (currentCardIndex < deckFlashcards.size - 1) {
                                        currentCardIndex++
                                        isFlipped = false
                                    }
                                },
                                enabled = currentCardIndex < deckFlashcards.size - 1
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next",
                                    tint = if (currentCardIndex < deckFlashcards.size - 1) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // FAB to add note (visible in Notes tab)
        if (selectedTab == 0) {
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
                    .testTag("create_note_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Create Note")
            }
        }
    }
}

@Composable
fun CreateNoteScreen(
    onSaveNote: (title: String, content: String, subjectName: String, chapterName: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Physics") }
    var chapterName by remember { mutableStateOf("Chapter 3 - Motion in a Plane") }
    var titleError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
    ) {
        // App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("create_note_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Create Note",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onSaveNote(title, content, selectedSubject, chapterName)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("save_note_button")
            ) {
                Text("Save")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Subject selector row
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listOf("Physics", "Chemistry", "Mathematics", "Biology", "English", "ICT")) { sub ->
                val isSelected = selectedSubject == sub
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedSubject = sub },
                    label = { Text(sub) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                if (it.isNotBlank()) titleError = false
            },
            placeholder = { Text("Note Title (e.g. Motion in a Plane - Formulas)") },
            isError = titleError,
            supportingText = if (titleError) {
                { Text("Please enter a note title", color = MaterialTheme.colorScheme.error) }
            } else null,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("note_title_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Chapter Name input
        OutlinedTextField(
            value = chapterName,
            onValueChange = { chapterName = it },
            placeholder = { Text("Associated Chapter (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Content Area
        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            placeholder = { Text("Write your study note, key equations or summary here...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("note_content_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

@Composable
fun RemindersScreen(
    reminders: List<ReminderEntity>,
    onToggleReminder: (id: String, enabled: Boolean) -> Unit,
    onAddReminder: (title: String, subtitle: String, time: String, type: ReminderType) -> Unit,
    onDeleteReminder: (id: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTypeFilter by remember { mutableStateOf(ReminderType.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredReminders = reminders.filter {
        selectedTypeFilter == ReminderType.ALL || it.reminderType == selectedTypeFilter
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // App Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag("reminders_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reminders",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            // Filter Tabs
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ReminderType.entries) { type ->
                    val isSelected = selectedTypeFilter == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedTypeFilter = type },
                        label = { Text(type.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Reminder Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                if (filteredReminders.isEmpty()) {
                    item {
                        EmptyStateView(
                            icon = Icons.Default.Notifications,
                            title = "No reminders",
                            subtitle = "Stay ahead of deadlines by scheduling daily study alarms.",
                            ctaText = "+ Add Reminder",
                            onCtaClick = { showAddDialog = true }
                        )
                    }
                } else {
                    items(filteredReminders, key = { it.id }) { reminder ->
                        StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = when (reminder.reminderType) {
                                        ReminderType.STUDY -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                        ReminderType.REVISION -> Color(0xFFFEF3C7)
                                        ReminderType.EXAM -> Color(0xFFFDE8E8)
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            tint = when (reminder.reminderType) {
                                                ReminderType.STUDY -> MaterialTheme.colorScheme.primary
                                                ReminderType.REVISION -> Color(0xFFD97706)
                                                ReminderType.EXAM -> Color(0xFFD14343)
                                                else -> MaterialTheme.colorScheme.onSurface
                                            },
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reminder.title,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = reminder.subtitle,
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = reminder.scheduledTimeFormatted,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }

                                Switch(
                                    checked = reminder.isEnabled,
                                    onCheckedChange = { onToggleReminder(reminder.id, it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                                        uncheckedThumbColor = Color.White,
                                        uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Add Reminder FAB
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("add_reminder_fab")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Reminder")
        }
    }

    if (showAddDialog) {
        var rTitle by remember { mutableStateOf("") }
        var rSub by remember { mutableStateOf("") }
        var rType by remember { mutableStateOf(ReminderType.STUDY) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Schedule Reminder") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = rTitle,
                        onValueChange = { rTitle = it },
                        label = { Text("Reminder Title (e.g. Study Task)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = rSub,
                        onValueChange = { rSub = it },
                        label = { Text("Details (e.g. Physics - Chapter 4)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rTitle.isNotBlank()) {
                            onAddReminder(rTitle, rSub.ifBlank { "Study Session" }, "Tomorrow, 09:00 AM", rType)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Schedule")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
