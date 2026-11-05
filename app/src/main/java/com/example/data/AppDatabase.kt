package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [BookingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookingDao(): BookingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "badlapur_civil_services.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Prepopulate sample realistic active booking
                            CoroutineScope(Dispatchers.IO).launch {
                                getInstance(context).bookingDao().insertBooking(
                                    BookingEntity(
                                        serviceType = "PLUMBING",
                                        taskName = "Pipe Fitting & Concealed Leakage",
                                        customerName = "Anil Sharma",
                                        customerPhone = "9823154820",
                                        customerAddress = "Flat 302, Sai Sparsh Apts, Katrap Road",
                                        locality = "Katrap",
                                        bookingDate = "Today",
                                        timeSlot = "10:30 AM - 12:30 PM",
                                        isUrgent = true,
                                        materialPreference = "TECHNICIAN_BRINGS",
                                        problemDescription = "Bathroom concealed wall pipe leaking, requires valve replacement and new CPVC pipe joint.",
                                        status = "ON_THE_WAY",
                                        technicianName = "Badlapur Plumbing Specialist",
                                        technicianPhone = "9284292348",
                                        technicianRating = 4.9f,
                                        technicianSpecialty = "Certified Master Plumber",
                                        technicianExperienceYears = 9,
                                        isPoliceVerified = true,
                                        serviceCharge = 450.0,
                                        materialCharge = 320.0,
                                        materialItems = "1/2 inch CPVC Brass Elbow (2 pcs), Solvent Cement, Teflon Tape",
                                        totalAmount = 770.0,
                                        isQuoteApproved = true,
                                        paymentMethod = "",
                                        paymentStatus = "PENDING",
                                        createdAt = System.currentTimeMillis() - 35 * 60 * 1000
                                    )
                                )
                                getInstance(context).bookingDao().insertBooking(
                                    BookingEntity(
                                        serviceType = "FABRICATION",
                                        taskName = "Window Safety Grill & Railing",
                                        customerName = "Pooja Kulkarni",
                                        customerPhone = "9892011234",
                                        customerAddress = "Row House 4, Near Hendre Pada Garden",
                                        locality = "Hendre Pada",
                                        bookingDate = "Yesterday",
                                        timeSlot = "02:00 PM",
                                        isUrgent = false,
                                        materialPreference = "CUSTOMER_PROVIDES",
                                        problemDescription = "Welding and alignment of balcony safety grill.",
                                        status = "PAID",
                                        technicianName = "Ramesh Jadhav",
                                        technicianPhone = "9284292348",
                                        technicianRating = 4.8f,
                                        technicianSpecialty = "Senior Welder & Fabricator",
                                        technicianExperienceYears = 12,
                                        isPoliceVerified = true,
                                        serviceCharge = 650.0,
                                        materialCharge = 0.0,
                                        materialItems = "Customer provided MS rods & welding electrodes (Labor Only)",
                                        totalAmount = 650.0,
                                        isQuoteApproved = true,
                                        paymentMethod = "UPI",
                                        paymentStatus = "PAID",
                                        transactionId = "TXN_BDP_883492",
                                        customerRating = 5.0f,
                                        customerReview = "Superb fast doorstep service in Badlapur! Honest pricing, no extra charges since I provided the materials.",
                                        createdAt = System.currentTimeMillis() - 26 * 60 * 60 * 1000,
                                        completedAt = System.currentTimeMillis() - 24 * 60 * 60 * 1000
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
