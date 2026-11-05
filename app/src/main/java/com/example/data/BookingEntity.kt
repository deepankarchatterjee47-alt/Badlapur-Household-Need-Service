package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val serviceType: String,
    val taskName: String,
    val customerName: String,
    val customerPhone: String,
    val customerAddress: String,
    val locality: String,
    val bookingDate: String,
    val timeSlot: String,
    val isUrgent: Boolean = false,
    val materialPreference: String, // "CUSTOMER_PROVIDES" or "TECHNICIAN_BRINGS"
    val problemDescription: String = "",
    val status: String, // "REQUESTED", "ASSIGNED", "ON_THE_WAY", "INSPECTING", "IN_PROGRESS", "COMPLETED", "PAID", "CANCELLED"
    val technicianName: String = "",
    val technicianPhone: String = "",
    val technicianRating: Float = 4.9f,
    val technicianSpecialty: String = "",
    val technicianExperienceYears: Int = 6,
    val isPoliceVerified: Boolean = true,
    val serviceCharge: Double = 0.0, // Decided by service person
    val materialCharge: Double = 0.0, // Additional charges if customer requests technician to bring products
    val materialItems: String = "",
    val totalAmount: Double = 0.0,
    val isQuoteApproved: Boolean = false,
    val paymentMethod: String = "", // "UPI", "CASH", "CARD"
    val paymentStatus: String = "PENDING", // "PENDING", "PAID"
    val transactionId: String = "",
    val customerRating: Float = 0f,
    val customerReview: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long = 0L
)
