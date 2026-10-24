package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudyAccentSky
import com.example.ui.theme.StudyBackground
import com.example.ui.theme.StudyNavyPrimary
import com.example.ui.theme.StudyPrimaryText
import com.example.ui.theme.StudySecondaryText
import com.example.ui.theme.StudyTealAction
import com.example.ui.theme.StudyTealLight

@Composable
fun SplashScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F2642),
                        StudyNavyPrimary,
                        Color(0xFF071B30)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // StudyHub Logo Icon Mark
            Surface(
                modifier = Modifier.size(90.dp),
                shape = RoundedCornerShape(24.dp),
                color = StudyTealAction.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, StudyTealAction.copy(alpha = 0.6f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "StudyHub Icon",
                        tint = Color.White,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "StudyHub",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    letterSpacing = (-0.5).sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Plan • Learn • Succeed",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = StudyTealAction,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // Bottom CTA
            Text(
                text = "Your Study Journey Starts Here",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.75f)
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("splash_get_started_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = StudyNavyPrimary
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun OnboardingScreen(
    onSkip: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentSlide by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudyBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
    ) {
        // Skip Button Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onSkip,
                modifier = Modifier.testTag("onboarding_skip_button")
            ) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.labelLarge.copy(color = StudySecondaryText)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Hero Illustration Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2EAF2))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Feature preview icon / graphics
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = StudyTealLight
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (currentSlide) {
                                0 -> Icons.Default.EventNote
                                1 -> Icons.Default.AutoAwesome
                                else -> Icons.Default.BarChart
                            },
                            contentDescription = null,
                            tint = StudyTealAction,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome to StudyHub",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = StudyNavyPrimary,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your all-in-one study management app for SSC, HSC and University Admission.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = StudySecondaryText),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Feature Highlights matching mockup
                FeatureRow(
                    icon = Icons.Default.EventNote,
                    title = "Smart Study Plan",
                    subtitle = "Personalized for your exam goals"
                )
                Spacer(modifier = Modifier.height(12.dp))
                FeatureRow(
                    icon = Icons.Default.AutoAwesome,
                    title = "Track Syllabus Progress",
                    subtitle = "Stay on top of subjects & chapters"
                )
                Spacer(modifier = Modifier.height(12.dp))
                FeatureRow(
                    icon = Icons.Default.BarChart,
                    title = "Boost Performance",
                    subtitle = "With focused practice & analytics"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Dots & Next/Get Started controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Page dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentSlide) 20.dp else 8.dp, 8.dp)
                            .background(
                                color = if (index == currentSlide) StudyTealAction else Color(0xFFCBD5E1),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }

            Button(
                onClick = {
                    if (currentSlide < 2) {
                        currentSlide++
                    } else {
                        onContinue()
                    }
                },
                modifier = Modifier
                    .height(48.dp)
                    .testTag("onboarding_next_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudyTealAction)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (currentSlide == 2) "Get Started" else "Next",
                        style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun FeatureRow(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = StudyAccentSky,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = StudyTealAction,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = StudyPrimaryText
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = StudySecondaryText)
            )
        }
    }
}
