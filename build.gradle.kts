import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

plugins {
    kotlin("jvm") version "2.2.0"
    `java-library`
    // dokka
    id("org.jetbrains.dokka") version "2.0.0"
    id("org.jetbrains.dokka-javadoc") version "2.0.0"
    // spotless
    id("com.diffplug.spotless") version "7.2.1"
    // Maven Central
    id("com.vanniktech.maven.publish") version "0.34.0"
}

group = "com.shotadft"
version = "1.1-SNAPSHOT"

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

    dokkaPublications.javadoc {
        outputDirectory.set(layout.buildDirectory.dir("javadoc"))
        suppressInheritedMembers.set(true)
        failOnWarning.set(true)
    }

    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/shotadft/KanaConverter/tree/master/src/main/kotlin")
            remoteLineSuffix.set("#L")
        }

        documentedVisibilities.set(setOf(
            VisibilityModifier.Public,
            VisibilityModifier.Internal
        ))
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

val dokkaJavadoc = tasks.named<DokkaGeneratePublicationTask>("dokkaGeneratePublicationJavadoc")

tasks.register<Jar>("javadocJar") {
    dependsOn(dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(dokkaJavadoc.flatMap { it.outputDirectory })
}

tasks.assemble {
    dependsOn(tasks.named<Jar>("javadocJar"))
}

val gitHubUserName = "shotadft"
val gitHubRepoName = "KanaConverter"

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates(
        group.toString(),
        project.rootProject.name,
        version.toString()
    )

    pom {
        name.set(project.rootProject.name)
        description.set("This is a library that converts Roman letters to both hiragana and katakana and vice versa.")
        inceptionYear.set("2025")
        url.set("https://github.com/${gitHubUserName}/KanaConverter")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org.licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set(gitHubUserName)
                name.set("Shouta Fukaya")
                url.set("https://github.com/${gitHubUserName}/")
            }
        }

        scm {
            connection = "scm:git:https://github.com/${gitHubUserName}/${gitHubRepoName}.git"
            developerConnection = "scm:git:git@github.com:${gitHubUserName}/${gitHubRepoName}.git"
            url = "https://github.com/${gitHubUserName}/$gitHubRepoName"
        }
    }
}