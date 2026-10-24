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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.StudyHubCard
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight

@Composable
fun ProfileScreen(
    profile: UserProfile?,
    onEditProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAnalytics: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGoalsDialog by remember { mutableStateOf(false) }
    var showAchievementsDialog by remember { mutableStateOf(false) }
    var showHelpDialog by remember { mutableStateOf(false) }

    val totalHours = (profile?.totalStudyMinutes ?: 7245) / 60
    val totalMinutes = (profile?.totalStudyMinutes ?: 7245) % 60

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = StudyNavyPrimary
                )
            )
        }

        // Profile Info Card (matching mockup!)
        item {
            StudyHubCard(contentPadding = PaddingValues(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = Color(0xFF4338CA)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = profile?.displayName?.split(" ")?.mapNotNull { it.firstOrNull()?.toString() }?.take(2)?.joinToString("") ?: "SA",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile?.displayName ?: "Sayem Arafat",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${profile?.level ?: "Class 12"} • ${profile?.groupName ?: "Science"} • ${profile?.board ?: "Dhaka"}",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                    }

                    OutlinedButton(
                        onClick = onEditProfile,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("profile_edit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = StudyTealAction,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit", color = StudyTealAction, fontSize = 12.sp)
                    }
                }
            }
        }

        // Stats: Streak & Total Study Time (matching mockup!)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Streak Card
                StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFEF3C7),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Study Streak",
                                style = MaterialTheme.typography.labelSmall.copy(color = StudySecondaryText)
                            )
                            Text(
                                text = "${profile?.streakDays ?: 7} days",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudyNavyPrimary
                                )
                            )
                        }
                    }
                }

                // Total Study Time Card
                StudyHubCard(modifier = Modifier.weight(1f), contentPadding = PaddingValues(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = StudyAccentSky,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = null,
                                    tint = StudyTealAction,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Total Study Time",
                                style = MaterialTheme.typography.labelSmall.copy(color = StudySecondaryText)
                            )
                            Text(
                                text = "${totalHours}h ${totalMinutes}m",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StudyNavyPrimary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Profile Menu Links matching mockup
        item {
            StudyHubCard(contentPadding = PaddingValues(0.dp)) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Default.TrackChanges,
                        label = "My Goals",
                        onClick = { showGoalsDialog = true }
                    )
                    HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
                    ProfileMenuItem(
                        icon = Icons.Default.EmojiEvents,
                        label = "Achievements",
                        onClick = { showAchievementsDialog = true }
                    )
                    HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
                    ProfileMenuItem(
                        icon = Icons.Default.HelpOutline,
                        label = "Help & Support",
                        onClick = { showHelpDialog = true }
                    )
                    HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        label = "Settings",
                        onClick = onOpenSettings
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }

    if (showGoalsDialog) {
        AlertDialog(
            onDismissRequest = { showGoalsDialog = false },
            title = { Text("My Academic Goals") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Target Exam: ${profile?.targetExamName ?: "HSC 2026"}")
                    Text("• Daily Study Target: ${(profile?.dailyGoalMinutes ?: 300) / 60} hours")
                    Text("• Target Board: ${profile?.board ?: "Dhaka"}")
                    Text("• Stream: ${profile?.groupName ?: "Science"}")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showGoalsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showAchievementsDialog) {
        AlertDialog(
            onDismissRequest = { showAchievementsDialog = false },
            title = { Text("Achievements 🏆") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🔥 7-Day Consistency Streak (Unlocked)")
                    Text("⏱️ 100+ Hours Study Club (Unlocked)")
                    Text("📚 Syllabus Pioneer - 3 Chapters Complete (Unlocked)")
                    Text("⚡ Quick Revise Master (In Progress)")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAchievementsDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                ) {
                    Text("Awesome")
                }
            }
        )
    }

    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("StudyHub Support") },
            text = {
                Text(
                    "StudyHub is designed specifically for Bangladeshi students preparing for SSC, HSC, and University Admissions.\n\n" +
                            "For feedback or syllabus updates, reach out to the StudyHub team:\nsupport@studyhub.bd"
                )
            },
            confirmButton = {
                Button(
                    onClick = { showHelpDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
                ) {
                    Text("Got it")
                }
            }
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = StudyNavyPrimary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = StudyPrimaryText
            ),
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = StudySecondaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsScreen(
    profile: UserProfile?,
    onBack: () -> Unit,
    onResetData: () -> Unit,
    modifier: Modifier = Modifier
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var soundEnabled by remember { mutableStateOf(true) }
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
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
                modifier = Modifier.testTag("settings_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = StudyNavyPrimary
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Settings",
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
            // Appearance Section matching mockup (Light active)
            item {
                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Appearance",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = StudyTealAction,
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LightMode,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Light", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DarkMode,
                                        contentDescription = null,
                                        tint = StudySecondaryText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Dark", color = StudySecondaryText, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Notifications Section
            item {
                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Study Reminders", style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    text = "Alerts for scheduled tasks & exams",
                                    style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                                )
                            }
                            Switch(
                                checked = notificationsEnabled,
                                onCheckedChange = { notificationsEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudyTealAction
                                )
                            )
                        }
                    }
                }
            }

            // Data & Local Persistence Section
            item {
                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Data & Storage",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = StudyTealAction,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Local Room SQLite Database: Active & Synced",
                                style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                            )
                        }
                        Text(
                            text = "All your study tasks, notes, sessions, and syllabus progress are preserved on-device even when offline.",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = { showResetDialog = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD14343))
                        ) {
                            Text("Reset Sample Data & Profile")
                        }
                    }
                }
            }

            // About & Version info
            item {
                StudyHubCard(contentPadding = PaddingValues(16.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "About StudyHub",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StudyNavyPrimary
                            )
                        )
                        Text(
                            text = "StudyHub v1.0.0 (Release Build 2026)",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                        Text(
                            text = "Dedicated study management app for SSC, HSC, and University Admission candidates in Bangladesh.",
                            style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Study Data?") },
            text = { Text("This will restore default demo seed data and profile.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetData()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD14343))
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
