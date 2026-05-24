plugins {
    `java-library`
}

group = "com.shotadft"
version = "2.0-SNAPSHOT"

val targetJavaVersion = 25

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jspecify)

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    sourceCompatibility = JavaVersion.toVersion(targetJavaVersion)
    targetCompatibility = JavaVersion.toVersion(targetJavaVersion)
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
}

tasks.test {
    useJUnitPlatform()
}