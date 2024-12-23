package org.example.controller

import org.example.model.FriendDTO
import org.example.model.User
import org.example.model.UserDTO
import org.example.model.UserWithEmail
import org.example.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.createUser(user))
    }

    @GetMapping("/{email}")
    fun getUser(@PathVariable email: String): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUser(email))
    }

    @GetMapping("/{email}/validate")
    fun getUserByEmailAndPassword(
        @PathVariable email: String,
        @RequestParam password: String
    ): ResponseEntity<UserDTO> {
        return ResponseEntity.ok(userService.getUserByEmailAndPassword(email, password))
    }

    @GetMapping
    fun getUsersWithPagination(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<UserDTO>> {
        val users = userService.getUsersWithPagination(page, size)
        return if (users.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(users)
    }

    @GetMapping("/by-domain")
    fun getUsersByEmailDomain(
        @RequestParam domain: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<UserDTO>> {
        val users = userService.getUsersByEmailDomain(domain, page, size)
        return if (users.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(users)
    }

    @GetMapping("/by-lastname")
    fun getUsersByLastName(
        @RequestParam lastname: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<UserDTO>> {
        val users = userService.getUsersByLastName(lastname, page, size)
        return if (users.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(users)
    }

    @GetMapping("/by-age")
    fun getUsersByMinimumAge(
        @RequestParam minimumAge: Int,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<UserDTO>> {
        val users = userService.getUsersByMinimumAge(minimumAge, page, size)
        return if (users.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(users)
    }

    @DeleteMapping
    fun deleteAllPeople(): ResponseEntity<Void> {
        userService.deleteAllPeople()
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{email}/friends")
    fun addFriend(
        @PathVariable email: String,
        @RequestBody userWithEmail: UserWithEmail
    ): ResponseEntity<String> {
        userService.addFriend(email, userWithEmail.email)
        return ResponseEntity.ok("Friend added successfully")
    }

    @GetMapping("/{email}/friends")
    fun getFriends(
        @PathVariable email: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<List<FriendDTO>> {
        val friends = userService.getFriendsByEmail(email, page, size)
        return if (friends.isEmpty()) ResponseEntity.noContent().build()
        else ResponseEntity.ok(friends)
    }

    @DeleteMapping("/{email}/friends/{friendEmail}")
    fun deleteFriend(
        @PathVariable email: String,
        @PathVariable friendEmail: String
    ): ResponseEntity<String> {
        userService.deleteFriend(email, friendEmail)
        return ResponseEntity.ok("Friendship between $email and $friendEmail has been removed")
    }

    @DeleteMapping("/{email}/friends")
    fun deleteAllFriends(
        @PathVariable email: String
    ): ResponseEntity<String> {
        userService.deleteAllFriends(email)
        return ResponseEntity.ok("All friendships for user $email have been removed")
    }
}