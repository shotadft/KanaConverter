import java.time.LocalDate

plugins {
    `java-library`
    alias(libs.plugins.spotless)
    alias(libs.plugins.maven.publish)
}

group = "com.shotadft"
version = "2.0-SNAPSHOT"

val targetJavaVersion = 25

repositories {
    mavenCentral()
}

dependencies {
    api(libs.jspecify)

    implementation(libs.fastutil)

    testImplementation(platform("org.junit:junit-bom:6.1.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

spotless {
    java {
        target("src/**/*.java")
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
        groupId = group as String,
        artifactId = project.rootProject.name,
        version = version as String
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