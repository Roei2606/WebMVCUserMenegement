package org.example.services

import org.example.boundarys.UserBoundary
import org.example.entities.UserEntity
import org.example.repositories.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    fun addUser(user: UserEntity): UserEntity {
        if (userRepository.existsByEmail(user.email!!)) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        if (user.firstName == null || user.lastName == null) {
            throw IllegalArgumentException("First name or last name is missing. Received: firstName=${user.firstName}, lastName=${user.lastName}")
             }
        val passwordPattern = Regex("^(?=.*\\d).{5,}$")
        if (user.password.isNullOrBlank() || !passwordPattern.matches(user.password!!)) {
            throw IllegalArgumentException("Password must be at least 8 characters long and contain at least one digit")
        }
        if (!user.birthdate.isNullOrBlank() && !user.birthdate!!.matches(Regex("\\d{2}-\\d{2}-\\d{4}"))) {
            throw IllegalArgumentException("Birthdate must be in the format dd-MM-yyyy")
        }
        if (user.interests.isNullOrEmpty() || user.interests!!.any { it.isBlank() }) {
            throw IllegalArgumentException("Interests must contain at least one non-empty string")
        }
        return userRepository.save(user)
    }

    fun getUserByEmailAndPassword(email: String, password: String): UserBoundary {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User with email $email does not exist")

        if (user.password != password) {
            throw IllegalArgumentException("Incorrect password for user with email $email")
        }
        return UserBoundary(user).apply { this.password = null }
    }

    fun getAllUsers(page: Int, size: Int): List<UserBoundary> {
        val pageable = PageRequest.of(page, size)
        return userRepository.findAll(pageable)
            .content
            .map { UserBoundary(it).apply { this.password = null } }
    }

    fun getAllByCriteria(criteria: String?, value: String?, size: Int, page: Int): List<UserBoundary> {
        val skip = page * size

        if (criteria.isNullOrBlank() || value.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid Input")
        }

        return when (criteria) {
            "byEmailDomain" -> userRepository.findByEmailDomain(value, skip, size)
            "byLastname" -> userRepository.findByLastName(value, skip, size)
            "byMinimumAge" -> userRepository.findByMinimumAge(value.toInt(), skip, size)
            else -> throw IllegalArgumentException("Criteria is invalid!")
        }.map { UserBoundary(it).apply { this.password = null } }
    }

    fun deleteAll() {
        userRepository.deleteAllData()
    }

    fun addFriend(userEmail: String, friendEmail: String) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User with email $userEmail not found.")
        val friend = userRepository.findByEmail(friendEmail)
            ?: throw IllegalArgumentException("User with email $friendEmail not found.")

        // Check if the friendship already exists
        if (user.friends.any { it.email == friendEmail }) {
            return
        }

        // Add bidirectional relationship
        user.friends.add(friend)
        friend.friends.add(user)

        // Save updates
        userRepository.save(user)
        userRepository.save(friend)
    }

    fun getUserFriends(email: String, page: Int, size: Int): List<UserBoundary> {
        val pageRequest = PageRequest.of(page, size)
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User with email $email does not exist")

        // Find friends and remove duplicates by email
        return userRepository.findFriendsByEmail(user.email!!, pageRequest)
            .distinctBy { it.email } // Remove duplicates based on email
            .map { UserBoundary(it).apply { this.password = null } }
    }

    fun removeFriendship(email: String, friendEmail: String) {
        if (!userRepository.existsById(email) || !userRepository.existsById(friendEmail)) {
            throw IllegalArgumentException("One or both users do not exist")
        }
        userRepository.removeFriendship(email, friendEmail)
    }

    fun removeAllFriends(email: String): Boolean {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User with email $email does not exist")

        try {
            userRepository.removeAllFriends(email)
            return true
        } catch (e: Exception) {
            println("Failed to remove friends for user $email")
            throw RuntimeException("Failed to remove friends", e)
        }
    }
}

