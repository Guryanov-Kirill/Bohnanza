plugins {
    kotlin("jvm") version "1.9.24"
    id("org.jetbrains.compose") version "1.6.11"
}

group = "bohnanza"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))

    // Compose Desktop
    implementation(compose.desktop.currentOs)
    implementation(compose.material)
    implementation(compose.runtime)

    // JUnit 5
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(kotlin("test"))

    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
}

tasks.test {
    useJUnitPlatform()
}

compose.desktop {
    application {
        mainClass = "bohnanza.MainKt"
    }
}

kotlin {
    jvmToolchain(17)
}