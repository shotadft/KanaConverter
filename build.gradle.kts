import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.time.LocalDate

plugins {
    kotlin("jvm") version "2.2.10"
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
version = "1.1.2"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
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

val gitHubUserName = "shotadft"
val gitHubRepoName = "KanaConverter"
val projectName = project.rootProject.name
    .split("-")
    .joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = group.toString(),
        artifactId = project.rootProject.name,
        version = version.toString()
    )

    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationJavadoc"),
            sourcesJar = true
        )
    )

    pom {
        name.set(projectName)
        description.set("This is a library that converts Roman letters to both hiragana and katakana and vice versa.")
        inceptionYear.set(LocalDate.now().year.toString())
        url.set("https://github.com/${gitHubUserName}/KanaConverter")

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org.licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set(gitHubUserName)
                name.set("Shouta Fukaya")
                email.set("98450322+shotadft@users.noreply.github.com")
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