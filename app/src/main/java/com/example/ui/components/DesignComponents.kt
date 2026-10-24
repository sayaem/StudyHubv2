package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudyTaskEntity
import com.example.data.model.TaskPriority
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyBorder
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudySuccess
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight

@Composable
fun StudyHubCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = Color.White,
    borderColor: Color = StudyBorder,
    cornerRadius: Dp = 18.dp,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.padding(contentPadding)) {
            content()
        }
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: ImageVector? = null,
    testTag: String = "primary_button"
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = StudyTealAction,
            contentColor = Color.White,
            disabledContainerColor = StudyTealAction.copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.8f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.5.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                )
            }
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    testTag: String = "secondary_button"
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag(testTag),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = StudyNavyPrimary
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudyBorder)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp)
        )
    }
}

@Composable
fun CircularProgressGauge(
    progress: Float, // 0.0f to 1.0f
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = StudyTealAction,
    trackColor: Color = StudyAccentSky,
    centerContent: @Composable () -> Unit
) {
    val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), label = "gauge_progress")
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            // Background track
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )
            // Progress arc
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke
            )
        }
        centerContent()
    }
}

@Composable
fun SubjectTag(
    name: String,
    modifier: Modifier = Modifier,
    colorHex: String = "#087F8C"
) {
    val color = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (_: Exception) {
        StudyTealAction
    }
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun TaskCardItem(
    task: StudyTaskEntity,
    onToggleComplete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    StudyHubCard(
        modifier = modifier.testTag("task_item_${task.id}"),
        onClick = onClick,
        borderColor = if (task.isCompleted) StudyBorder else StudyBorder,
        contentPadding = PaddingValues(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Circular custom checkbox
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = if (task.isCompleted) StudySuccess else StudySecondaryText.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
                    .background(if (task.isCompleted) StudySuccess else Color.Transparent)
                    .clickable(onClick = onToggleComplete)
                    .testTag("task_checkbox_${task.id}"),
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (task.isCompleted) StudySecondaryText else StudyPrimaryText,
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SubjectTag(
                        name = task.subjectName,
                        colorHex = when (task.subjectName) {
                            "Physics" -> "#087F8C"
                            "Chemistry" -> "#E05638"
                            "Mathematics" -> "#2563EB"
                            "Biology" -> "#10B981"
                            "English" -> "#8B5CF6"
                            else -> "#0284C7"
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${task.estimatedMinutes / 60}h ${task.estimatedMinutes % 60}m",
                        style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
                    )
                }
            }

            if (task.priority == TaskPriority.HIGH && !task.isCompleted) {
                Surface(
                    color = Color(0xFFFDE8E8),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(start = 6.dp)
                ) {
                    Text(
                        text = "High",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFFD14343),
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateView(
    icon: ImageVector,
    title: String,
    subtitle: String,
    ctaText: String? = null,
    onCtaClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = StudyAccentSky
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = StudyTealAction,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(color = StudyPrimaryText),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText),
            textAlign = TextAlign.Center
        )
        if (ctaText != null && onCtaClick != null) {
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onCtaClick,
                colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(ctaText)
            }
        }
    }
}
