package org.example.controller

import org.example.model.User
import org.example.model.UserDTO
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
}
