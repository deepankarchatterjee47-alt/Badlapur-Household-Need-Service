package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BADLAPUR_LOCALITIES
import com.example.model.CIVIL_TASKS
import com.example.model.MaterialPreference
import com.example.model.ServiceTaskItem
import com.example.model.ServiceType
import com.example.ui.components.MaterialPolicyBanner
import com.example.ui.components.getServiceIcon
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangePrimary
import com.example.viewmodel.BookingDraft
import com.example.viewmodel.CivilServiceViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingSheet(
    viewModel: CivilServiceViewModel,
    draft: BookingDraft,
    onBack: () -> Unit,
    onBookingSuccess: (Long) -> Unit
) {
    BackHandler { onBack() }

    val scrollState = rememberScrollState()

    var customerName by remember { mutableStateOf(draft.customerName) }
    var customerPhone by remember { mutableStateOf(draft.customerPhone) }
    var customerAddress by remember { mutableStateOf(draft.customerAddress) }
    var locality by remember { mutableStateOf(draft.locality) }
    var materialPreference by remember { mutableStateOf(draft.materialPreference) }
    var isUrgent by remember { mutableStateOf(draft.isUrgent) }
    var problemDescription by remember { mutableStateOf(draft.problemDescription) }
    var selectedTask by remember { mutableStateOf<ServiceTaskItem?>(draft.taskItem) }
    var selectedDate by remember { mutableStateOf(draft.bookingDate) }
    var selectedSlot by remember { mutableStateOf(draft.timeSlot) }

    val relevantTasks = CIVIL_TASKS.filter { it.serviceType == draft.serviceType }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Book ${draft.serviceType.title}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Doorstep Service • Badlapur",
                            fontSize = 11.sp,
                            color = Color(0xFFCBD5E1)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("booking_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
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
            // Service Category Header Badge & Switcher
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFE2E8F0))
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(OrangePrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getServiceIcon(draft.serviceType),
                                contentDescription = null,
                                tint = OrangePrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "${draft.serviceType.title} Services",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color(0xFF0F172A)
                            )
                            Text(
                                text = "Vetted Badlapur professionals • Inspection ₹${draft.serviceType.baseInspectionFee}",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Change category need:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ServiceType.values().forEach { st ->
                            val isStSelected = st == draft.serviceType
                            FilterChip(
                                selected = isStSelected,
                                onClick = {
                                    if (st != draft.serviceType) {
                                        viewModel.initBookingDraft(st)
                                    }
                                },
                                label = { Text(st.title, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NavyPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Select specific task or custom need
            Text(
                text = "1. Choose Your Specific Service Need",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Pick from common requirements below or choose 'Custom Requirement' to state your exact needs.",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(8.dp))

            relevantTasks.forEach { task ->
                val isSelected = selectedTask?.id == task.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .testTag("task_select_${task.id}")
                        .clickable { selectedTask = task },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFEFF6FF) else Color.White
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(
                            if (isSelected) NavyPrimary else Color(0xFFE2E8F0)
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = task.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = if (isSelected) NavyPrimary else Color(0xFF1E293B)
                            )
                            Text(
                                text = task.description,
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Estimated Labor: ${task.typicalLaborRange}",
                                    fontSize = 11.sp,
                                    color = OrangePrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = NavyPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Custom Requirement Card
            val isCustomSelected = selectedTask == null
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .testTag("task_select_custom")
                    .clickable { selectedTask = null },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCustomSelected) Color(0xFFFFFBEB) else Color.White
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isCustomSelected) OrangePrimary else Color(0xFFE2E8F0)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Custom / Other Specific Need",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = if (isCustomSelected) OrangePrimary else Color(0xFF1E293B)
                        )
                        Text(
                            text = "Need something specific or combined? Describe exactly what you need in the details box below.",
                            fontSize = 11.5.sp,
                            color = Color(0xFF64748B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rate estimated after doorstep inspection",
                            fontSize = 11.sp,
                            color = Color(0xFF0369A1),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (isCustomSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = OrangePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. CRITICAL MATERIAL POLICY SELECTION
            Text(
                text = "2. Material & Product Arrangement",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "As per policy, service person decides work amount based on your material choice.",
                fontSize = 11.5.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Option A: Customer provides
            val isOptionA = materialPreference == MaterialPreference.CUSTOMER_PROVIDES
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("material_choice_customer")
                    .clickable { materialPreference = MaterialPreference.CUSTOMER_PROVIDES },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOptionA) Color(0xFFF0FDF4) else Color.White
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isOptionA) Color(0xFF16A34A) else Color(0xFFE2E8F0)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (isOptionA) Icons.Default.CheckCircle else Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = if (isOptionA) Color(0xFF16A34A) else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "I will provide the materials / parts",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = if (isOptionA) Color(0xFF166534) else Color(0xFF1E293B)
                        )
                        Text(
                            text = "★ Only service / labor charges will be taken by the service person.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF15803D)
                        )
                        Text(
                            text = "You supply pipes, fittings, paint, tiles, switches, etc.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Option B: Technician brings
            val isOptionB = materialPreference == MaterialPreference.TECHNICIAN_BRINGS
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("material_choice_technician")
                    .clickable { materialPreference = MaterialPreference.TECHNICIAN_BRINGS },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOptionB) Color(0xFFFFF7ED) else Color.White
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isOptionB) OrangePrimary else Color(0xFFE2E8F0)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (isOptionB) Icons.Default.CheckCircle else Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = if (isOptionB) OrangePrimary else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp).padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Technician to bring required products / materials",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp,
                            color = if (isOptionB) Color(0xFF9A3412) else Color(0xFF1E293B)
                        )
                        Text(
                            text = "★ Additional charges will be acceptable & itemized on the bill.",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = OrangePrimary
                        )
                        Text(
                            text = "Technician procures genuine parts from local Badlapur hardware store.",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Badlapur Locality & Address
            Text(
                text = "3. Service Address in Badlapur",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Select Locality:",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.height(4.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BADLAPUR_LOCALITIES.take(8).forEach { loc ->
                    val isLocSelected = locality.equals(loc.name, ignoreCase = true)
                    FilterChip(
                        selected = isLocSelected,
                        onClick = { locality = loc.name },
                        label = { Text("${loc.name} (${loc.zone})", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customerAddress,
                onValueChange = { customerAddress = it },
                label = { Text("Flat / House No, Building & Landmark") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = NavyPrimary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_address"),
                shape = RoundedCornerShape(8.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Your Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NavyPrimary) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_name"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = NavyPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("input_phone"),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Timing & Urgency
            Text(
                text = "4. Service Schedule",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0F172A)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUrgent) Color(0xFFFFF7ED) else Color.White
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (isUrgent) OrangePrimary else Color(0xFFE2E8F0)
                    )
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(OrangePrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Urgent",
                            tint = OrangePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Urgent Doorstep Visit (Within 60 Mins)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Assigns priority vetted technician on duty in Badlapur",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                    Switch(
                        checked = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        modifier = Modifier.testTag("urgent_switch"),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = OrangePrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = problemDescription,
                onValueChange = { problemDescription = it },
                label = { Text("Problem Description / Work Details (Optional)") },
                placeholder = { Text("e.g. Tap leaking continuously under kitchen sink") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_description"),
                shape = RoundedCornerShape(8.dp),
                minLines = 2,
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            MaterialPolicyBanner()

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    viewModel.updateDraftCustomerInfo(customerName, customerPhone, customerAddress, locality)
                    viewModel.updateDraftMaterialPreference(materialPreference)
                    selectedTask?.let { viewModel.updateDraftTask(it) }
                    viewModel.updateDraftSchedule(selectedDate, selectedSlot, isUrgent)
                    viewModel.updateDraftDescription(problemDescription)
                    viewModel.submitBookingDraft { newId ->
                        onBookingSuccess(newId)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_booking_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Confirm Doorstep Booking",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
