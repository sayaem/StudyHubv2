package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.R

object StudyHubNotificationManager {

    const val CHANNEL_TASKS = "studyhub_tasks_channel"
    const val CHANNEL_GOALS = "studyhub_goals_channel"
    const val CHANNEL_EXAMS = "studyhub_exams_channel"

    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val taskChannel = NotificationChannel(
                CHANNEL_TASKS,
                "Study Tasks & Schedule",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders for scheduled study sessions and task due dates"
                enableVibration(true)
            }

            val goalChannel = NotificationChannel(
                CHANNEL_GOALS,
                "Daily Study Goals",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Daily study progress alerts and milestone check-ins"
            }

            val examChannel = NotificationChannel(
                CHANNEL_EXAMS,
                "Exam Countdowns & Milestones",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for upcoming SSC, HSC, and Admission exams"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannels(listOf(taskChannel, goalChannel, examChannel))
        }
    }

    fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }

    fun sendTestNotification(context: Context, studentName: String = "Student") {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_TASKS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("StudyHub Reminder Active! 🎓")
            .setContentText("Hello $studentName! Your daily study alerts and task notifications are enabled.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            NotificationManagerCompat.from(context).notify(1001, builder.build())
        } catch (_: SecurityException) {
            // Handled safely if revoked
        }
    }

    fun sendTaskReminder(context: Context, taskTitle: String, subject: String) {
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskTitle.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_TASKS)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Time to study: $subject")
            .setContentText(taskTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            NotificationManagerCompat.from(context).notify(taskTitle.hashCode(), builder.build())
        } catch (_: SecurityException) {
            // Handled safely
        }
    }
}
