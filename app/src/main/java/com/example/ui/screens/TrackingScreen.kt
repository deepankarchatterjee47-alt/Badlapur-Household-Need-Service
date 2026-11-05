package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.model.BookingStatus
import com.example.model.MaterialPreference
import com.example.ui.components.PriceQuoteBreakdownCard
import com.example.ui.components.StatusTimeline
import com.example.ui.components.TechnicianCard
import com.example.ui.components.callBusiness
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangePrimary
import com.example.viewmodel.CivilServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackingScreen(
    bookingId: Long,
    viewModel: CivilServiceViewModel,
    onBack: () -> Unit,
    onProceedToPayment: (Long) -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val bookings by viewModel.allBookings.collectAsState()
    val booking = bookings.find { it.id == bookingId }

    if (booking == null) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Service Tracking", color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NavyPrimary)
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Booking not found. Please select from My Bookings.")
            }
        }
        return
    }

    val currentStatus = try {
        BookingStatus.valueOf(booking.status)
    } catch (e: Exception) {
        BookingStatus.REQUESTED
    }

    var showQuoteDialog by remember { mutableStateOf(false) }
    var showReviewDialog by remember { mutableStateOf(false) }

    // Dialog state for Technician Quote adjustment
    var editLaborCharge by remember { mutableStateOf(if (booking.serviceCharge > 0) booking.serviceCharge.toInt().toString() else "350") }
    var editMaterialCharge by remember {
        mutableStateOf(
            if (booking.materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name) "0"
            else if (booking.materialCharge > 0) booking.materialCharge.toInt().toString() else "250"
        )
    }
    var editMaterialItems by remember {
        mutableStateOf(
            if (booking.materialItems.isNotEmpty()) booking.materialItems
            else "Replacement valve, pipe fittings, Teflon seal"
        )
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Live Service Tracking",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Order #${booking.id} • ${booking.locality}",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("tracking_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.advanceStatus(booking)
                            Toast.makeText(context, "Advanced service stage", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.testTag("tracking_advance_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = "Next Status",
                            tint = Color(0xFFFFD166)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NavyPrimary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Live Status Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (currentStatus) {
                        BookingStatus.COMPLETED, BookingStatus.PAID -> Color(0xFF10B981)
                        BookingStatus.ON_THE_WAY -> OrangePrimary
                        BookingStatus.IN_PROGRESS -> Color(0xFF0284C7)
                        else -> NavyPrimary
                    }
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentStatus.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = currentStatus.subtitle,
                            color = Color(0xFFF1F5F9),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Booking Details Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = booking.taskName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = Color(0xFF0F172A)
                        )
                        if (booking.isUrgent) {
                            Text(
                                text = "Urgent 60 Min",
                                color = OrangePrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${booking.customerAddress}, ${booking.locality}, Badlapur",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }

                    if (booking.problemDescription.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Note: ${booking.problemDescription}",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Assigned Local Vetted Technician
            Text(
                text = "Assigned Local Professional",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(6.dp))

            TechnicianCard(
                name = booking.technicianName,
                phone = booking.technicianPhone.ifEmpty { "9284292348" },
                rating = booking.technicianRating,
                specialty = booking.technicianSpecialty,
                experienceYears = booking.technicianExperienceYears,
                isPoliceVerified = booking.isPoliceVerified
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Work Amount & Quote Breakdown (CRITICAL PROMPT REQUIREMENT)
            Text(
                text = "Work Amount & Pricing",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(6.dp))

            PriceQuoteBreakdownCard(
                materialPreference = booking.materialPreference,
                serviceCharge = booking.serviceCharge,
                materialCharge = booking.materialCharge,
                materialItems = booking.materialItems,
                totalAmount = booking.totalAmount,
                isQuoteApproved = booking.isQuoteApproved,
                onApproveQuote = {
                    viewModel.approveQuote(booking)
                    Toast.makeText(context, "Quote approved! Work in progress.", Toast.LENGTH_SHORT).show()
                },
                onUpdateQuoteClick = {
                    showQuoteDialog = true
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Real-Time Progress Timeline
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Real-Time Tracking Status",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    StatusTimeline(currentStatus = currentStatus)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Action Buttons depending on status
            when (currentStatus) {
                BookingStatus.COMPLETED -> {
                    Button(
                        onClick = { onProceedToPayment(booking.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("proceed_payment_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Proceed to Payment (₹${booking.totalAmount.toInt()})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
                BookingStatus.PAID -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFBBF7D0))
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Service Completed & Paid",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534),
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Transaction ID: ${booking.transactionId}",
                                fontSize = 12.sp,
                                color = Color(0xFF15803D)
                            )

                            if (booking.customerRating > 0f) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    repeat(booking.customerRating.toInt()) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                if (booking.customerReview.isNotEmpty()) {
                                    Text(
                                        text = "\"${booking.customerReview}\"",
                                        fontSize = 12.sp,
                                        color = Color(0xFF334155),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedButton(
                                    onClick = { showReviewDialog = true },
                                    modifier = Modifier.testTag("rate_pro_btn"),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Rate & Review Technician")
                                }
                            }
                        }
                    }
                }
                else -> {
                    // Quick advance button for demo / testing
                    OutlinedButton(
                        onClick = { viewModel.advanceStatus(booking) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("simulate_advance_step_btn"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.FastForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Simulate Next Service Update (${currentStatus.actionHint})",
                            fontSize = 12.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    // Modal Dialog: Technician Quote Adjustment Simulator
    if (showQuoteDialog) {
        val isCustomerSupplying = booking.materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name

        AlertDialog(
            onDismissRequest = { showQuoteDialog = false },
            title = {
                Text(
                    text = "Service Person Quote Tool",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column {
                    Text(
                        text = "Work amount is decided by service person based on inspection.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editLaborCharge,
                        onValueChange = { editLaborCharge = it },
                        label = { Text("Service / Labor Charge (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isCustomerSupplying) {
                        Text(
                            text = "• Customer provides materials -> Material charges set to ₹0",
                            fontSize = 11.sp,
                            color = Color(0xFF16A34A),
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        OutlinedTextField(
                            value = editMaterialCharge,
                            onValueChange = { editMaterialCharge = it },
                            label = { Text("Material / Product Charges (₹)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = editMaterialItems,
                            onValueChange = { editMaterialItems = it },
                            label = { Text("Material Items Purchased") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val labor = editLaborCharge.toDoubleOrNull() ?: 0.0
                        val mat = if (isCustomerSupplying) 0.0 else (editMaterialCharge.toDoubleOrNull() ?: 0.0)
                        val items = if (isCustomerSupplying) "Customer provided materials (Labor only)" else editMaterialItems
                        viewModel.submitTechnicianQuote(booking, labor, mat, items)
                        showQuoteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("Update Quote")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuoteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal Dialog: Review & Rating
    if (showReviewDialog) {
        var ratingVal by remember { mutableFloatStateOf(5.0f) }
        var reviewText by remember { mutableStateOf("Great doorstep civil service in Badlapur, very professional!") }

        AlertDialog(
            onDismissRequest = { showReviewDialog = false },
            title = { Text("Rate ${booking.technicianName}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { ratingVal = star.toFloat() }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "$star stars",
                                    tint = if (star <= ratingVal) Color(0xFFF59E0B) else Color(0xFFCBD5E1),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text("Your Review / Feedback") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitReview(booking, ratingVal, reviewText)
                        showReviewDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Submit Review")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewDialog = false }) {
                    Text("Skip")
                }
            }
        )
    }
}
