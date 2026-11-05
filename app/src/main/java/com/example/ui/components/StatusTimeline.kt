package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BookingStatus
import com.example.ui.theme.OrangePrimary

@Composable
fun StatusTimeline(
    currentStatus: BookingStatus,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        BookingStatus.REQUESTED to "Booking Received",
        BookingStatus.ASSIGNED to "Pro Assigned",
        BookingStatus.ON_THE_WAY to "On The Way",
        BookingStatus.INSPECTING to "Inspection & Quote",
        BookingStatus.IN_PROGRESS to "Work In Progress",
        BookingStatus.COMPLETED to "Completed"
    )

    val currentStepIndex = when (currentStatus) {
        BookingStatus.PAID -> 6
        BookingStatus.CANCELLED -> 0
        else -> currentStatus.stepIndex
    }

    Column(modifier = modifier.fillMaxWidth()) {
        steps.forEachIndexed { index, (status, label) ->
            val stepNumber = index + 1
            val isDone = currentStepIndex > stepNumber || (currentStepIndex == stepNumber && (currentStatus == BookingStatus.COMPLETED || currentStatus == BookingStatus.PAID))
            val isCurrent = currentStepIndex == stepNumber && currentStatus != BookingStatus.COMPLETED && currentStatus != BookingStatus.PAID
            val isPending = currentStepIndex < stepNumber

            val circleColor by animateColorAsState(
                targetValue = when {
                    isDone -> Color(0xFF10B981) // Green
                    isCurrent -> OrangePrimary // Active Orange
                    else -> Color(0xFFE2E8F0) // Gray
                },
                label = "circleColor"
            )

            val lineColor by animateColorAsState(
                targetValue = if (currentStepIndex > stepNumber) Color(0xFF10B981) else Color(0xFFE2E8F0),
                label = "lineColor"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Indicator column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(circleColor),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isDone) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = "$stepNumber",
                                color = if (isCurrent) Color.White else Color(0xFF64748B),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (index < steps.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.5.dp)
                                .height(28.dp)
                                .background(lineColor)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Text column
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = if (index < steps.size - 1) 16.dp else 4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                            color = when {
                                isCurrent -> OrangePrimary
                                isDone -> Color(0xFF0F172A)
                                else -> Color(0xFF94A3B8)
                            },
                            fontSize = 13.5.sp
                        )
                    )

                    if (isCurrent) {
                        Text(
                            text = status.subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF475569),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}
