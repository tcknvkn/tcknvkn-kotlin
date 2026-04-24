/*
 * -----------------------------------------------------------------------------
 * Proje: tcknvkn-kotlin
 * Dosya: build.gradle.kts
 * Açıklama: Kotlin/JVM derleme, test ve bağımlılık yapılandırması.
 * Oluşturma Tarihi: 2026-04-24
 * Lisans: MIT
 * Site: https://www.tcknvkn.com
 * -----------------------------------------------------------------------------
 */
plugins {
    kotlin("jvm") version "1.9.24"
}

group = "com.tcknvkn"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}