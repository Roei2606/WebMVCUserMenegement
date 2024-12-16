package org.example.service

import org.example.model.User
import org.example.model.UserDTO
import org.example.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: User): UserDTO {
        if (userRepository.existsById(user.email)) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        val savedUser = userRepository.save(user)
        return savedUser.toDTO()
    }

    fun getUser(email: String): UserDTO {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")
        return user.toDTO()
    }
}

fun User.toDTO(): UserDTO {
    return UserDTO(email, name, birthdate, interests)
}
