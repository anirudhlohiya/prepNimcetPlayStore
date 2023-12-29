package com.example.prepnimcet

// User.kt
data class User(
    val name: String,
    val email: String,
    val emailVerified: Boolean = false
)