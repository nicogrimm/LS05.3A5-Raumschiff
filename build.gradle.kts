plugins {
    kotlin("jvm")
    application
}

group = "com.github.nicogrimm"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(22)
}

application {
    mainClass = "com.github.nicogrimm.SonnensystemKt"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}