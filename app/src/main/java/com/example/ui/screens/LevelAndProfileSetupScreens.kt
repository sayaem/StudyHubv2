package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AcademicLevel
import com.example.data.model.EducationBoard
import com.example.data.model.StudyStream
import com.example.ui.components.PrimaryButton
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun LevelSelectionScreen(
    selectedLevel: String,
    onSelectLevel: (String) -> Unit,
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        // Top Back Button
        IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("level_select_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = StudyNavyPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select Your Level",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = StudyNavyPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Choose your current academic level to get a personalized experience.",
            style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Level Cards matching mockup
        LevelCard(
            levelCode = "SSC",
            classLabel = "Class 9-10",
            icon = Icons.Default.MenuBook,
            iconTint = Color(0xFF10B981),
            isSelected = selectedLevel == "SSC",
            onClick = { onSelectLevel("SSC") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LevelCard(
            levelCode = "HSC",
            classLabel = "Class 11-12",
            icon = Icons.Default.School,
            iconTint = Color(0xFF2563EB),
            isSelected = selectedLevel == "HSC",
            onClick = { onSelectLevel("HSC") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LevelCard(
            levelCode = "Admission",
            classLabel = "University Preparation",
            icon = Icons.Default.AccountBalance,
            iconTint = Color(0xFF8B5CF6),
            isSelected = selectedLevel == "Admission",
            onClick = { onSelectLevel("Admission") }
        )

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = "Continue",
            onClick = onContinue,
            testTag = "level_select_continue_button"
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun LevelCard(
    levelCode: String,
    classLabel: String,
    icon: ImageVector,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("level_card_$levelCode"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF2FBFA) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) StudyTealAction else StudyBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(50.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = levelCode,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudyNavyPrimary
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = classLabel,
                    style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                )
            }

            // Radio / Check circle
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) StudyTealAction else StudyBorder,
                        shape = CircleShape
                    )
                    .background(if (isSelected) StudyTealAction else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    initialLevel: String,
    onBack: () -> Unit,
    onComplete: (name: String, level: String, board: String, group: String, examName: String, examDate: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("Sayem Arafat") }
    var selectedLevel by remember { mutableStateOf(initialLevel) }
    var selectedBoard by remember { mutableStateOf("Dhaka") }
    var boardExpanded by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf("Science") }
    var groupExpanded by remember { mutableStateOf(false) }

    // Target Exam Date (defaults to approx 8 months from now)
    val examCalendar = remember {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 238)
        }
    }
    var targetExamDateMillis by remember { mutableStateOf(examCalendar.timeInMillis) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("profile_setup_back_button")
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = StudyNavyPrimary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Center Avatar
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                color = StudyAccentSky
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = StudyTealAction,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Complete Your Profile",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = StudyNavyPrimary
            ),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Help us personalize your study plan.",
            style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText),
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Name input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = StudyTealAction
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_name_input"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StudyTealAction,
                unfocusedBorderColor = StudyBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Education Board Dropdown
        ExposedDropdownMenuBox(
            expanded = boardExpanded,
            onExpandedChange = { boardExpanded = !boardExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedBoard,
                onValueChange = {},
                readOnly = true,
                label = { Text("Board") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = boardExpanded) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = StudyTealAction
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .testTag("profile_board_dropdown"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudyTealAction,
                    unfocusedBorderColor = StudyBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = boardExpanded,
                onDismissRequest = { boardExpanded = false }
            ) {
                EducationBoard.entries.forEach { board ->
                    DropdownMenuItem(
                        text = { Text(board.displayName) },
                        onClick = {
                            selectedBoard = board.displayName
                            boardExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Group / Stream Dropdown
        ExposedDropdownMenuBox(
            expanded = groupExpanded,
            onExpandedChange = { groupExpanded = !groupExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedGroup,
                onValueChange = {},
                readOnly = true,
                label = { Text("Group") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = groupExpanded) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = StudyTealAction
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .testTag("profile_group_dropdown"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudyTealAction,
                    unfocusedBorderColor = StudyBorder,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            ExposedDropdownMenu(
                expanded = groupExpanded,
                onDismissRequest = { groupExpanded = false }
            ) {
                listOf("Science", "Commerce", "Humanities").forEach { group ->
                    DropdownMenuItem(
                        text = { Text(group) },
                        onClick = {
                            selectedGroup = group
                            groupExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target Exam Date field
        OutlinedTextField(
            value = dateFormatter.format(Date(targetExamDateMillis)),
            onValueChange = {},
            readOnly = true,
            label = { Text("Target Exam Date") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    tint = StudyTealAction
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_exam_date_field"),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = StudyTealAction,
                unfocusedBorderColor = StudyBorder,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continue",
            onClick = {
                onComplete(
                    name,
                    selectedLevel,
                    selectedBoard,
                    selectedGroup,
                    "$selectedLevel 2026",
                    targetExamDateMillis
                )
            },
            testTag = "profile_setup_continue_button"
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}
