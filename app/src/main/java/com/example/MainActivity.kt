package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppDatabase
import com.example.repository.CivilServiceRepository
import com.example.ui.screens.BookingSheet
import com.example.ui.screens.ContactSupportScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.PostsAndReviewsScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.OrangePrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.CivilServiceViewModel
import com.example.viewmodel.CivilServiceViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                CivilServiceApp()
            }
        }
    }
}

@Composable
fun CivilServiceApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val repository = remember { CivilServiceRepository(database.bookingDao()) }
    val viewModel: CivilServiceViewModel = viewModel(
        factory = CivilServiceViewModelFactory(repository)
    )

    val currentScreen by viewModel.currentScreen.collectAsState()
    val bookingDraft by viewModel.bookingDraft.collectAsState()

    val showBottomBar = currentScreen is AppScreen.Home ||
            currentScreen is AppScreen.MyBookings ||
            currentScreen is AppScreen.ContactSupport ||
            currentScreen is AppScreen.BusinessPostsAndReviews

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    contentColor = NavyPrimary
                ) {
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.Home,
                        onClick = { viewModel.navigateTo(AppScreen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_home"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = OrangePrimary,
                            indicatorColor = OrangePrimary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.BusinessPostsAndReviews,
                        onClick = { viewModel.navigateTo(AppScreen.BusinessPostsAndReviews) },
                        icon = { Icon(Icons.Default.PhotoLibrary, contentDescription = "Work & Reviews") },
                        label = { Text("Gallery & Reviews", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_gallery_reviews"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = OrangePrimary,
                            indicatorColor = OrangePrimary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.MyBookings,
                        onClick = { viewModel.navigateTo(AppScreen.MyBookings) },
                        icon = { Icon(Icons.Default.Assignment, contentDescription = "Bookings") },
                        label = { Text("My Bookings", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                        modifier = Modifier.testTag("nav_bookings"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = OrangePrimary,
                            indicatorColor = OrangePrimary
                        )
                    )
                    NavigationBarItem(
                        selected = currentScreen is AppScreen.ContactSupport,
                        onClick = { viewModel.navigateTo(AppScreen.ContactSupport) },
                        icon = { Icon(Icons.Default.Call, contentDescription = "9284292348") },
                        label = { Text("9284292348", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_contact"),
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = OrangePrimary,
                            indicatorColor = OrangePrimary
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
        when (val screen = currentScreen) {
            is AppScreen.Home -> {
                HomeScreen(
                    viewModel = viewModel,
                    onServiceSelected = { serviceType ->
                        viewModel.initBookingDraft(serviceType)
                    },
                    onTaskSelected = { task ->
                        viewModel.initBookingDraft(task.serviceType, task)
                    },
                    onViewActiveBooking = { bookingId ->
                        viewModel.selectBooking(bookingId)
                    },
                    onNavigateToMyBookings = {
                        viewModel.navigateTo(AppScreen.MyBookings)
                    },
                    onNavigateToContact = {
                        viewModel.navigateTo(AppScreen.ContactSupport)
                    }
                )
            }

            is AppScreen.CreateBooking -> {
                BookingSheet(
                    viewModel = viewModel,
                    draft = bookingDraft,
                    onBack = { viewModel.navigateTo(AppScreen.Home) },
                    onBookingSuccess = { newId ->
                        viewModel.selectBooking(newId)
                    }
                )
            }

            is AppScreen.Tracking -> {
                TrackingScreen(
                    bookingId = screen.bookingId,
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.Home) },
                    onProceedToPayment = { id ->
                        viewModel.navigateTo(AppScreen.Payment(id))
                    }
                )
            }

            is AppScreen.Payment -> {
                PaymentScreen(
                    bookingId = screen.bookingId,
                    viewModel = viewModel,
                    onBack = { viewModel.selectBooking(screen.bookingId) },
                    onPaymentCompleted = {
                        viewModel.selectBooking(screen.bookingId)
                    }
                )
            }

            is AppScreen.MyBookings -> {
                MyBookingsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.Home) },
                    onBookingClick = { bookingId ->
                        viewModel.selectBooking(bookingId)
                    },
                    onNewBookingClick = {
                        viewModel.initBookingDraft(com.example.model.ServiceType.PLUMBING)
                    }
                )
            }

            is AppScreen.ContactSupport -> {
                ContactSupportScreen(
                    onBack = { viewModel.navigateTo(AppScreen.Home) }
                )
            }

            is AppScreen.BusinessPostsAndReviews -> {
                PostsAndReviewsScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.navigateTo(AppScreen.Home) }
                )
            }

            else -> {
                HomeScreen(
                    viewModel = viewModel,
                    onServiceSelected = { viewModel.initBookingDraft(it) },
                    onTaskSelected = { viewModel.initBookingDraft(it.serviceType, it) },
                    onViewActiveBooking = { viewModel.selectBooking(it) },
                    onNavigateToMyBookings = { viewModel.navigateTo(AppScreen.MyBookings) },
                    onNavigateToContact = { viewModel.navigateTo(AppScreen.ContactSupport) }
                )
            }
        }
    }
}
