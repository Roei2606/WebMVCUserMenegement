package org.example.model

import java.time.LocalDate

data class UserDTO(
    val email: String,       // כתובת האימייל של המשתמש
    val name: Name,          // שם המשתמש (פרטי ומשפחה)
    val birthdate: LocalDate, // תאריך הלידה
    val interests: List<String> // תחביבים
)
