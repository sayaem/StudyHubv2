package com.example.ui.screens

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EducationBoard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.StudentAvatarBadge
import com.example.ui.components.StudyHubCard
import com.example.ui.components.StudyHubLogoMark
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction
import com.example.util.StudyHubNotificationManager
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onCompleteOnboarding: (
        name: String,
        level: String,
        admissionCategory: String,
        board: String,
        group: String,
        examName: String,
        examDate: Long,
        dailyGoalMinutes: Int,
        focusMinutes: Int,
        breakMinutes: Int,
        mainGoal: String,
        studyReminders: Boolean,
        goalReminders: Boolean,
        examReminders: Boolean,
        avatarId: Int,
        selectedSubjects: List<String>
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentStep by remember { mutableIntStateOf(1) } // 1 through 7

    // State collected progressively
    var studentName by remember { mutableStateOf("") }
    var selectedAvatarId by remember { mutableIntStateOf(1) }
    var nameError by remember { mutableStateOf(false) }

    var selectedPathway by remember { mutableStateOf("HSC") } // SSC, HSC, Admission
    var selectedAdmissionCategory by remember { mutableStateOf("Engineering") } // Medical, Engineering, General, Other
    var targetInstitution by remember { mutableStateOf("BUET & Top Universities") }

    var selectedBoard by remember { mutableStateOf("Dhaka") }
    var boardExpanded by remember { mutableStateOf(false) }
    var selectedStream by remember { mutableStateOf("Science") }
    var streamExpanded by remember { mutableStateOf(false) }

    var targetExamName by remember { mutableStateOf("HSC 2026") }
    val examDateMillis = remember {
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 240) }.timeInMillis
    }

    var dailyGoalHours by remember { mutableIntStateOf(3) }
    var focusDuration by remember { mutableIntStateOf(25) }
    var breakDuration by remember { mutableIntStateOf(5) }
    var selectedMainGoal by remember { mutableStateOf("Exam Preparation") }

    // Subject selection list
    val selectedSubjects = remember {
        mutableStateListOf("Physics", "Chemistry", "Higher Mathematics", "Biology", "ICT", "English")
    }

    // Reminder toggles & runtime permission
    var studyReminders by remember { mutableStateOf(true) }
    var goalReminders by remember { mutableStateOf(true) }
    var examReminders by remember { mutableStateOf(true) }
    var hasGrantedNotificationPermission by remember {
        mutableStateOf(StudyHubNotificationManager.hasNotificationPermission(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasGrantedNotificationPermission = isGranted
        if (isGranted) {
            StudyHubNotificationManager.sendTestNotification(context, studentName.ifBlank { "Student" })
        }
    }

    // Adapt subject list when pathway changes
    fun updateSubjectsForPathway(pathway: String, admissionCat: String, stream: String) {
        selectedSubjects.clear()
        when (pathway) {
            "SSC" -> selectedSubjects.addAll(listOf("Physics", "Chemistry", "General Math", "Biology", "Bangladesh & Global Studies"))
            "HSC" -> {
                when (stream) {
                    "Commerce" -> selectedSubjects.addAll(listOf("Accounting", "Finance & Banking", "Business Organization", "English", "ICT"))
                    "Humanities" -> selectedSubjects.addAll(listOf("Civics & Governance", "Economics", "Sociology", "English", "Bangla"))
                    else -> selectedSubjects.addAll(listOf("Physics", "Chemistry", "Higher Mathematics", "Biology", "ICT", "English"))
                }
            }
            "Admission" -> {
                when (admissionCat) {
                    "Medical" -> selectedSubjects.addAll(listOf("Biology (Adv)", "Chemistry (Adv)", "Physics (Adv)", "General Knowledge & English"))
                    "General" -> selectedSubjects.addAll(listOf("General Knowledge & English", "Bangla (Adv)", "General Math (Adv)"))
                    else -> selectedSubjects.addAll(listOf("Higher Math (Adv)", "Physics (Adv)", "Chemistry (Adv)", "English"))
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // Top Navigation & Step Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 1) {
                IconButton(
                    onClick = { currentStep-- },
                    modifier = Modifier.testTag("onboarding_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }

            // Step Progress Indicator
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "STEP $currentStep OF 7",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { currentStep / 7f },
                    modifier = Modifier
                        .width(100.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // Skip button (safely advances to review)
            if (currentStep < 7) {
                TextButton(
                    onClick = {
                        if (studentName.isBlank()) studentName = "Student"
                        currentStep = 7
                    }
                ) {
                    Text("Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Spacer(modifier = Modifier.size(40.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step Content Body
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (currentStep) {
                // STEP 1: Welcome & Profile Basics
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                StudyHubLogoMark(size = 64.dp)
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Welcome to StudyHub",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Your personalized study manager for SSC, HSC, and University Admissions.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                    Text(
                                        text = "What is your name?",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    OutlinedTextField(
                                        value = studentName,
                                        onValueChange = {
                                            studentName = it
                                            if (it.isNotBlank()) nameError = false
                                        },
                                        placeholder = { Text("Enter your name or nickname") },
                                        isError = nameError,
                                        supportingText = if (nameError) {
                                            { Text("Please enter a name to personalize your plan", color = MaterialTheme.colorScheme.error) }
                                        } else null,
                                        singleLine = true,
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("onboarding_name_input"),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(
                                        text = "Pick an avatar",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        (1..4).forEach { id ->
                                            val isSelected = selectedAvatarId == id
                                            Box(
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(CircleShape)
                                                    .border(
                                                        width = if (isSelected) 3.dp else 1.dp,
                                                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                                        shape = CircleShape
                                                    )
                                                    .clickable { selectedAvatarId = id }
                                                    .padding(4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                StudentAvatarBadge(
                                                    avatarId = id,
                                                    name = studentName.ifBlank { "SH" },
                                                    size = 50.dp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // STEP 2: Academic Pathway
                2 -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Select Your Academic Pathway",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "We will tailor your subjects, syllabus chapters, and countdowns accordingly.",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        PathwayCard(
                            title = "SSC",
                            classLabel = "Class 9-10 Secondary Certificate",
                            icon = Icons.Default.MenuBook,
                            isSelected = selectedPathway == "SSC",
                            onClick = {
                                selectedPathway = "SSC"
                                targetExamName = "SSC 2026"
                                updateSubjectsForPathway("SSC", selectedAdmissionCategory, selectedStream)
                            }
                        )

                        PathwayCard(
                            title = "HSC",
                            classLabel = "Class 11-12 Higher Secondary Certificate",
                            icon = Icons.Default.School,
                            isSelected = selectedPathway == "HSC",
                            onClick = {
                                selectedPathway = "HSC"
                                targetExamName = "HSC 2026"
                                updateSubjectsForPathway("HSC", selectedAdmissionCategory, selectedStream)
                            }
                        )

                        PathwayCard(
                            title = "University Admission",
                            classLabel = "Engineering, Medical & Varsity Admission Prep",
                            icon = Icons.Default.AccountBalance,
                            isSelected = selectedPathway == "Admission",
                            onClick = {
                                selectedPathway = "Admission"
                                targetExamName = "Admission Test 2026"
                                updateSubjectsForPathway("Admission", selectedAdmissionCategory, selectedStream)
                            }
                        )
                    }
                }

                // STEP 3: Academic Details
                3 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Academic Details",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Customize the board, group, or admission target.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        if (selectedPathway == "Admission") {
                            item {
                                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(
                                            text = "Target Admission Category",
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                        )
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            listOf("Medical", "Engineering", "General").forEach { cat ->
                                                val isSelected = selectedAdmissionCategory == cat
                                                FilterChip(
                                                    selected = isSelected,
                                                    onClick = {
                                                        selectedAdmissionCategory = cat
                                                        updateSubjectsForPathway(selectedPathway, cat, selectedStream)
                                                    },
                                                    label = { Text(cat) },
                                                    colors = FilterChipDefaults.filterChipColors(
                                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                                        selectedLabelColor = Color.White
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                OutlinedTextField(
                                    value = targetInstitution,
                                    onValueChange = { targetInstitution = it },
                                    label = { Text("Target Institutions / Universities") },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        } else {
                            // Education Board
                            item {
                                ExposedDropdownMenuBox(
                                    expanded = boardExpanded,
                                    onExpandedChange = { boardExpanded = !boardExpanded },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = selectedBoard,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Education Board") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = boardExpanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = boardExpanded,
                                        onDismissRequest = { boardExpanded = false }
                                    ) {
                                        EducationBoard.entries.forEach { b ->
                                            DropdownMenuItem(
                                                text = { Text(b.displayName) },
                                                onClick = {
                                                    selectedBoard = b.displayName
                                                    boardExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Stream / Group
                            item {
                                ExposedDropdownMenuBox(
                                    expanded = streamExpanded,
                                    onExpandedChange = { streamExpanded = !streamExpanded },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = selectedStream,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Study Stream / Group") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = streamExpanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    ExposedDropdownMenu(
                                        expanded = streamExpanded,
                                        onDismissRequest = { streamExpanded = false }
                                    ) {
                                        listOf("Science", "Commerce", "Humanities").forEach { st ->
                                            DropdownMenuItem(
                                                text = { Text(st) },
                                                onClick = {
                                                    selectedStream = st
                                                    updateSubjectsForPathway(selectedPathway, selectedAdmissionCategory, st)
                                                    streamExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            OutlinedTextField(
                                value = targetExamName,
                                onValueChange = { targetExamName = it },
                                label = { Text("Target Exam Name") },
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // STEP 4: Study Routine & Goals
                4 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Study Routine & Goals",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Set achievable targets to build consistency without burnout.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Daily Study Target", fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = "$dailyGoalHours hours / day",
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Slider(
                                        value = dailyGoalHours.toFloat(),
                                        onValueChange = { dailyGoalHours = it.toInt() },
                                        valueRange = 1f..10f,
                                        steps = 8,
                                        colors = SliderDefaults.colors(thumbColor = MaterialTheme.colorScheme.primary)
                                    )
                                }
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Text(text = "Focus Session Duration", fontWeight = FontWeight.SemiBold)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        listOf(25, 45, 60).forEach { mins ->
                                            val isSelected = focusDuration == mins
                                            FilterChip(
                                                selected = isSelected,
                                                onClick = { focusDuration = mins },
                                                label = { Text("$mins min") },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(text = "Primary Goal", fontWeight = FontWeight.SemiBold)
                                    listOf("Exam Preparation", "Syllabus Completion", "Revision & Mock Practice", "Daily Consistency").forEach { g ->
                                        val isSelected = selectedMainGoal == g
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable { selectedMainGoal = g }
                                                .padding(vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .border(
                                                        2.dp,
                                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                                        CircleShape
                                                    )
                                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(text = g, style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // STEP 5: Subject Selection
                5 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Text(
                                text = "Select Your Subjects",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Check the subjects you are studying this academic year.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        val availableForLevel = listOf(
                            "Physics", "Chemistry", "Higher Mathematics", "Biology",
                            "ICT", "English", "General Math", "Accounting", "Finance & Banking",
                            "Civics & Governance", "Economics", "General Knowledge & English"
                        )

                        items(availableForLevel) { sub ->
                            val isChecked = selectedSubjects.contains(sub)
                            StudyHubCard(
                                onClick = {
                                    if (isChecked) selectedSubjects.remove(sub) else selectedSubjects.add(sub)
                                },
                                contentPadding = PaddingValues(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { check ->
                                            if (check) selectedSubjects.add(sub) else selectedSubjects.remove(sub)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // STEP 6: Reminder Preferences & Real Notification Permission
                6 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Text(
                                text = "Study Reminders & Alerts",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gentle, non-intrusive alerts help you keep your daily study commitments.",
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }

                        // Notification Permission Banner
                        item {
                            StudyHubCard(
                                backgroundColor = if (hasGrantedNotificationPermission) MaterialTheme.colorScheme.primary.copy(alpha = 0.10f) else MaterialTheme.colorScheme.surfaceVariant,
                                contentPadding = PaddingValues(16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = if (hasGrantedNotificationPermission) Icons.Default.CheckCircle else Icons.Default.NotificationsActive,
                                            contentDescription = null,
                                            tint = if (hasGrantedNotificationPermission) StudySuccess else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (hasGrantedNotificationPermission) "Notifications Enabled" else "Allow Android Notifications",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    Text(
                                        text = "We only send essential reminders: task start times and your daily goal milestone.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    )

                                    if (!hasGrantedNotificationPermission) {
                                        Button(
                                            onClick = {
                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                                } else {
                                                    hasGrantedNotificationPermission = true
                                                    StudyHubNotificationManager.sendTestNotification(context, studentName.ifBlank { "Student" })
                                                }
                                            },
                                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("Grant Notification Permission")
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                    ReminderRow("Study Task Reminders", "Alert when a planned task is scheduled", studyReminders) { studyReminders = it }
                                    ReminderRow("Daily Goal Check-in", "Evening progress summary", goalReminders) { goalReminders = it }
                                    ReminderRow("Exam Countdown Alerts", "Weekly days-remaining alert", examReminders) { examReminders = it }
                                }
                            }
                        }
                    }
                }

                // STEP 7: Review & Final Confirmation
                7 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                StudentAvatarBadge(
                                    avatarId = selectedAvatarId,
                                    name = studentName.ifBlank { "Student" },
                                    size = 72.dp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Ready to Study, ${studentName.ifBlank { "Student" }}!",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Review your personalized study setup below.",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }

                        item {
                            StudyHubCard(contentPadding = PaddingValues(18.dp)) {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(text = "Summary", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                    SummaryRow("Pathway", selectedPathway)
                                    if (selectedPathway == "Admission") {
                                        SummaryRow("Category", selectedAdmissionCategory)
                                        SummaryRow("Target", targetInstitution)
                                    } else {
                                        SummaryRow("Board", selectedBoard)
                                        SummaryRow("Stream", selectedStream)
                                    }
                                    SummaryRow("Target Exam", targetExamName)
                                    SummaryRow("Daily Goal", "$dailyGoalHours hours / day")
                                    SummaryRow("Focus Session", "$focusDuration min")
                                    SummaryRow("Selected Subjects", "${selectedSubjects.size} subjects")
                                    SummaryRow("Notifications", if (hasGrantedNotificationPermission) "Active" else "Optional")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom CTA Button
        PrimaryButton(
            text = if (currentStep == 7) "Start My Study Journey 🚀" else "Continue",
            onClick = {
                if (currentStep == 1 && studentName.isBlank()) {
                    nameError = true
                } else if (currentStep < 7) {
                    currentStep++
                } else {
                    onCompleteOnboarding(
                        studentName.ifBlank { "Student" },
                        selectedPathway,
                        selectedAdmissionCategory,
                        selectedBoard,
                        selectedStream,
                        targetExamName,
                        examDateMillis,
                        dailyGoalHours * 60,
                        focusDuration,
                        breakDuration,
                        selectedMainGoal,
                        studyReminders,
                        goalReminders,
                        examReminders,
                        selectedAvatarId,
                        selectedSubjects.toList()
                    )
                }
            },
            testTag = "onboarding_continue_button"
        )
    }
}

@Composable
private fun PathwayCard(
    title: String,
    classLabel: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("pathway_card_$title"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(26.dp)
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
                Text(
                    text = classLabel,
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        CircleShape
                    )
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ReminderRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
    }
}
