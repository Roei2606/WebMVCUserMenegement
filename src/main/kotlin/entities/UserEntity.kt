package org.example.entities

import org.springframework.data.annotation.Id
import org.springframework.data.neo4j.core.schema.Node
import org.springframework.data.neo4j.core.schema.Relationship

@Node("User")
class UserEntity(
    @Id var email: String?,
    var firstName: String?,
    var lastName: String?,
    var password: String?,
    var birthdate: String?,
    var interests: List<String>?,
//    @Relationship(type = "friends", direction = Relationship.Direction.UNDIRECTED)
//    var friends: List<UserEntity> ?
    @Relationship(type = "friends", direction = Relationship.Direction.OUTGOING)
    var friends: MutableList<UserEntity> = mutableListOf()
) {
    constructor() : this(null, null, null, null, null, emptyList())
}




