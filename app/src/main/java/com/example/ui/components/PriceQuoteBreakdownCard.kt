package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MaterialPreference
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangePrimary

@Composable
fun PriceQuoteBreakdownCard(
    materialPreference: String,
    serviceCharge: Double,
    materialCharge: Double,
    materialItems: String,
    totalAmount: Double,
    isQuoteApproved: Boolean,
    onApproveQuote: () -> Unit,
    onUpdateQuoteClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val isCustomerProvided = materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = "Quote",
                        tint = NavyPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Work Amount & Estimate",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 15.sp
                        )
                    )
                }

                Text(
                    text = if (isQuoteApproved) "Approved" else "Awaiting Approval",
                    color = if (isQuoteApproved) Color(0xFF16A34A) else OrangePrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Material Preference Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isCustomerProvided) Color(0xFFF0FDF4) else Color(0xFFFFF7ED))
                    .border(
                        1.dp,
                        if (isCustomerProvided) Color(0xFFBBF7D0) else Color(0xFFFFEDD5),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCustomerProvided) Icons.Default.CheckCircle else Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = if (isCustomerProvided) Color(0xFF16A34A) else OrangePrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (isCustomerProvided) {
                                "Customer Material Selected"
                            } else {
                                "Technician Brings Materials Selected"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (isCustomerProvided) Color(0xFF166534) else Color(0xFF9A3412)
                        )
                        Text(
                            text = if (isCustomerProvided) {
                                "Only labor / service charge will be taken by professional."
                            } else {
                                "Additional charges added for required products from Badlapur store."
                            },
                            fontSize = 11.sp,
                            color = if (isCustomerProvided) Color(0xFF15803D) else Color(0xFF7C2D12)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Pricing Breakdown
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(12.dp)
            ) {
                // Service/Labor Charge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Handyman,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Service / Labor Charge",
                            fontSize = 13.sp,
                            color = Color(0xFF334155),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Text(
                        text = if (serviceCharge > 0) "₹${serviceCharge.toInt()}" else "Pending Inspection",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Material Charges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Material / Product Charges",
                                fontSize = 13.sp,
                                color = Color(0xFF334155),
                                fontWeight = FontWeight.Medium
                            )
                            if (materialItems.isNotEmpty()) {
                                Text(
                                    text = materialItems,
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF64748B),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                    Text(
                        text = if (isCustomerProvided) "₹0 (Provided by you)" else if (materialCharge > 0) "₹${materialCharge.toInt()}" else "₹0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = if (isCustomerProvided) Color(0xFF16A34A) else Color(0xFF0F172A)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(10.dp))

                // Total Work Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Work Amount",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = "Decided by service professional",
                            fontSize = 10.5.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    Text(
                        text = if (totalAmount > 0) "₹${totalAmount.toInt()}" else "₹0",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = OrangePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action
            if (!isQuoteApproved && totalAmount > 0) {
                Button(
                    onClick = onApproveQuote,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("approve_quote_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Approve",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Approve Quote & Proceed (₹${totalAmount.toInt()})",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            if (onUpdateQuoteClick != null) {
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedButton(
                    onClick = onUpdateQuoteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("simulate_pro_quote_button"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "Adjust",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Adjust Pro Quote (Labor / Material Simulator)",
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
