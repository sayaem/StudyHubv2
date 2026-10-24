package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
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
import androidx.compose.material3.IconButton
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

@Composable
fun StudyHubDrawerContent(
    profile: UserProfile?,
    onNavigateTab: (NavTab) -> Unit,
    onNavigateAnalytics: () -> Unit,
    onNavigateFocusTimer: () -> Unit,
    onNavigateNotes: () -> Unit,
    onNavigateReminders: () -> Unit,
    onNavigateSettings: () -> Unit,
    onToggleTheme: ((String) -> Unit)? = null,
    onCloseDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp),
        color = MaterialTheme.colorScheme.surface
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
                modifier = Modifier.padding(bottom = 18.dp)
            ) {
                StudentAvatarBadge(
                    avatarId = profile?.avatarId ?: 1,
                    name = profile?.displayName ?: "Student",
                    size = 52.dp
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = profile?.displayName?.ifBlank { "StudyHub Student" } ?: "StudyHub Student",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "${profile?.level ?: "HSC"} • ${if (profile?.level == "Admission") profile.admissionCategory else profile?.groupName ?: "Science"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            HorizontalDivider(thickness = 0.8.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

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
                label = "Study Plan & Tasks",
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
                label = "Performance Analytics",
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
                label = "Reminders & Alerts",
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

            // Quick Theme Switcher in Drawer
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Theme: ${profile?.themeMode ?: "SYSTEM"}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                    Row {
                        IconButton(
                            onClick = { onToggleTheme?.invoke("LIGHT") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LightMode, contentDescription = "Light Theme", modifier = Modifier.size(16.dp))
                        }
                        IconButton(
                            onClick = { onToggleTheme?.invoke("DARK") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(imageVector = Icons.Default.DarkMode, contentDescription = "Dark Theme", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            HorizontalDivider(thickness = 0.8.dp, color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(10.dp))

            // Preferences link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onNavigateSettings()
                        onCloseDrawer()
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Preferences & Setup",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            .padding(vertical = 11.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
