package org.example.repository

import org.example.model.User
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, String> {
    fun findByEmail(email: String): User?
}

