package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebMvcUserManagementApplication

fun main(args: Array<String>) {
    runApplication<WebMvcUserManagementApplication>(*args)
}