package com.example.repository

import com.example.data.BookingDao
import com.example.data.BookingEntity
import com.example.model.BookingStatus
import com.example.model.MaterialPreference
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CivilServiceRepository(private val bookingDao: BookingDao) {

    val allBookings: Flow<List<BookingEntity>> = bookingDao.getAllBookings()
    val activeBooking: Flow<BookingEntity?> = bookingDao.getLatestActiveBooking()

    fun getBookingById(id: Long): Flow<BookingEntity?> = bookingDao.getBookingById(id)

    suspend fun createBooking(
        serviceType: String,
        taskName: String,
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        locality: String,
        bookingDate: String,
        timeSlot: String,
        isUrgent: Boolean,
        materialPreference: MaterialPreference,
        problemDescription: String
    ): Long {
        // Select an appropriate vetted professional for Badlapur area based on service type
        val technician = getVettedTechnicianForService(serviceType)

        val newBooking = BookingEntity(
            serviceType = serviceType,
            taskName = taskName,
            customerName = customerName,
            customerPhone = customerPhone,
            customerAddress = customerAddress,
            locality = locality,
            bookingDate = bookingDate,
            timeSlot = timeSlot,
            isUrgent = isUrgent,
            materialPreference = materialPreference.name,
            problemDescription = problemDescription,
            status = BookingStatus.REQUESTED.name,
            technicianName = technician.name,
            technicianPhone = technician.phone,
            technicianRating = technician.rating,
            technicianSpecialty = technician.specialty,
            technicianExperienceYears = technician.experienceYears,
            isPoliceVerified = true,
            serviceCharge = 0.0,
            materialCharge = 0.0,
            materialItems = if (materialPreference == MaterialPreference.CUSTOMER_PROVIDES) {
                "Customer providing own materials (Labor charge only)"
            } else {
                "Awaiting technician site inspection for material checklist"
            },
            totalAmount = 0.0,
            isQuoteApproved = false,
            createdAt = System.currentTimeMillis()
        )

        return bookingDao.insertBooking(newBooking)
    }

    suspend fun updateStatus(booking: BookingEntity, nextStatus: BookingStatus) {
        val updated = booking.copy(
            status = nextStatus.name,
            completedAt = if (nextStatus == BookingStatus.COMPLETED || nextStatus == BookingStatus.PAID) System.currentTimeMillis() else booking.completedAt
        )
        bookingDao.updateBooking(updated)
    }

    suspend fun submitTechnicianQuote(
        booking: BookingEntity,
        serviceCharge: Double,
        materialCharge: Double,
        materialItems: String
    ) {
        val total = serviceCharge + materialCharge
        val updated = booking.copy(
            serviceCharge = serviceCharge,
            materialCharge = materialCharge,
            materialItems = materialItems,
            totalAmount = total,
            isQuoteApproved = false,
            status = BookingStatus.INSPECTING.name
        )
        bookingDao.updateBooking(updated)
    }

    suspend fun approveQuote(booking: BookingEntity) {
        val updated = booking.copy(
            isQuoteApproved = true,
            status = BookingStatus.IN_PROGRESS.name
        )
        bookingDao.updateBooking(updated)
    }

    suspend fun processPayment(
        booking: BookingEntity,
        paymentMethod: String,
        amount: Double
    ): String {
        val txnId = "TXN_BDP_" + (100000..999999).random()
        val updated = booking.copy(
            paymentMethod = paymentMethod,
            paymentStatus = "PAID",
            transactionId = txnId,
            status = BookingStatus.PAID.name,
            completedAt = System.currentTimeMillis()
        )
        bookingDao.updateBooking(updated)
        return txnId
    }

    suspend fun submitReview(
        booking: BookingEntity,
        rating: Float,
        reviewText: String
    ) {
        val updated = booking.copy(
            customerRating = rating,
            customerReview = reviewText
        )
        bookingDao.updateBooking(updated)
    }

    suspend fun cancelBooking(booking: BookingEntity) {
        val updated = booking.copy(
            status = BookingStatus.CANCELLED.name
        )
        bookingDao.updateBooking(updated)
    }

    private data class VettedTechnician(
        val name: String,
        val phone: String,
        val specialty: String,
        val rating: Float,
        val experienceYears: Int
    )

    private fun getVettedTechnicianForService(serviceType: String): VettedTechnician {
        return when (serviceType.uppercase()) {
            "PLUMBING" -> VettedTechnician("Badlapur Plumbing Specialist", "9284292348", "Certified Master Plumber", 4.9f, 9)
            "FABRICATION" -> VettedTechnician("Shankar Chatterjee", "9922559228", "Senior Civil Contractor & Fabrication Specialist", 4.9f, 15)
            "FURNITURE" -> VettedTechnician("Dinesh Suthar", "9284292348", "Master Carpenter & Modular Specialist", 4.9f, 11)
            "PAINTING" -> VettedTechnician("Vijay Bhoir", "9284292348", "Professional Wall Painter & Waterproofing", 4.8f, 8)
            "TILES" -> VettedTechnician("Santosh Kanojia", "9284292348", "Master Tile Mason & Granite Expert", 4.9f, 10)
            "ELECTRICIAN" -> VettedTechnician("Nitin Gaikwad", "9284292348", "Licensed Wireman & Electrical Technician", 4.9f, 7)
            "HOME_MAID" -> VettedTechnician("Sunita Shinde & Team", "9284292348", "Verified Deep House Cleaning Specialist", 4.9f, 6)
            else -> VettedTechnician("Shankar Chatterjee", "9922559228", "Senior Civil Maintenance Supervisor", 4.9f, 15)
        }
    }
}
