package com.example.model

enum class MaterialPreference(
    val title: String,
    val description: String,
    val badgeLabel: String
) {
    CUSTOMER_PROVIDES(
        title = "I will provide materials",
        description = "Only service/labor charges will be taken by the technician.",
        badgeLabel = "Customer Provides Materials (Labor Only)"
    ),
    TECHNICIAN_BRINGS(
        title = "Technician to bring materials / products",
        description = "Technician will buy necessary parts from local Badlapur hardware store. Additional material charges will be billed.",
        badgeLabel = "Technician Brings Products (+ Material Cost)"
    )
}

enum class BookingStatus(
    val stepIndex: Int,
    val title: String,
    val subtitle: String,
    val actionHint: String
) {
    REQUESTED(
        stepIndex = 1,
        title = "Service Requested",
        subtitle = "Searching nearest local vetted professional in Badlapur",
        actionHint = "Assigning technician..."
    ),
    ASSIGNED(
        stepIndex = 2,
        title = "Professional Assigned",
        subtitle = "Vetted local technician confirmed your slot",
        actionHint = "Technician preparing tools"
    ),
    ON_THE_WAY(
        stepIndex = 3,
        title = "On The Way",
        subtitle = "Technician is traveling to your doorstep",
        actionHint = "Arriving shortly at your location"
    ),
    INSPECTING(
        stepIndex = 4,
        title = "Inspection & Price Quote",
        subtitle = "Technician inspected the work & submitted the work amount",
        actionHint = "Review & approve quote to start work"
    ),
    IN_PROGRESS(
        stepIndex = 5,
        title = "Work In Progress",
        subtitle = "Professional is executing the maintenance task",
        actionHint = "Quality civil work in execution"
    ),
    COMPLETED(
        stepIndex = 6,
        title = "Work Completed",
        subtitle = "Task finished. Ready for final review & payment",
        actionHint = "Proceed to secure payment"
    ),
    PAID(
        stepIndex = 7,
        title = "Payment Completed",
        subtitle = "Transaction settled with official Badlapur Civil receipt",
        actionHint = "Share your rating & review"
    ),
    CANCELLED(
        stepIndex = 0,
        title = "Booking Cancelled",
        subtitle = "This booking was cancelled",
        actionHint = "Re-book anytime"
    )
}
