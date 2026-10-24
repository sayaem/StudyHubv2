package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StudyHubColorScheme = lightColorScheme(
    primary = StudyTealAction,
    onPrimary = Color.White,
    primaryContainer = StudyTealLight,
    onPrimaryContainer = StudyTealDark,
    secondary = StudyNavyPrimary,
    onSecondary = Color.White,
    secondaryContainer = StudyAccentSky,
    onSecondaryContainer = StudyNavyPrimary,
    tertiary = StudyTealDark,
    onTertiary = Color.White,
    background = StudyBackground,
    onBackground = StudyPrimaryText,
    surface = StudySurface,
    onSurface = StudyPrimaryText,
    surfaceVariant = StudyBackground,
    onSurfaceVariant = StudySecondaryText,
    outline = StudyBorder,
    outlineVariant = Color(0xFFCBD5E1),
    error = StudyError,
    onError = Color.White,
    errorContainer = StudyErrorLight,
    onErrorContainer = StudyError
)

@Composable
fun StudyHubTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudyHubColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    StudyHubTheme(content = content)
}
