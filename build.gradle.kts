plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
    // dokka
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.dokka-javadoc") version "2.0.0"
    // spotless
    id("com.diffplug.spotless") version "7.2.1"
}

group = "com.shotadft"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("it.unimi.dsi:fastutil:8.5.16")
    implementation("org.apache.commons:commons-compress:1.28.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.19.2")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.13.4")
}

tasks.test {
    useJUnitPlatform()
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

spotless {
    kotlin {
        target("src/**/kotlin/**/*.kt")
        licenseHeaderFile(rootProject.file("config/license-header.txt"))
    }
}
