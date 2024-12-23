package org.example.model

import java.time.LocalDate

data class FriendDTO(
    val email: String,
    val name: String,
    val birthdate: LocalDate,
    val interests: List<String>
)
