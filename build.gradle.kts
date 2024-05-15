plugins {
    id("java")
    id("application")
}

group = "com.github.nicogrimm"
version = "1.0-SNAPSHOT"

application {
    mainClass = "com.github.nicogrimm.Sonnensystem"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}