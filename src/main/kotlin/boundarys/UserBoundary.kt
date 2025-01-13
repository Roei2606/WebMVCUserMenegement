package org.example.boundarys

import org.example.entities.UserEntity

class UserBoundary(
    var email: String?,
    var name: NameBoundary?,
    var password: String?,
    var birthdate: String?,
    var interests: List<String>?
) {
    constructor() : this("", NameBoundary(), "", "", listOf())

    constructor(userEntity: UserEntity) : this() {
        this.email = userEntity.email
        this.name = NameBoundary(userEntity.firstName, userEntity.lastName)
        this.password = userEntity.password
        this.birthdate = userEntity.birthdate
        this.interests = userEntity.interests
    }

    fun toEntity(): UserEntity {
        return UserEntity(
            email = this.email,
            firstName = this.name?.first,
            lastName = this.name?.last,
            password = this.password,
            birthdate = this.birthdate,
            interests = this.interests
        )
    }

}


