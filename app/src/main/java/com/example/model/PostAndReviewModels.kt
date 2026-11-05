package com.example.model

data class BusinessWorkPost(
    val id: String,
    val title: String,
    val description: String,
    val serviceCategory: String,
    val mediaType: MediaType, // PHOTO or VIDEO
    val mediaUrl: String? = null,
    val localUri: String? = null,
    val drawableRes: Int? = null,
    val locality: String = "Badlapur",
    val postedBy: String = "Business Owner",
    val timestamp: Long = System.currentTimeMillis()
)

enum class MediaType {
    PHOTO,
    VIDEO
}

data class CustomerReview(
    val id: String,
    val customerName: String,
    val locality: String,
    val serviceName: String,
    val rating: Float,
    val reviewComment: String,
    val date: String,
    val verifiedCustomer: Boolean = true
)
