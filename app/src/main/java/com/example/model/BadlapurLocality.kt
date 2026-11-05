package com.example.model

data class BadlapurLocality(
    val name: String,
    val zone: String, // "East" or "West"
    val postalCode: String = "421503"
)

val BADLAPUR_LOCALITIES = listOf(
    BadlapurLocality("Katrap", "East"),
    BadlapurLocality("Shirgaon", "East"),
    BadlapurLocality("Belavali", "West"),
    BadlapurLocality("Kulgaon", "East"),
    BadlapurLocality("Gandhi Chowk", "East"),
    BadlapurLocality("Rameshwadi", "West"),
    BadlapurLocality("Hendre Pada", "East"),
    BadlapurLocality("Badlapur Station", "East/West"),
    BadlapurLocality("Mankivali", "West"),
    BadlapurLocality("Juveli / MIDC", "East"),
    BadlapurLocality("Barrage Road", "West"),
    BadlapurLocality("Ambernath Link Road", "West")
)
