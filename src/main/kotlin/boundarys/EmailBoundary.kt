package org.example.boundarys

data class EmailBoundary(
    val email: String
) {
    constructor() : this("") {}
}