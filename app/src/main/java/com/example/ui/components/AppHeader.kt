package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.DividerDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudyTealAction

@Composable
fun AppHeader(
    title: String? = null,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
    showNotificationBadge: Boolean = true,
    notificationCount: Int = 3,
    onNotificationClick: (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton && onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("header_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = StudyNavyPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            } else if (onMenuClick != null) {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("header_menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = StudyNavyPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }

            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = StudyNavyPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )
            } else {
                // Branded StudyHub Logo Header (matching mockup!)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = StudyTealAction,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "S",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "StudyHub",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = StudyNavyPrimary,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }
            }

            if (actions != null) {
                actions()
            } else if (onNotificationClick != null) {
                Box(contentAlignment = Alignment.TopEnd) {
                    IconButton(
                        onClick = onNotificationClick,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("header_notification_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = StudyNavyPrimary
                        )
                    }
                    if (showNotificationBadge && notificationCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(top = 8.dp, end = 8.dp)
                                .size(10.dp)
                                .background(StudyTealAction, CircleShape)
                        )
                    }
                }
            }
        }
        HorizontalDivider(thickness = 0.8.dp, color = StudyBorder)
    }
}
