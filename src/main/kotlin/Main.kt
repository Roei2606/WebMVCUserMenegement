package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebMVCUserManagementApplication

fun main(args: Array<String>) {
    runApplication<WebMVCUserManagementApplication>(*args)
}