package org.example.model

import org.springframework.data.annotation.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship
import org.springframework.data.neo4j.core.schema.Relationship.Direction
import java.time.LocalDate
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Past
import javax.validation.constraints.Size

@Node
data class User(
    @Id
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 5) val password: String,
    @field:NotBlank val name: Name,
    @field:Past val birthdate: LocalDate,
    @field:NotEmpty val interests: List<String>,
    @Relationship(type = "FRIENDS", direction = Direction.OUTGOING)
    val friends: MutableSet<User> = mutableSetOf()
)

data class Name(
    @Id val id: String = java.util.UUID.randomUUID().toString(), // מזהה ייחודי
    val first: String,
    val last: String
)
