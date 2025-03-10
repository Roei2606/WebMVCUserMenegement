package org.example.model

import java.time.LocalDate

data class UserDTO(
    val email: String,
    val name: Name,
    val birthdate: LocalDate,
    val interests: List<String>
)

