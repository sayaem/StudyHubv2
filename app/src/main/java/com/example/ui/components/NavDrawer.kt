package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Notes
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight

@Composable
fun StudyHubDrawerContent(
    profile: UserProfile?,
    onNavigateTab: (NavTab) -> Unit,
    onNavigateAnalytics: () -> Unit,
    onNavigateFocusTimer: () -> Unit,
    onNavigateNotes: () -> Unit,
    onNavigateReminders: () -> Unit,
    onNavigateSettings: () -> Unit,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header in Drawer
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = CircleShape,
                    color = Color(0xFF4338CA) // Blue avatar badge from mockup
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = profile?.displayName?.split(" ")?.mapNotNull { it.firstOrNull()?.toString() }?.take(2)?.joinToString("") ?: "SA",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = profile?.displayName ?: "Sayem Arafat",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StudyNavyPrimary
                        )
                    )
                    Text(
                        text = "${profile?.level ?: "HSC"} • ${profile?.groupName ?: "Science"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                    )
                }
            }

            HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Links
            DrawerItem(
                icon = Icons.Outlined.Home,
                label = "Home",
                onClick = {
                    onNavigateTab(NavTab.HOME)
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.CalendarMonth,
                label = "My Study Plan",
                onClick = {
                    onNavigateTab(NavTab.PLAN)
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.MenuBook,
                label = "Syllabus Tracker",
                onClick = {
                    onNavigateTab(NavTab.TRACK)
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.Analytics,
                label = "Performance",
                onClick = {
                    onNavigateAnalytics()
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.Timer,
                label = "Focus Timer",
                onClick = {
                    onNavigateFocusTimer()
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.Notes,
                label = "Notes & Flashcards",
                onClick = {
                    onNavigateNotes()
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.Notifications,
                label = "Reminders",
                onClick = {
                    onNavigateReminders()
                    onCloseDrawer()
                }
            )
            DrawerItem(
                icon = Icons.Outlined.Settings,
                label = "Settings",
                onClick = {
                    onNavigateSettings()
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
            Spacer(modifier = Modifier.height(12.dp))

            // Log Out / Reset
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateSettings()
                        onCloseDrawer()
                    }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = StudySecondaryText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Preferences & Reset",
                    style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText)
                )
            }
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = StudyNavyPrimary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = StudyPrimaryText
            )
        )
    }
}
