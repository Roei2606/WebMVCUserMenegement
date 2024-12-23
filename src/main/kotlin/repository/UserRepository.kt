
package org.example.repository

import org.example.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : Neo4jRepository<User, String> {
    fun findByEmail(email: String): User?

    @Query(
        value = """
            MATCH (u:User) 
            WHERE u.email ENDS WITH ${'$'}domain 
            RETURN u
            SKIP ${'$'}skip 
            LIMIT ${'$'}limit
        """,
        countQuery = """
            MATCH (u:User) 
            WHERE u.email ENDS WITH ${'$'}domain 
            RETURN count(u)
        """
    )
    fun findByEmailDomain(
        @Param("domain") domain: String,
        @Param("skip") skip: Long,
        @Param("limit") limit: Long,
        pageable: Pageable
    ): Page<User>

    @Query(
        value = """
            MATCH (u:User) 
            WHERE u.name.last = ${'$'}lastname 
            RETURN u
            SKIP ${'$'}skip 
            LIMIT ${'$'}limit
        """,
        countQuery = """
            MATCH (u:User) 
            WHERE u.name.last = ${'$'}lastname 
            RETURN count(u)
        """
    )
    fun findByLastName(
        @Param("lastname") lastname: String,
        @Param("skip") skip: Long,
        @Param("limit") limit: Long,
        pageable: Pageable
    ): Page<User>

    @Query(
        value = """
            MATCH (u:User)
            WHERE datetime().year - datetime(u.birthdate).year >= ${'$'}minimumAge
            RETURN u
            SKIP ${'$'}skip 
            LIMIT ${'$'}limit
        """,
        countQuery = """
            MATCH (u:User)
            WHERE datetime().year - datetime(u.birthdate).year >= ${'$'}minimumAge
            RETURN count(u)
        """
    )
    fun findByMinimumAge(
        @Param("minimumAge") minimumAge: Int,
        @Param("skip") skip: Long,
        @Param("limit") limit: Long,
        pageable: Pageable
    ): Page<User>

    @Query(
        value = """
            MATCH (u:User {email: ${'$'}email})-[:FRIENDS]->(f:User)
            RETURN f
            SKIP ${'$'}skip 
            LIMIT ${'$'}limit
        """,
        countQuery = """
            MATCH (u:User {email: ${'$'}email})-[:FRIENDS]->(f:User)
            RETURN count(f)
        """
    )
    fun findFriendsByEmail(
        @Param("email") email: String,
        @Param("skip") skip: Long,
        @Param("limit") limit: Long,
        pageable: Pageable
    ): Page<User>
}