package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BookingEntity
import com.example.model.BookingStatus
import com.example.model.MaterialPreference
import com.example.model.ServiceType
import com.example.ui.components.callBusiness
import com.example.ui.components.getServiceColor
import com.example.ui.components.getServiceIcon
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangePrimary
import com.example.viewmodel.CivilServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyBookingsScreen(
    viewModel: CivilServiceViewModel,
    onBack: () -> Unit,
    onBookingClick: (Long) -> Unit,
    onNewBookingClick: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val allBookings by viewModel.allBookings.collectAsState()
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "ACTIVE", "COMPLETED"

    val filteredList = when (selectedFilter) {
        "ACTIVE" -> allBookings.filter { it.status != BookingStatus.PAID.name && it.status != BookingStatus.CANCELLED.name }
        "COMPLETED" -> allBookings.filter { it.status == BookingStatus.PAID.name }
        else -> allBookings
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "My Service Bookings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Badlapur Household Need Service • 9284292348",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("my_bookings_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { callBusiness(context, "9284292348") }) {
                        Icon(Icons.Default.Call, contentDescription = "Call Helpline", tint = Color(0xFFFFD166))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NavyPrimary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewBookingClick,
                containerColor = OrangePrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_new_booking")
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Booking")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8FAFC))
        ) {
            // Filter row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == "ALL",
                    onClick = { selectedFilter = "ALL" },
                    label = { Text("All (${allBookings.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavyPrimary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == "ACTIVE",
                    onClick = { selectedFilter = "ACTIVE" },
                    label = {
                        val count = allBookings.count { it.status != BookingStatus.PAID.name && it.status != BookingStatus.CANCELLED.name }
                        Text("Active ($count)")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = OrangePrimary,
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = selectedFilter == "COMPLETED",
                    onClick = { selectedFilter = "COMPLETED" },
                    label = {
                        val count = allBookings.count { it.status == BookingStatus.PAID.name }
                        Text("Completed ($count)")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF10B981),
                        selectedLabelColor = Color.White
                    )
                )
            }

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PendingActions,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No bookings found in this section",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF475569)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Need plumbing, electrical, tiles, or carpentry in Badlapur?",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNewBookingClick,
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                        ) {
                            Text("Book Doorstep Service Now")
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { booking ->
                        BookingListItemCard(
                            booking = booking,
                            onClick = { onBookingClick(booking.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BookingListItemCard(
    booking: BookingEntity,
    onClick: () -> Unit
) {
    val serviceType = try {
        ServiceType.valueOf(booking.serviceType)
    } catch (e: Exception) {
        ServiceType.PLUMBING
    }

    val currentStatus = try {
        BookingStatus.valueOf(booking.status)
    } catch (e: Exception) {
        BookingStatus.REQUESTED
    }

    val isCustomerProvided = booking.materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("booking_item_${booking.id}")
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(getServiceColor(serviceType).copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getServiceIcon(serviceType),
                            contentDescription = null,
                            tint = getServiceColor(serviceType),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = booking.taskName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "${booking.locality}, Badlapur",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Status Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (currentStatus) {
                                BookingStatus.COMPLETED, BookingStatus.PAID -> Color(0xFFDCFCE7)
                                BookingStatus.ON_THE_WAY -> Color(0xFFFFEDD5)
                                BookingStatus.IN_PROGRESS -> Color(0xFFE0F2FE)
                                else -> Color(0xFFF1F5F9)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = currentStatus.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = when (currentStatus) {
                            BookingStatus.COMPLETED, BookingStatus.PAID -> Color(0xFF166534)
                            BookingStatus.ON_THE_WAY -> OrangePrimary
                            BookingStatus.IN_PROGRESS -> Color(0xFF0369A1)
                            else -> Color(0xFF475569)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Material note & Technician
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isCustomerProvided) "Material: Customer Providing (Labor Only)" else "Material: Technician Brings (+ Material Charge)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isCustomerProvided) Color(0xFF15803D) else Color(0xFF9A3412)
                    )
                    Text(
                        text = "Assigned Pro: ${booking.technicianName.ifEmpty { "Badlapur Civil Team" }}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (booking.totalAmount > 0) "₹${booking.totalAmount.toInt()}" else "Quote Pending",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = if (booking.totalAmount > 0) OrangePrimary else Color(0xFF64748B)
                    )
                    Text(
                        text = "Tap to Track",
                        fontSize = 10.5.sp,
                        color = NavyPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
