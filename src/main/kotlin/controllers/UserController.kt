package org.example.controllers

import org.example.boundarys.EmailBoundary
import org.example.boundarys.UserBoundary
import org.example.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun createUser(@RequestBody userBoundary: UserBoundary): ResponseEntity<UserBoundary> {
        val userEntity = userBoundary.toEntity()
        val savedEntity = userService.addUser(userEntity)
        return ResponseEntity.ok(UserBoundary(savedEntity))
    }

    @GetMapping(
        path = ["/{email}"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUserByEmailAndPassword(
        @PathVariable email: String,
        @RequestParam password: String
    ): ResponseEntity<UserBoundary> {
        val user = userService.getUserByEmailAndPassword(email, password)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/all", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllUsers(
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "size", required = false, defaultValue = "10") size: Int
    ): List<UserBoundary> {
        return this.userService.getAllUsers(page, size)
    }

    @GetMapping(
        "/criteria",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUsersByCriteria(
        @RequestParam(name = "criteria", required = true) criteria: String,
        @RequestParam(name = "value", required = true) value: String,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "size", required = false, defaultValue = "10") size: Int
    ): List<UserBoundary> {
        return this.userService.getAllByCriteria(criteria, value, size, page)
    }

    @DeleteMapping("/people")
    fun deleteAll(): ResponseEntity<Void> {
        userService.deleteAll()
        return ResponseEntity.noContent().build()
    }

    @PutMapping(
        path = ["/{email}/friends"],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun addFriend(
        @PathVariable email: String,
        @RequestBody emailBoundary: EmailBoundary
    ): ResponseEntity<String> {
        userService.addFriend(email, emailBoundary.email)
        return ResponseEntity.ok("Friendship created successfully!")
    }

    @GetMapping(
        "/{email}/friends",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun getUserFriends(
        @PathVariable email: String,
        @RequestParam(name = "page", required = false, defaultValue = "0") page: Int,
        @RequestParam(name = "size", required = false, defaultValue = "10") size: Int
    ): List<UserBoundary> {
        return this.userService.getUserFriends(email, page, size)
    }

    @DeleteMapping("/{email}/friends/{friendEmail}")
    fun removeFriendship(
        @PathVariable email: String,
        @PathVariable friendEmail: String
    ): ResponseEntity<Void> {
        userService.removeFriendship(email, friendEmail)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{email}/friends")
    fun deleteAllFriends(@PathVariable email: String): ResponseEntity<String> {
        return try {
            userService.removeAllFriends(email)
            ResponseEntity.ok("All friends removed successfully for user $email")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to remove friends for user $email")
        }
    }
}