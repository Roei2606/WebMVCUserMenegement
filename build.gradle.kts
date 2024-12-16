plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "il.ac.afeka.cloud"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21) // שימוש ב-Java 21
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // תלות ב-Spring Boot Web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // תלות ב-Jackson Module for Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // תלות ב-Kotlin Reflect
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // תלות ב-SpringDoc OpenAPI for Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // תלות ב-Spring Boot DevTools
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // תלות ב-Spring Boot Starter Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // תלות ב-Kotlin Test JUnit5
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // תלות ב-JUnit Platform Launcher
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // תלות ב-Spring Data Neo4j
    implementation("org.springframework.boot:spring-boot-starter-data-neo4j")

    // תלות ב-Spring Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("javax.validation:validation-api:2.0.1.Final")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
