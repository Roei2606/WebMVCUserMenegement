package org.example.repositories

import org.example.entities.UserEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.neo4j.repository.Neo4jRepository
import org.springframework.data.neo4j.repository.query.Query
import org.springframework.data.repository.query.Param


interface UserRepository : Neo4jRepository<UserEntity, String> {
    fun existsByEmail(email: String): Boolean

    @Query("MATCH (u:User) WHERE u.email = \$email RETURN u")
    fun findByEmail(@Param("email") email: String): UserEntity?

    @Query(
        "MATCH (u:User) WHERE split(u.email, '@')[1] = \$domain " +
                "RETURN u ORDER BY u.email ASC SKIP \$skip LIMIT \$limit"
    )
    fun findByEmailDomain(
        @Param("domain") domain: String,
        @Param("skip") skip: Int,
        @Param("limit") limit: Int
    ): List<UserEntity>

    @Query(
        "MATCH (u:User) WHERE u.lastName = \$lastName " +
                "RETURN u ORDER BY u.lastName ASC SKIP \$skip LIMIT \$limit"
    )
    fun findByLastName(
        @Param("lastName") lastName: String,
        @Param("skip") skip: Int,
        @Param("limit") limit: Int
    ): List<UserEntity>

    @Query(
        "MATCH (u:User) " +
                "WHERE (date().year - toInteger(SPLIT(u.birthdate, '-')[2]) > \$ageInYears) OR " +
                "(date().year - toInteger(SPLIT(u.birthdate, '-')[2]) = \$ageInYears AND " +
                "(date().month > toInteger(SPLIT(u.birthdate, '-')[1]) OR " +
                "(date().month = toInteger(SPLIT(u.birthdate, '-')[1]) AND " +
                "date().day >= toInteger(SPLIT(u.birthdate, '-')[0])))) " +
                "RETURN u ORDER BY u.birthdate ASC SKIP \$skip LIMIT \$limit"
    )
    fun findByMinimumAge(
        @Param("ageInYears") ageInYears: Int,
        @Param("skip") skip: Int,
        @Param("limit") limit: Int
    ): List<UserEntity>

    @Query("MATCH (n) DETACH DELETE n")
    fun deleteAllData()


    @Query(
        " MATCH (u:User)-[:friends]-(friend:User)" +
                " WHERE u.email = \$email WITH DISTINCT friend" +
                " RETURN friend :#{orderBy(#pageable)} SKIP \$skip LIMIT \$limit"
    )
    fun findFriendsByEmail(@Param("email") email: String, pageable: Pageable): List<UserEntity>

    @Query("MATCH (u1:User {email: \$email})-[r:friends]-(u2:User {email: \$friendEmail}) DELETE r")
    fun removeFriendship(@Param("email") email: String, @Param("friendEmail") friendEmail: String)

    @Query("MATCH (u:User {email: \$email})-[r:friends]-(friend:User) DELETE r")
    fun removeAllFriends(@Param("email") email: String): Int

}