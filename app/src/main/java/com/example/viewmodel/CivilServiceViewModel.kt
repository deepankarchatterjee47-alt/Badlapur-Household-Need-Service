package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BookingEntity
import com.example.model.BookingStatus
import com.example.model.MaterialPreference
import com.example.model.ServiceTaskItem
import com.example.model.ServiceType
import com.example.repository.CivilServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CivilServiceViewModel(private val repository: CivilServiceRepository) : ViewModel() {

    val allBookings: StateFlow<List<BookingEntity>> = repository.allBookings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val activeBooking: StateFlow<BookingEntity?> = repository.activeBooking
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Current selected booking for detail/tracking
    private val _selectedBookingId = MutableStateFlow<Long?>(null)
    val selectedBookingId: StateFlow<Long?> = _selectedBookingId.asStateFlow()

    // Navigation and tab states
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Booking Creation Draft State
    private val _bookingDraft = MutableStateFlow(BookingDraft())
    val bookingDraft: StateFlow<BookingDraft> = _bookingDraft.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectBooking(bookingId: Long) {
        _selectedBookingId.value = bookingId
        _currentScreen.value = AppScreen.Tracking(bookingId)
    }

    fun initBookingDraft(serviceType: ServiceType, task: ServiceTaskItem? = null) {
        _bookingDraft.value = BookingDraft(
            serviceType = serviceType,
            taskItem = task,
            taskName = task?.name ?: "${serviceType.title} Maintenance & Inspection",
            materialPreference = MaterialPreference.CUSTOMER_PROVIDES,
            customerName = "Deepankar Chatterjee",
            customerPhone = "9820012345",
            customerAddress = "B-204, Shree Siddhivinayak Heights, Katrap",
            locality = "Katrap",
            bookingDate = "Today",
            timeSlot = "Immediate / Next Available Slot",
            isUrgent = false,
            problemDescription = ""
        )
        _currentScreen.value = AppScreen.CreateBooking
    }

    fun updateDraftCustomerInfo(name: String, phone: String, address: String, locality: String) {
        _bookingDraft.value = _bookingDraft.value.copy(
            customerName = name,
            customerPhone = phone,
            customerAddress = address,
            locality = locality
        )
    }

    fun updateDraftMaterialPreference(preference: MaterialPreference) {
        _bookingDraft.value = _bookingDraft.value.copy(materialPreference = preference)
    }

    fun updateDraftTask(task: ServiceTaskItem) {
        _bookingDraft.value = _bookingDraft.value.copy(
            taskItem = task,
            taskName = task.name
        )
    }

    fun updateDraftSchedule(date: String, slot: String, isUrgent: Boolean) {
        _bookingDraft.value = _bookingDraft.value.copy(
            bookingDate = date,
            timeSlot = slot,
            isUrgent = isUrgent
        )
    }

    fun updateDraftDescription(desc: String) {
        _bookingDraft.value = _bookingDraft.value.copy(problemDescription = desc)
    }

    fun submitBookingDraft(onSuccess: (Long) -> Unit) {
        val draft = _bookingDraft.value
        viewModelScope.launch {
            val newId = repository.createBooking(
                serviceType = draft.serviceType.name,
                taskName = draft.taskName,
                customerName = draft.customerName,
                customerPhone = draft.customerPhone,
                customerAddress = draft.customerAddress,
                locality = draft.locality,
                bookingDate = draft.bookingDate,
                timeSlot = draft.timeSlot,
                isUrgent = draft.isUrgent,
                materialPreference = draft.materialPreference,
                problemDescription = draft.problemDescription
            )
            _selectedBookingId.value = newId
            _currentScreen.value = AppScreen.Tracking(newId)
            onSuccess(newId)
        }
    }

    fun advanceStatus(booking: BookingEntity) {
        viewModelScope.launch {
            val current = try {
                BookingStatus.valueOf(booking.status)
            } catch (e: Exception) {
                BookingStatus.REQUESTED
            }

            val next = when (current) {
                BookingStatus.REQUESTED -> BookingStatus.ASSIGNED
                BookingStatus.ASSIGNED -> BookingStatus.ON_THE_WAY
                BookingStatus.ON_THE_WAY -> {
                    // Technician reaches and inspects work
                    // If no quote exists yet, set realistic quote
                    if (booking.serviceCharge <= 0.0) {
                        val serviceCharge = 350.0
                        val materialCharge = if (booking.materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name) 0.0 else 280.0
                        val items = if (booking.materialPreference == MaterialPreference.CUSTOMER_PROVIDES.name) {
                            "Customer providing materials (Labor only)"
                        } else {
                            "Hardware parts, fasteners & sealing tape"
                        }
                        repository.submitTechnicianQuote(booking, serviceCharge, materialCharge, items)
                    }
                    BookingStatus.INSPECTING
                }
                BookingStatus.INSPECTING -> {
                    // Quote is approved, work begins
                    repository.approveQuote(booking)
                    BookingStatus.IN_PROGRESS
                }
                BookingStatus.IN_PROGRESS -> BookingStatus.COMPLETED
                BookingStatus.COMPLETED -> BookingStatus.PAID
                else -> current
            }

            if (next != current && current != BookingStatus.INSPECTING) {
                repository.updateStatus(booking, next)
            }
        }
    }

    fun submitTechnicianQuote(
        booking: BookingEntity,
        serviceCharge: Double,
        materialCharge: Double,
        itemsDescription: String
    ) {
        viewModelScope.launch {
            repository.submitTechnicianQuote(booking, serviceCharge, materialCharge, itemsDescription)
        }
    }

    fun approveQuote(booking: BookingEntity) {
        viewModelScope.launch {
            repository.approveQuote(booking)
        }
    }

    fun processPayment(
        booking: BookingEntity,
        paymentMethod: String,
        amount: Double,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            val txn = repository.processPayment(booking, paymentMethod, amount)
            onSuccess(txn)
        }
    }

    fun submitReview(booking: BookingEntity, rating: Float, reviewText: String) {
        viewModelScope.launch {
            repository.submitReview(booking, rating, reviewText)
        }
    }

    fun cancelBooking(booking: BookingEntity) {
        viewModelScope.launch {
            repository.cancelBooking(booking)
        }
    }
    private val _workPosts = MutableStateFlow<List<com.example.model.BusinessWorkPost>>(
        listOf(
            com.example.model.BusinessWorkPost(
                id = "post-1",
                title = "Concealed CPVC Pipe Fitting & Leakage Fix",
                description = "Replaced degraded GI pipe line with Finolex CPVC concealed pipes in Katrap society flat.",
                serviceCategory = "Plumbing",
                mediaType = com.example.model.MediaType.PHOTO,
                drawableRes = com.example.R.drawable.badlapur_household_need_logo,
                locality = "Katrap, Badlapur East",
                postedBy = "Shankar Chatterjee (Owner)"
            ),
            com.example.model.BusinessWorkPost(
                id = "post-2",
                title = "Heavy MS Window Safety Grill Fabrication",
                description = "Custom fabricated 16-gauge square bar safety balcony enclosure with anti-rust primer and enamel coat.",
                serviceCategory = "Fabrication",
                mediaType = com.example.model.MediaType.PHOTO,
                drawableRes = com.example.R.drawable.badlapur_household_need_logo,
                locality = "Shirgaon, Badlapur",
                postedBy = "Shankar Chatterjee (Owner)"
            )
        )
    )
    val workPosts: StateFlow<List<com.example.model.BusinessWorkPost>> = _workPosts.asStateFlow()

    // Customer Reviews & Feedback Rating State
    private val _customerReviews = MutableStateFlow<List<com.example.model.CustomerReview>>(
        listOf(
            com.example.model.CustomerReview(
                id = "rev-1",
                customerName = "Prakash Joshi",
                locality = "Katrap",
                serviceName = "Plumbing (Pipe Joint & Tap Leak)",
                rating = 5.0f,
                reviewComment = "Technician arrived in 40 mins! Since I had bought the tap myself, they took only labor charge as promised. Extremely honest service.",
                date = "2 days ago",
                verifiedCustomer = true
            ),
            com.example.model.CustomerReview(
                id = "rev-2",
                customerName = "Sneha Salvi",
                locality = "Shirgaon",
                serviceName = "Electrical Short Circuit Fix",
                rating = 4.8f,
                reviewComment = "Prompt response. Repaired the main MCB breaker safely. Fair pricing decided on spot.",
                date = "4 days ago",
                verifiedCustomer = true
            ),
            com.example.model.CustomerReview(
                id = "rev-3",
                customerName = "Mahesh Patil",
                locality = "Belavali",
                serviceName = "Tiles & Masonry Repair",
                rating = 5.0f,
                reviewComment = "Bathroom tile grouting and waterproofing was executed neatly. Highly recommended in Badlapur.",
                date = "1 week ago",
                verifiedCustomer = true
            )
        )
    )
    val customerReviews: StateFlow<List<com.example.model.CustomerReview>> = _customerReviews.asStateFlow()

    fun addBusinessPost(
        title: String,
        description: String,
        serviceCategory: String,
        mediaType: com.example.model.MediaType,
        mediaUri: String? = null,
        locality: String = "Badlapur"
    ) {
        val newPost = com.example.model.BusinessWorkPost(
            id = "post-${System.currentTimeMillis()}",
            title = title,
            description = description,
            serviceCategory = serviceCategory,
            mediaType = mediaType,
            localUri = mediaUri,
            drawableRes = if (mediaUri == null) com.example.R.drawable.badlapur_household_need_logo else null,
            locality = locality,
            postedBy = "Business Owner (Shankar Chatterjee)"
        )
        _workPosts.value = listOf(newPost) + _workPosts.value
    }

    fun submitCustomerFeedback(
        name: String,
        locality: String,
        serviceName: String,
        rating: Float,
        comment: String
    ) {
        val newReview = com.example.model.CustomerReview(
            id = "rev-${System.currentTimeMillis()}",
            customerName = if (name.isBlank()) "Badlapur Resident" else name,
            locality = locality,
            serviceName = serviceName,
            rating = rating,
            reviewComment = comment,
            date = "Just now",
            verifiedCustomer = true
        )
        _customerReviews.value = listOf(newReview) + _customerReviews.value
    }
}

data class BookingDraft(
    val serviceType: ServiceType = ServiceType.PLUMBING,
    val taskItem: ServiceTaskItem? = null,
    val taskName: String = "Plumbing Service",
    val customerName: String = "Customer",
    val customerPhone: String = "9284292348",
    val customerAddress: String = "Badlapur",
    val locality: String = "Katrap",
    val bookingDate: String = "Today",
    val timeSlot: String = "Immediate (Within 60 mins)",
    val isUrgent: Boolean = false,
    val materialPreference: MaterialPreference = MaterialPreference.CUSTOMER_PROVIDES,
    val problemDescription: String = ""
)

sealed class AppScreen {
    object Home : AppScreen()
    object CreateBooking : AppScreen()
    data class Tracking(val bookingId: Long) : AppScreen()
    data class Payment(val bookingId: Long) : AppScreen()
    object MyBookings : AppScreen()
    object ServicesCatalog : AppScreen()
    object ContactSupport : AppScreen()
    object BusinessPostsAndReviews : AppScreen()
}

class CivilServiceViewModelFactory(private val repository: CivilServiceRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CivilServiceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CivilServiceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
