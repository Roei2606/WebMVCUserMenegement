package org.example.model

import javax.validation.constraints.Email

data class UserWithEmail(
    @field:Email
    val email: String
)