package org.example.service

import org.example.model.FriendDTO
import org.example.model.User
import org.example.model.UserDTO
import org.example.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.neo4j.core.Neo4jClient
import org.springframework.data.neo4j.core.Neo4jTemplate
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val neo4jClient: Neo4jClient,
    private val neo4jTemplate: Neo4jTemplate
) {
    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun createUser(user: User): UserDTO {
        try {
            if (userRepository.existsById(user.email)) {
                throw IllegalArgumentException("User with email ${user.email} already exists")
            }
            val savedUser = userRepository.save(user)
            return savedUser.toDTO()
        } catch (e: Exception) {
            logger.error("Error creating user: ${e.message}", e)
            throw e
        }
    }

    fun getUser(email: String): UserDTO {
        try {
            val user = userRepository.findByEmail(email)
                ?: throw IllegalArgumentException("User not found with email: $email")
            return user.toDTO()
        } catch (e: Exception) {
            logger.error("Error getting user: ${e.message}", e)
            throw e
        }
    }

    fun getUserByEmailAndPassword(email: String, password: String): UserDTO {
        try {
            val user = userRepository.findByEmail(email)
                ?: throw IllegalArgumentException("User with email $email not found")

            if (user.password != password) {
                throw IllegalArgumentException("Invalid password for user $email")
            }

            return user.toDTO()
        } catch (e: Exception) {
            logger.error("Error validating user: ${e.message}", e)
            throw e
        }
    }

    fun getUsersWithPagination(page: Int, size: Int): List<UserDTO> {
        try {
            val pageable = PageRequest.of(page, size)
            val usersPage = userRepository.findAll(pageable)
            return usersPage.content.map { it.toDTO() }
        } catch (e: Exception) {
            logger.error("Error getting users with pagination: ${e.message}", e)
            throw e
        }
    }

    fun getUsersByEmailDomain(domain: String, page: Int, size: Int): List<UserDTO> {
        try {
            val pageable = PageRequest.of(page, size)
            val skip = page * size.toLong()
            val limit = size.toLong()

            val usersPage = userRepository.findByEmailDomain(domain, skip, limit, pageable)
            return usersPage.content.map { it.toDTO() }
        } catch (e: Exception) {
            logger.error("Error getting users by email domain: ${e.message}", e)
            throw e
        }
    }

    fun getUsersByLastName(lastname: String, page: Int, size: Int): List<UserDTO> {
        try {
            val pageable = PageRequest.of(page, size)
            val skip = page * size.toLong()
            val limit = size.toLong()

            val usersPage = userRepository.findByLastName(lastname, skip, limit, pageable)
            return usersPage.content.map { it.toDTO() }
        } catch (e: Exception) {
            logger.error("Error getting users by last name: ${e.message}", e)
            throw e
        }
    }

    fun getUsersByMinimumAge(minimumAge: Int, page: Int, size: Int): List<UserDTO> {
        try {
            val pageable = PageRequest.of(page, size)
            val skip = page * size.toLong()
            val limit = size.toLong()

            val usersPage = userRepository.findByMinimumAge(minimumAge, skip, limit, pageable)
            return usersPage.content.map { it.toDTO() }
        } catch (e: Exception) {
            logger.error("Error getting users by minimum age: ${e.message}", e)
            throw e
        }
    }

    fun deleteAllPeople() {
        try {
            neo4jClient.query("MATCH (n) DETACH DELETE n").run()
        } catch (e: Exception) {
            logger.error("Error deleting all people: ${e.message}", e)
            throw e
        }
    }

    fun addFriend(userEmail: String, friendEmail: String) {
        try {
            val query = """
                MATCH (u:User {email: ${'$'}userEmail}), (f:User {email: ${'$'}friendEmail})
                MERGE (u)-[:FRIENDS]->(f)
                MERGE (f)-[:FRIENDS]->(u)
            """

            neo4jClient.query(query)
                .bindAll(mapOf(
                    "userEmail" to userEmail,
                    "friendEmail" to friendEmail
                ))
                .run()
        } catch (e: Exception) {
            logger.error("Error adding friend: ${e.message}", e)
            throw e
        }
    }

    fun getFriendsByEmail(email: String, page: Int, size: Int): List<FriendDTO> {
        try {
            val pageable = PageRequest.of(page, size)
            val skip = page * size.toLong()
            val limit = size.toLong()

            val friendsPage = userRepository.findFriendsByEmail(email, skip, limit, pageable)
            return friendsPage.content.map { it.toFriendDTO() }
        } catch (e: Exception) {
            logger.error("Error getting friends: ${e.message}", e)
            throw e
        }
    }

    fun deleteFriend(userEmail: String, friendEmail: String) {
        try {
            val query = """
                MATCH (u:User {email: ${'$'}userEmail})-[r:FRIENDS]-(f:User {email: ${'$'}friendEmail})
                DELETE r
            """

            neo4jClient.query(query)
                .bindAll(mapOf(
                    "userEmail" to userEmail,
                    "friendEmail" to friendEmail
                ))
                .run()
        } catch (e: Exception) {
            logger.error("Error deleting friend: ${e.message}", e)
            throw e
        }
    }

    fun deleteAllFriends(userEmail: String) {
        try {
            val query = """
                MATCH (u:User {email: ${'$'}email})-[r:FRIENDS]-(f:User)
                DELETE r
            """

            neo4jClient.query(query)
                .bindAll(mapOf("email" to userEmail))
                .run()
        } catch (e: Exception) {
            logger.error("Error deleting all friends: ${e.message}", e)
            throw e
        }
    }

//    private fun User.toDTO(): UserDTO {
//        return UserDTO(
//            email = this.email,
//            name = this.name ?: Name("", "", ""),
//            birthdate = this.birthdate,
//            interests = this.interests
//        )
//    }
//
//    private fun User.toFriendDTO(): FriendDTO {
//        return FriendDTO(
//            email = this.email,
//            name = this.name ?: Name("", "", ""),
//            birthdate = this.birthdate,
//            interests = this.interests
//        )
//    }
private fun User.toDTO(): UserDTO {
    return UserDTO(
        email = this.email,
        name = this.name,
        birthdate = this.birthdate,
        interests = this.interests
    )
}

    private fun User.toFriendDTO(): FriendDTO {
        return FriendDTO(
            email = this.email,
            name = "${this.name.first} ${this.name.last}",
            birthdate = this.birthdate,
            interests = this.interests
        )
    }

}
