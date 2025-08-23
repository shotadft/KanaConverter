plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
    // dokka
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.dokka-javadoc") version "2.0.0"
}

group = "com.shotadft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

dokka {
    moduleName.set("KanaConverter")

    dokkaPublications.html {
        outputDirectory.set(layout.buildDirectory.dir("dokka"))
        suppressInheritedMembers.set(true)
        failOnWarning.set(true)
    }

    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/shotadft/KanaConverter")
            remoteLineSuffix.set("#L")
        }
    }

    pluginsConfiguration.html {
        footerMessage.set("(C) 2025 Shotadft")
    }
}
