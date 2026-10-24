package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = StudyTealAction,
    onPrimary = Color.White,
    primaryContainer = StudyTealLight,
    onPrimaryContainer = StudyTealDark,
    secondary = StudyNavyPrimary,
    onSecondary = Color.White,
    secondaryContainer = StudyAccentSky,
    onSecondaryContainer = StudyNavyPrimary,
    tertiary = Color(0xFF0284C7),
    onTertiary = Color.White,
    background = StudyBackgroundLight,
    onBackground = StudyPrimaryTextLight,
    surface = StudySurfaceLight,
    onSurface = StudyPrimaryTextLight,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = StudySecondaryTextLight,
    outline = StudyBorderLight,
    outlineVariant = Color(0xFFE2E8F0),
    error = StudyError,
    onError = Color.White,
    errorContainer = StudyErrorLight,
    onErrorContainer = StudyError
)

private val DarkColorScheme = darkColorScheme(
    primary = StudyTealLuminous,
    onPrimary = Color(0xFF042F2E),
    primaryContainer = Color(0xFF134E4A),
    onPrimaryContainer = Color(0xFFCCFBF1),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF082F49),
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = Color(0xFFE0F2FE),
    tertiary = Color(0xFFA78BFA),
    onTertiary = Color(0xFF2E1065),
    background = StudyBackgroundDark,
    onBackground = StudyPrimaryTextDark,
    surface = StudySurfaceDark,
    onSurface = StudyPrimaryTextDark,
    surfaceVariant = StudyCardDark,
    onSurfaceVariant = StudySecondaryTextDark,
    outline = StudyBorderDark,
    outlineVariant = Color(0xFF1E2A44),
    error = StudyError,
    onError = Color.White,
    errorContainer = StudyErrorDark,
    onErrorContainer = Color(0xFFFECACA)
)

@Composable
fun StudyHubTheme(
    themeMode: String = "SYSTEM", // "SYSTEM", "LIGHT", "DARK"
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode.uppercase()) {
        "DARK" -> true
        "LIGHT" -> false
        else -> systemDark
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = !isDark
                controller.isAppearanceLightNavigationBars = !isDark
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    StudyHubTheme(
        themeMode = if (darkTheme) "DARK" else "LIGHT",
        content = content
    )
}
